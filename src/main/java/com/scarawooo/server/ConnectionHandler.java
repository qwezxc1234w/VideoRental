package com.scarawooo.server;

import com.scarawooo.dao.implementations.ClientDAOImpl;
import com.scarawooo.dao.implementations.ReserveUnitDAOImpl;
import com.scarawooo.dao.implementations.WarehouseUnitDAOImpl;
import com.scarawooo.dto.ClientDTO;
import com.scarawooo.dto.ReserveUnitDTO;
import com.scarawooo.dto.WarehouseUnitDTO;
import com.scarawooo.hibernate.SessionProvider;
import com.scarawooo.service.Notification;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Cleaner;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.scarawooo.service.ConnectionProperties.ADDRESS;
import static com.scarawooo.service.ConnectionProperties.PORT;

public class ConnectionHandler extends Thread {
    private final int POOL_SIZE = 50;
    private static final ArrayList<ClientService> clients = new ArrayList<>();
    private static final ArrayList<Token> tokens = new ArrayList<>();
    private ExecutorService clientsExecutor = Executors.newFixedThreadPool(POOL_SIZE);
    private ScheduledExecutorService cleanerExecutor = Executors.newSingleThreadScheduledExecutor();

    ConnectionHandler() {
        cleanerExecutor.scheduleAtFixedRate(
                new Cleaner(), Cleaner.CALL_FREQUENCY, Cleaner.CALL_FREQUENCY, Cleaner.DURATION);
    }

    @Override
    public void run() {
        Socket client;
        ClientService clientService;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("> Сервер запущен");
            while (!Thread.currentThread().isInterrupted()) {
                client = serverSocket.accept();
                clientService = new ClientService(client);
                synchronized (clients) {
                    clients.add(clientService);
                }
                clientsExecutor.execute(clientService);
                System.out.println("> Присоединился клиент " + client);
            }
        } catch (IOException exception) {
            System.err.println("Ошибка создания ServerSocket / ошибка установления соединения с клиентом");
        }
        finally {
            System.out.println("> Остановка сервера...");
            cleanerExecutor.shutdownNow();
            clientsExecutor.shutdownNow();
            synchronized (clients) {
                for (ClientService i : clients)
                    i.shutdown();
            }
            synchronized (tokens) {
                WarehouseUnitDAOImpl warehouseUnitDAO = new WarehouseUnitDAOImpl();
                if (!tokens.isEmpty()) {
                    boolean shutdownSessionProviderByToken = false;
                    for (Token token : tokens)
                        if (token.isValid()) {
                            shutdownSessionProviderByToken = true;
                            token.enableAutoFlushing();
                        }
                        else
                            warehouseUnitDAO.update(token.getWarehouseUnit());
                    if (!shutdownSessionProviderByToken)
                        SessionProvider.shutdown();
                }
                else
                    SessionProvider.shutdown();
            }
            System.out.println("> Сервер остановлен");
        }
    }

    void showConnections() {
        synchronized (clients) {
            for (ClientService i : clients) {
                System.out.println(i);
            }
        }
    }

    void shutdown() {
        interrupt();
        try {
            new Socket(ADDRESS, PORT).close();
        }
        catch (IOException error) {
            //
        }
    }

    private static class Cleaner implements Runnable {
        private static final int CALL_FREQUENCY = 1;
        private static final TimeUnit DURATION = TimeUnit.MINUTES;

        @Override
        public void run() {
            synchronized (clients) {
                for (int i = 0; i < clients.size() && !Thread.interrupted(); ++i) {
                    if (clients.get(i).isClosed())
                        clients.remove(i--);
                }
            }
            synchronized (tokens) {
                for (int i = 0; i < tokens.size() && !Thread.interrupted(); ++i) {
                    if (!tokens.get(i).isValid()) {
                        new WarehouseUnitDAOImpl().update(tokens.get(i).getWarehouseUnit());
                        tokens.remove(i--);
                    }
                }
            }
            System.gc();
        }
    }

    private static class ClientService implements Runnable {
        private ClientDTO clientDTO;
        private Socket socket;

        ClientService(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            Notification request, reply;
            try (ObjectInputStream socketObjectReader = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream socketObjectWriter = new ObjectOutputStream(socket.getOutputStream())) {
                while (!Thread.currentThread().isInterrupted()) {
                    request = (Notification) socketObjectReader.readObject();
                    System.out.println("> Клиент: " + socket + " Запрос: " + request);
                    switch (request) {
                        case AUTHORIZATION:
                            reply = auth((ClientDTO) socketObjectReader.readObject());
                            socketObjectWriter.writeObject(reply);
                            System.out.println("> Ответ: " + reply);
                            break;
                        case REGISTRATION:
                            reply = register((ClientDTO) socketObjectReader.readObject());
                            socketObjectWriter.writeObject(reply);
                            System.out.println("> Ответ: " + reply);
                            break;
                        case RESERVE:
                            boolean reserveBarrier = false;
                            ArrayList<Pair<Token, Integer>> factReservesAmount = null;
                            try {
                                if (clientDTO == null) {
                                    socketObjectWriter.writeObject(Notification.UNAUTHORIZED);
                                    System.out.println("> Клиент " + socket + " не авторизован");
                                }
                                else {
                                    socketObjectWriter.writeObject(Notification.DEFAULT);
                                    List reserves = (List) socketObjectReader.readObject();
                                    factReservesAmount = reserve(reserves);
                                    socketObjectWriter.writeObject(factReservesAmount);
                                    System.out.println("> Уточняем детали бронирования");
                                    if (socketObjectReader.readObject() == Notification.AGREE) {
                                        System.out.println("> Клиент " + socket + " подтвердил бронирование");
                                        reserveBarrier = true;
                                        try {
                                            for (int i = 0; i < reserves.size(); ++i) {
                                                ((ReserveUnitDTO) reserves.get(i)).setClient(clientDTO);
                                                ((ReserveUnitDTO) reserves.get(i)).setAmount(factReservesAmount.get(i).getValue());
                                                new ReserveUnitDAOImpl().insert(((ReserveUnitDTO) reserves.get(i)));
                                            }
                                            reply = Notification.SUCCESSFULLY;
                                        } catch (Throwable exception) {
                                            exception.printStackTrace();
                                            cancelReserve(factReservesAmount);
                                            reply = Notification.UNSUCCESSFULLY;
                                        }
                                        socketObjectWriter.writeObject(reply);
                                        System.out.println("> Ответ: " + reply);
                                    } else {
                                        System.out.println("> Клиент " + socket + " отказался от бронирования. Отмена бронирования");
                                        cancelReserve(factReservesAmount);
                                    }
                                }
                            }
                            catch (IOException | ClassNotFoundException | ClassCastException exception) {
                                if (!reserveBarrier) {
                                    System.err.println("> Соединение с клиентом " + socket + " потеряно. Отмена бронирования");
                                    if (factReservesAmount != null)
                                        cancelReserve(factReservesAmount);
                                }
                                throw exception;
                            }
                            finally {
                                if (factReservesAmount != null)
                                    for (Pair<Token, Integer> i : factReservesAmount)
                                        i.getKey().decrementUsingCounter();
                            }
                            break;
                        case UNRESERVE:
                            if (clientDTO == null) {
                                socketObjectWriter.writeObject(Notification.UNAUTHORIZED);
                                System.out.println("> Клиент " + socket + " не авторизован");
                            }
                            else {
                                socketObjectWriter.writeObject(Notification.DEFAULT);
                                ReserveUnitDTO reserveUnitDTO = (ReserveUnitDTO) socketObjectReader.readObject();
                                if (reserveUnitDTO.getClient().getId() == clientDTO.getId())
                                    reply = unreserve(reserveUnitDTO) ? Notification.SUCCESSFULLY : Notification.UNSUCCESSFULLY;
                                else
                                    reply = Notification.UNSUCCESSFULLY;
                                socketObjectWriter.writeObject(reply);
                                System.out.println("> Ответ: " + reply);
                            }
                            break;
                        case GET_GOODS:
                            socketObjectWriter.writeObject(new WarehouseUnitDAOImpl().getAll());
                            break;
                        case GET_RESERVES:
                            if (clientDTO == null) {
                                socketObjectWriter.writeObject(Notification.UNAUTHORIZED);
                                System.out.println("> Клиент " + socket + " не авторизован");
                            }
                            else {
                                socketObjectWriter.writeObject(Notification.DEFAULT);
                                socketObjectWriter.writeObject(new ReserveUnitDAOImpl().getClientReserves(clientDTO));
                            }
                            break;
                        case DISCONNECT:
                            Thread.currentThread().interrupt();
                            System.out.println("Клиент " + socket + " отключился");
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException | ClassCastException exception) {
                System.err.println("Связь с клиентом " + socket + " потеряна. Причина: " + exception.getMessage());
            }
        }

        void shutdown() {
            try {
                socket.close();
            } catch (IOException exception) {
                //
            }
        }

        boolean isClosed() {
            return socket.isClosed();
        }

        private ArrayList<Pair<Token, Integer>> reserve(List reserves) {
            ArrayList<Pair<Token, Integer>> factReservesAmount = new ArrayList<>();
            try {
                for (Object i : reserves) {
                    Token token = null;
                    synchronized (tokens) {
                        for (Token j : tokens)
                            if (j.getWarehouseUnit().equals(((ReserveUnitDTO) i).getWarehouseUnit())) {
                                token = j;
                                token.incrementUsingCounter();
                                break;
                            }
                        if (token == null) {
                            token = new Token(new WarehouseUnitDAOImpl().getById(((ReserveUnitDTO) i).getWarehouseUnit().getId()));
                            tokens.add(token);
                        }
                    }
                    token.acquire();
                    if (token.getWarehouseUnit().getAmount() >= ((ReserveUnitDTO) i).getAmount()) {
                        factReservesAmount.add(new Pair<>(token, ((ReserveUnitDTO) i).getAmount()));
                        token.getWarehouseUnit().setAmount(token.getWarehouseUnit().getAmount() - ((ReserveUnitDTO) i).getAmount());
                    } else {
                        factReservesAmount.add(new Pair<>(token, token.getWarehouseUnit().getAmount()));
                        token.getWarehouseUnit().setAmount(0);
                    }
                    token.release();
                }
            }
            catch (InterruptedException exception) {
                cancelReserve(factReservesAmount);
                Thread.currentThread().interrupt();
            }
            return factReservesAmount;
        }

        private boolean unreserve(ReserveUnitDTO reserveUnitDTO) {
            Token token = null;
            boolean result = true;
            try {
                synchronized (tokens) {
                    for (Token j : tokens)
                        if (j.getWarehouseUnit().equals(reserveUnitDTO.getWarehouseUnit())) {
                            token = j;
                            token.incrementUsingCounter();
                            break;
                        }
                    if (token == null) {
                        token = new Token(new WarehouseUnitDAOImpl().getById(reserveUnitDTO.getWarehouseUnit().getId()));
                        tokens.add(token);
                    }
                }
                token.acquire();
                token.getWarehouseUnit().setAmount(token.getWarehouseUnit().getAmount() + reserveUnitDTO.getAmount());
                new ReserveUnitDAOImpl().delete(reserveUnitDTO);
                token.release();
            }
            catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                result = false;
            }
            catch (Throwable exception) {
                result = false;
            }
            finally {
                if (token != null)
                    token.decrementUsingCounter();
            }
            return result;
        }

        private void cancelReserve(ArrayList<Pair<Token, Integer>> factReservesAmount) {
            boolean interruptedFlag = false;
            for (Pair<Token, Integer> i : factReservesAmount) {
                boolean unreserveFlag = false;
                while (!unreserveFlag) {
                    try {
                        i.getKey().acquire();
                        i.getKey().getWarehouseUnit().setAmount(i.getKey().getWarehouseUnit().getAmount() + i.getValue());
                        i.getKey().release();
                        unreserveFlag = true;
                    } catch (InterruptedException exception) {
                        interruptedFlag = true;
                    }
                }
            }
            if (interruptedFlag)
                Thread.currentThread().interrupt();
        }

        @Override
        public String toString() {
            return socket + " / Статус соединения: " + (socket.isClosed() ? "закрыто" : "открыто");
        }

        private Notification register(ClientDTO clientDTO) {
            Notification notification;
            ClientDAOImpl clientDAO = new ClientDAOImpl();
            if (clientDAO.isLoginOccupy(clientDTO)) {
                clientDAO.insert(clientDTO);
                notification = Notification.SUCCESSFULLY;
            }
            else {
                notification = Notification.UNSUCCESSFULLY;
            }
            return notification;
        }

        private Notification auth(ClientDTO clientDTO) {
            ClientDTO authResult = new ClientDAOImpl().getByLoginAndPass(clientDTO);
            if (authResult != null) {
                this.clientDTO = authResult;
                return Notification.SUCCESSFULLY;
            }
            else
                return Notification.UNSUCCESSFULLY;
        }
    }

    private static class Token extends Semaphore {
        private static int validTokenCounter = 0;
        private boolean isAutoFlushing = false;
        private WarehouseUnitDTO warehouseUnit;
        private int usingCounter = 1;

        Token(WarehouseUnitDTO warehouseUnit) {
            super(1);
            this.warehouseUnit = warehouseUnit;
            ++validTokenCounter;
        }

        synchronized void incrementUsingCounter() {
            ++usingCounter;
        }

        synchronized void decrementUsingCounter() {
            --usingCounter;
            if (isAutoFlushing) {
                if (usingCounter <= 0) {
                    new WarehouseUnitDAOImpl().update(warehouseUnit);
                    --validTokenCounter;
                }
                if (validTokenCounter <= 0)
                    SessionProvider.shutdown();
            }
        }

        synchronized boolean isValid() {
            return usingCounter > 0;
        }

        WarehouseUnitDTO getWarehouseUnit() {
            return warehouseUnit;
        }

        synchronized void enableAutoFlushing() {
            isAutoFlushing = true;
        }
    }
}
package com.scarawooo.server;

import com.scarawooo.hibernate.SessionProvider;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        SessionProvider.getSession();
        ConnectionHandler connectionHandler = new ConnectionHandler();
        connectionHandler.start();
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            out:
            while (true) {
                switch (consoleReader.readLine()) {
                    case "connections":
                        connectionHandler.showConnections();
                        break;
                    case "shutdown":
                        break out;
                }
            }
        }
        catch (IOException error) {
            System.err.println("Ошибка обработки запроса администратора");
        }
        finally {
            connectionHandler.shutdown();
        }
    }
}

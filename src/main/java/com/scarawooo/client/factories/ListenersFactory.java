package com.scarawooo.client.factories;

import com.scarawooo.client.Client;
import com.scarawooo.client.view.dialogs.*;
import com.scarawooo.dto.ReserveUnitDTO;
import com.scarawooo.dto.WarehouseUnitDTO;
import com.scarawooo.service.Notification;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;

public class ListenersFactory {
    public static MouseAdapter getGoodsMouseListener(JList<WarehouseUnitDTO> list,
                                                     DefaultListModel<WarehouseUnitDTO> basket) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                while (true) {
                    String query = JOptionPane.showInputDialog(null,
                            "Введите количество единиц проката " + list.getSelectedValue(),
                            "Добавление в корзину", JOptionPane.PLAIN_MESSAGE);
                    if (query != null) {
                        try {
                            int reserveAmount = Integer.parseInt(query);
                            if (reserveAmount < 1) throw new NumberFormatException();
                            boolean flag = false;
                            for (int i = 0; i < basket.size(); ++i) {
                                if (basket.get(i).equals(list.getSelectedValue())) {
                                    basket.get(i).setAmount(basket.get(i).getAmount() + reserveAmount);
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag)
                                basket.addElement(new WarehouseUnitDTO(list.getSelectedValue().getId(), list.getSelectedValue().getFilm(), reserveAmount));
                            list.getSelectedValue().setAmount(list.getSelectedValue().getAmount() - reserveAmount);
                            break;
                        } catch (NumberFormatException exception) {
                            JOptionPane.showMessageDialog(null, "Неверное количество",
                                    "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else break;
                }
            }
            }
        };
    }

    public static MouseAdapter getBasketMouseListener(JList<WarehouseUnitDTO> list, DefaultListModel<WarehouseUnitDTO> basketModel,
                                                      DefaultListModel<WarehouseUnitDTO> goodsModel) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && basketModel.size() != 0) {
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Удалить данную позицию из корзины?", "Удаление из корзины", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        for (int i = 0; i < goodsModel.size(); ++i)
                            if (goodsModel.get(i).equals(list.getSelectedValue()))
                                goodsModel.get(i).setAmount(goodsModel.get(i).getAmount() + list.getSelectedValue().getAmount());
                        basketModel.remove(list.getSelectedIndex());
                    }
                }
            }
        };
    }

    public static MouseAdapter getReservesMouseListener(JList<ReserveUnitDTO> list, DefaultListModel<ReserveUnitDTO> model) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && model.size() != 0) {
                    new UnreserveDialog(null, list.getSelectedValue()).call();
                }
            }
        };
    }

    public static ListSelectionListener getListSelectionListener() {
        return (ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting())
                if (((JList) e.getSource()).getSelectedValue() == null)
                    ((JList) e.getSource()).setSelectedIndex(0);
        };
    }

    public static ActionListener getLoginActionListener() {
        return (ActionEvent e) -> new LoginDialog(null).call();
    }

    public static ActionListener getRegistrationActionListener() {
        return (ActionEvent e) -> new RegistrationDialog(null).call();
    }

    public static ActionListener getUpdateActionListener() {
        return (ActionEvent e) -> {
            switch (Client.getAccess().getContent().getTabs().getModel().getSelectedIndex()) {
                case 0:
                    PostmenFactory.launchPostman(Notification.GET_GOODS);
                    break;
                case 2:
                    PostmenFactory.launchPostman(Notification.GET_RESERVES);
                    break;
            }
        };
    }

    public static ActionListener getSendAuthQueryListener(LoginDialog dialog) {
        return (ActionEvent e) -> {
            dialog.fill();
            dialog.dispose();
            PostmenFactory.launchPostman(Notification.AUTHORIZATION);
        };
    }

    public static ActionListener getSendRegQueryListener(RegistrationDialog dialog) {
        return (ActionEvent e) -> {
            dialog.fill();
            dialog.dispose();
            PostmenFactory.launchPostman(Notification.REGISTRATION);
        };
    }

    public static ActionListener getReserveAllListener(DefaultListModel<WarehouseUnitDTO> model) {
        return (ActionEvent e) -> {
            ArrayList<ReserveUnitDTO> reserves = new ArrayList<>();
            if (model.size() == 0) {
                JOptionPane.showMessageDialog(null, "Корзина пуста",
                        "", JOptionPane.ERROR_MESSAGE);
            }
            else {
                String temp = JOptionPane.showInputDialog(null,
                        "Укажите период бронирования", "Срок бронирования", JOptionPane.INFORMATION_MESSAGE);
                for (int i = 0; i < model.size(); ++i)
                    reserves.add(new ReserveUnitDTO(model.get(i), model.get(i).getAmount(), new Date(), new Date()));
                Client.getAccess().setReserves(reserves);
                PostmenFactory.launchPostman(Notification.RESERVE);
            }
        };
    }

    public static ActionListener getUnreserveListener(UnreserveDialog dialog) {
        return (ActionEvent e) -> {
            PostmenFactory.launchPostman(Notification.UNRESERVE);
            dialog.dispose();
        };
    }
}
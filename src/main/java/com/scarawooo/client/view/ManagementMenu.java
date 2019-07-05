package com.scarawooo.client.view;

import com.scarawooo.client.factories.ListenersFactory;

import javax.swing.*;

public class ManagementMenu extends JMenuBar {
    private JMenu user = new JMenu("User");
    private JMenu update = new JMenu("Update");
    private JMenuItem login = new JMenuItem("Log in");
    private JMenuItem register = new JMenuItem("Register");
    private JMenuItem updateGoodsList = new JMenuItem("Update");

    ManagementMenu() {
        super();
        login.addActionListener(ListenersFactory.getLoginActionListener());
        register.addActionListener(ListenersFactory.getRegistrationActionListener());
        user.add(login);
        user.add(register);
        updateGoodsList.addActionListener(ListenersFactory.getUpdateActionListener());
        update.add(updateGoodsList);
        add(user);
        add(update);
    }
}

package com.scarawooo.client.view.dialogs;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.ClientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginDialog extends JDialog {
    private static ClientDTO clientInfo;
    private JLabel label = new JLabel("Введите логин и пароль");
    private JTextField login = new JTextField();
    private JTextField password = new JPasswordField();
    private JButton send = new JButton("Отправить");

    public LoginDialog(JFrame owner) {
        super(owner, "Авторизация", true);
        setResizable(false);
        setBounds(600, 150, 300, 100);
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 20, 5));
        mainPanel.add(label);
        mainPanel.add(login);
        mainPanel.add(password);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        JPanel cascade = new JPanel();
        JPanel buttonPanel = new JPanel();
        send.addActionListener(ListenersFactory.getSendAuthQueryListener(this));
        buttonPanel.add(send);
        cascade.add(new JPanel(), BorderLayout.CENTER);
        cascade.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(cascade, BorderLayout.EAST);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public void call() {
        setVisible(true);
    }

    public void fill() {
        clientInfo = new ClientDTO(null, null, login.getText(), password.getText());
    }

    public static ClientDTO getLoginData() {
        return clientInfo;
    }
}

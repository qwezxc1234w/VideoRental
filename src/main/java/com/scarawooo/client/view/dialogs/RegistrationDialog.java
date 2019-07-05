package com.scarawooo.client.view.dialogs;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.ClientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegistrationDialog extends JDialog {
    private static ClientDTO clientInfo;
    private JLabel label = new JLabel("Заполните данные для регистрации");
    private JLabel nameLabel = new JLabel("Имя: ");
    private JTextField name = new JTextField();
    private JLabel surnameLabel = new JLabel("Фамилия: ");
    private JTextField surname = new JTextField();
    private JLabel loginLabel = new JLabel("Логин: ");
    private JTextField login = new JTextField();
    private JLabel passwordLabel = new JLabel("Пароль: ");
    private JTextField password = new JPasswordField();
    private JButton send = new JButton("Отправить");

    public RegistrationDialog(JFrame owner) {
        super(owner, "Регистрация", true);
        setResizable(false);
        setBounds(600, 150, 300, 200);
        JPanel topPanel = new JPanel();
        topPanel.add(label);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        centerPanel.add(nameLabel);
        centerPanel.add(name);
        centerPanel.add(surnameLabel);
        centerPanel.add(surname);
        centerPanel.add(loginLabel);
        centerPanel.add(login);
        centerPanel.add(passwordLabel);
        centerPanel.add(password);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        send.addActionListener(ListenersFactory.getSendRegQueryListener(this));
        bottom.add(send);
        getContentPane().add(bottom, BorderLayout.SOUTH);
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
        clientInfo = new ClientDTO(name.getText(), surname.getText(),
                login.getText(), password.getText());
    }

    public static ClientDTO getRegData() {
        return clientInfo;
    }
}

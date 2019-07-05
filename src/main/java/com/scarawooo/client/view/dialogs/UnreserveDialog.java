package com.scarawooo.client.view.dialogs;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.ReserveUnitDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UnreserveDialog extends JDialog {
    public UnreserveDialog(JFrame owner, ReserveUnitDTO reserve) {
        super(owner, "Информация о брони", true);
        JButton unreserve = new JButton("Аннулировать");
        JTextArea reserveInfo = new JTextArea(reserve.getInfo());
        setResizable(false);
        setBounds(600, 150, 300, 300);
        reserveInfo.setEditable(false);
        add(new JScrollPane(reserveInfo), BorderLayout.CENTER);
        add(unreserve, BorderLayout.SOUTH);
        unreserve.addActionListener(ListenersFactory.getUnreserveListener(this));
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
}

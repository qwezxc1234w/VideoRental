package com.scarawooo.client.view;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.ReserveUnitDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Reserves extends JPanel {
    private DefaultListModel<ReserveUnitDTO> model = new DefaultListModel<>();
    private JList<ReserveUnitDTO> reservesList = new JList<>(model);
    private JScrollPane scroll = new JScrollPane(reservesList);

    Reserves() {
        super();
        setBackground(Color.WHITE);
        reservesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservesList.setBorder(new LineBorder(Color.BLACK));
        reservesList.addListSelectionListener(ListenersFactory.getListSelectionListener());
        reservesList.addMouseListener(ListenersFactory.getReservesMouseListener(reservesList, model));
        add(scroll, BorderLayout.CENTER);
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public JList<ReserveUnitDTO> getReservesList() {
        return reservesList;
    }

    public DefaultListModel<ReserveUnitDTO> getModel() {
        return model;
    }
}

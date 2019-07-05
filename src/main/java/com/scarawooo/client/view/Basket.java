package com.scarawooo.client.view;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.WarehouseUnitDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Basket extends JPanel {
    private DefaultListModel<WarehouseUnitDTO> model = new DefaultListModel<>();
    private JList<WarehouseUnitDTO> basketList = new JList<>(model);
    private JScrollPane scroll = new JScrollPane(basketList);
    private JButton reserveAll = new JButton("Забронировать все");

    Basket() {
        super();
        setBackground(Color.WHITE);
        basketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        basketList.setBorder(new LineBorder(Color.BLACK));
        add(scroll, BorderLayout.CENTER);
        add(reserveAll, BorderLayout.SOUTH);
    }

    public void addListeners(DefaultListModel<WarehouseUnitDTO> goodsModel) {
        basketList.addMouseListener(ListenersFactory.getBasketMouseListener(basketList, model, goodsModel));
        basketList.addListSelectionListener(ListenersFactory.getListSelectionListener());
        reserveAll.addActionListener(ListenersFactory.getReserveAllListener(model));
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public DefaultListModel<WarehouseUnitDTO> getModel() {
        return model;
    }
}

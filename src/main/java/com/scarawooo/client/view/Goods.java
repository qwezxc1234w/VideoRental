package com.scarawooo.client.view;

import com.scarawooo.client.factories.ListenersFactory;
import com.scarawooo.dto.WarehouseUnitDTO;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Goods extends JPanel {
    private DefaultListModel<WarehouseUnitDTO> model = new DefaultListModel<>();
    private JList<WarehouseUnitDTO> goodsList = new JList<>(model);
    private JScrollPane scroll = new JScrollPane(goodsList);

    Goods() {
        super();
        setBackground(Color.WHITE);
        goodsList.setBorder(new LineBorder(Color.BLACK));
        goodsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(scroll, BorderLayout.CENTER);
    }

    public void addListeners(DefaultListModel<WarehouseUnitDTO> basketModel) {
        goodsList.addListSelectionListener(ListenersFactory.getListSelectionListener());
        goodsList.addMouseListener(ListenersFactory.getGoodsMouseListener(goodsList, basketModel));
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public DefaultListModel<WarehouseUnitDTO> getModel() {
        return model;
    }
}

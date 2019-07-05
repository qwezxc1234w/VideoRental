package com.scarawooo.client.view;

import com.scarawooo.client.factories.PostmenFactory;
import com.scarawooo.service.Notification;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Content extends JFrame {
    private static final String title = "Video Rental";
    private final int WIDTH = 700;
    private final int HEIGHT = 500;
    private ManagementMenu managementMenu = new ManagementMenu();
    private Goods goods = new Goods();
    private Basket basket = new Basket();
    private Reserves reserves = new Reserves();
    private JTabbedPane tabs = new JTabbedPane();

    public Content() {
        super(title);
        setJMenuBar(managementMenu);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(400, 10, WIDTH, HEIGHT);
        setResizable(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                goods.getScroll().setPreferredSize(goods.getSize());
                basket.getScroll().setSize(basket.getWidth(), basket.getHeight() - 40);
                basket.getScroll().setPreferredSize(basket.getScroll().getSize());
                reserves.getScroll().setPreferredSize(reserves.getSize());
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PostmenFactory.launchPostman(Notification.DISCONNECT);
            }
        });
        goods.addListeners(basket.getModel());
        basket.addListeners(goods.getModel());
        tabs.addTab("Goods", goods);
        tabs.addTab("Basket", basket);
        tabs.addTab("Reserves", reserves);
        getContentPane().add(tabs);
        setVisible(true);
    }

    public JTabbedPane getTabs() {
        return tabs;
    }

    public Basket getBasket() {
        return basket;
    }

    public Goods getGoods() {
        return goods;
    }

    public Reserves getReserves() {
        return reserves;
    }
}

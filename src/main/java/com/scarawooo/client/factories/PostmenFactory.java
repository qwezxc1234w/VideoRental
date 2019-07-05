package com.scarawooo.client.factories;

import com.scarawooo.client.Client;
import com.scarawooo.service.Notification;

public class PostmenFactory {
    public static void launchPostman(Notification query) {
        Thread thread = new Thread(() -> {
            try {
                Client.getAccess().getExchanger().exchange(query);
            }
            catch (InterruptedException exception) {
                System.err.println("Поток отправки запроса " + query + " прерван");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}

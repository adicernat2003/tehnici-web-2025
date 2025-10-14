package service;

import utils.NotificationManager;

public class MessagingService {

    private final NotificationManager notificationManager;

    public MessagingService(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void sendMessage() {
        notificationManager.sendNotification();
    }
}

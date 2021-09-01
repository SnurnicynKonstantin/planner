package xrm.extrim.planner.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import xrm.extrim.planner.domain.Notification;

@Component
public class WsSender {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String login, Notification notification) {
        simpMessagingTemplate.convertAndSendToUser(login, "/queue/notifications", notification);
    }

    public void sendNotificationsCount(String login, String text) {
        simpMessagingTemplate.convertAndSendToUser(login, "/queue/notificationsCount", text);
    }
}

package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.WsSender;
import xrm.extrim.planner.domain.Notification;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.repository.NotificationRepository;
import xrm.extrim.planner.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Value("${enable_notifications}")
    private boolean notificationsEnabled;

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final WsSender wsSender;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository, WsSender wsSender) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.wsSender = wsSender;
    }

    public List<Notification> findAllNotificationsForUser(Long userId) {
        return notificationRepository.findAllByRecipientIdOrderByCreateDateAsc(userId);
    }

    public void sendNotification(Long recipientId, String text) {
        User user = userRepository.findById(recipientId).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), recipientId));
        }
        if (notificationsEnabled) {
            wsSender.sendNotification(user.getLogin(),
                    notificationRepository.save(
                            new Notification(
                                    recipientId,
                                    text,
                                    LocalDateTime.now()
                            )
                    )
            );
        }
    }

    public void markNotificationAsReadById(Long userId, Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            throw new EntityNotFoundException(String.format(Exception.NOTIFICATION_NOT_FOUND.getDescription(), id));
        }
        if (!notification.getRecipientId().equals(userId)) {
            throw new ForbiddenException(Exception.NOT_YOUR_NOTIFICATION.getDescription());
        }
        if (!notification.isChecked()) {
            notification.setChecked(true);
            notificationRepository.save(notification);
        }
    }

    public List<Notification> findAllUnreadNotificationsForUser(Long userId) {
        return notificationRepository.findAllByRecipientIdAndIsChecked(userId, false);
    }

    public int getUnreadNotificationsCountForUser(Long userId) {
        return notificationRepository.countByRecipientIdAndIsCheckedOrderByCreateDateAsc(userId, false);
    }

    public void markAllUserNotificationsAsRead(Long userId) {
       findAllUnreadNotificationsForUser(userId).forEach(notification -> markNotificationAsReadById(userId, notification.getId()));
    }
}

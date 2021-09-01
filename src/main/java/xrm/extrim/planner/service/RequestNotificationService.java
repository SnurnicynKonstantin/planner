package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.NotificationMessages;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Mail;

import java.util.Collections;
import java.util.List;

@Service
public class RequestNotificationService {
    @Value("${base.url}")
    private String urlBase;
    private final static String REQUESTS_PAGE = "request/";

    private final MailService mailService;
    private final NotificationService notificationService;
    private final CategorySubscribersService categorySubscribersService;

    public RequestNotificationService(MailService mailService, NotificationService notificationService,
                                      CategorySubscribersService categorySubscribersService) {
        this.mailService = mailService;
        this.notificationService = notificationService;
        this.categorySubscribersService = categorySubscribersService;
    }

    public void sendRequestCreatedEmail(Request request) {
        List<User> users = categorySubscribersService.getAllUsersFromCategory(request.getCategory().getId());
        String subject = String.format(Mail.REQUEST_CREATED_SUBJECT.getText(), request.getCategory().getDescription(), request.getTitle());
        String body = String.format(Mail.REQUEST_CREATED_TEXT.getText(), urlBase + REQUESTS_PAGE);
        mailService.sendEmailToUsers(users, subject, body);
    }

    public void sendRequestUpdateStatusEmail(Request request) {
        List<User> users = Collections.singletonList(request.getCreator());
        String subject = String.format(Mail.REQUEST_UPDATE_STATUS_SUBJECT.getText(), request.getTitle());
        String body = String.format(Mail.REQUEST_UPDATE_STATUS_TEXT.getText(), request.getTitle(), request.getStatus().getDescription());
        mailService.sendEmailToUsers(users, subject, body);
    }

    public void sendNotificationAboutComment(User commentAuthor, Request request) {
        String notificationText = String.format(NotificationMessages.REQUEST_NEW_COMMENT.getMessage(),
                commentAuthor.getName(), commentAuthor.getSurname(), REQUESTS_PAGE, request.getTitle());
        notificationService.sendNotification(request.getCreator().getId(), notificationText);
        if (request.getExecutor() != null) {
            notificationService.sendNotification(request.getExecutor().getId(), notificationText);
        }
    }
}

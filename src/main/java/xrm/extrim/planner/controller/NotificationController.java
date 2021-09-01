package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import xrm.extrim.planner.domain.Notification;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.service.NotificationService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/notifications")
@Api(description = "Operations pertaining to notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @ApiOperation("Get all notifications for current user")
    public List<Notification> getAllNotificationsForUser(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        return notificationService.findAllNotificationsForUser(currentUser.getId());
    }

    @GetMapping("/new")
    @ApiOperation("Get unread notifications for current user")
    public List<Notification> getAllUnreadNotificationsForUser(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        return notificationService.findAllUnreadNotificationsForUser(currentUser.getId());
    }

    @GetMapping("/new/count")
    @ApiOperation("Get count of unread notifications for current user")
    public int getUnreadNotificationsCountForUser(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        return notificationService.getUnreadNotificationsCountForUser(currentUser.getId());
    }

    @PutMapping("/{id}")
    @ApiOperation("Set notification as read")
    public void markNotificationAsRead(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                       @ApiParam("Notification id") @PathVariable(name = "id") Long notificationId) {
        notificationService.markNotificationAsReadById(currentUser.getId(), notificationId);
    }

    @PutMapping("/all")
    @ApiOperation("Mark all user notifications as read")
    public void markAllUserNotificationsAsRead(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        notificationService.markAllUserNotificationsAsRead(currentUser.getId());
    }
}

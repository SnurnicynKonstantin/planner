package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.domain.User;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BirthdayService {
   private final UserService userService;
   private final MailService mailService;

    @Autowired
    public BirthdayService(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "${mail.cron.timeTriggerBirthdayEmail}")
    public void sendBirthdayMessage() {
        LocalDate now = LocalDate.now();
        List<User> allUsers = userService.getUsers();
        for(User user: allUsers) {
            if(user.getBirthday().getMonth().equals(now.getMonth()) && user.getBirthday().getDayOfMonth() == now.getDayOfMonth()) {
                mailService.sendBirthdayEmail(user);
            }
        }
    }

    @Scheduled(cron = "${mail.cron.timeTriggerBirthdayInfoEmail}")
    public void sendBirthdayInfoMessage() {
        LocalDate now = LocalDate.now();
        List<User> allUsers = userService.getUsers();
        allUsers.removeIf(user -> !user.getBirthday().getMonth().equals(now.getMonth()));
        allUsers = allUsers.stream().sorted(Comparator.comparingInt(o -> o.getBirthday().getDayOfMonth()))
                .collect(Collectors.toList());
        mailService.sendBirthdayInfoEmail(allUsers);
    }

}


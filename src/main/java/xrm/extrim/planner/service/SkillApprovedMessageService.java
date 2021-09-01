package xrm.extrim.planner.service;

import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.NotificationMessages;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;

import java.util.function.BiPredicate;

@Service
public class SkillApprovedMessageService {
    private final NotificationService notificationService;

    BiPredicate<UserSkill, UserSkill> checkIfSend = (oldUserSkill, updatedUserSkill) -> !oldUserSkill.isConfirmed() && updatedUserSkill.isConfirmed();

    public SkillApprovedMessageService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(Long userId, User currentUser, Skill skill) {
        if (currentUser.getRole().isRateSkill() && !currentUser.getId().equals(userId)) {
            String notificationText = appendWhoApprovedSkillInfo(String.format(NotificationMessages.SKILL_APPROVE.getMessage(), skill.getName()), userId, currentUser);
            notificationService.sendNotification(userId, notificationText);
        }
    }

    private String appendWhoApprovedSkillInfo(String text, Long recipientId, User currentUser) {
        if (!recipientId.equals(currentUser.getId())) {
            return text + String.format(NotificationMessages.YOUR_SKILL_WAS_APPROVED_BY.getMessage(), currentUser.getName(), currentUser.getSurname());
        }

        return text;
    }
}

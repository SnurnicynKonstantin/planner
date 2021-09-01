package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByRecipientIdOrderByCreateDateAsc(Long recipientId);

    List<Notification> findAllByRecipientIdAndIsChecked(Long recipientId, Boolean isChecked);

    int countByRecipientIdAndIsCheckedOrderByCreateDateAsc(Long recipientId, Boolean isChecked);
}

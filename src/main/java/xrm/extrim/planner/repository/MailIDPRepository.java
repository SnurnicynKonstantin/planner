package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.MailIDP;

import java.time.LocalDate;

public interface MailIDPRepository extends JpaRepository <MailIDP, Long> {
    MailIDP findAllByIdpDateBetweenAndUserId(LocalDate currentDate, LocalDate idpDate, Long userId);
}

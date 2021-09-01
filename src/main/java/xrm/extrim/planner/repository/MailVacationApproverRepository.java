package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.MailVacationApprover;

public interface MailVacationApproverRepository extends JpaRepository<MailVacationApprover, Long> {
    boolean existsByApprover_IdAndRequest_Id(Long approverId, Long requestId);
}

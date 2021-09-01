package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import xrm.extrim.planner.domain.VacationApprover;

import java.util.List;

public interface VacationApproverRepository extends JpaRepository<VacationApprover, Long> {
    @Transactional
    @Modifying
    void deleteByRequestId(Long requestId);

    @Query("SELECT w.approverId FROM VacationApprover w WHERE w.requestId = ?1")
    List<Long> getApproverIdsByRequestId(Long requestId);

    VacationApprover getByRequestIdAndApproverId(Long requestId, Long approverId);

    @Query("SELECT w.isApproved FROM VacationApprover w WHERE w.requestId = ?1")
    List<Boolean> getIsApprovedByRequestId(Long requestId);


}


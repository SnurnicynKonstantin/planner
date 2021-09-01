package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xrm.extrim.planner.domain.VacationRequest;

import java.time.LocalDate;
import java.util.List;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> getAllByCreatorId(Long creatorId);

    @Query("select vr from VacationRequest vr where vr.id in " +
            "(select vrv.id from VacationRequestView vrv where vrv.isApproved = false and vrv.dateFrom = :overdueDate)")
    List<VacationRequest> getAllOverdueVacationRequests(LocalDate overdueDate);
}

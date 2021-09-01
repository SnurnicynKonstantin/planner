package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.RateDescription;

import java.util.List;

public interface RateDescriptionRepository extends JpaRepository<RateDescription, Long> {
    List<RateDescription> findBySkillId(Long id);
    List<RateDescription> findBySkillIdAndRateNumber(Long id, int rateNumber);
}

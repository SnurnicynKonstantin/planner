package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.RequestCategory;

public interface RequestCategoryRepository extends JpaRepository<RequestCategory, Long> {
    RequestCategory findByName(String name);
}

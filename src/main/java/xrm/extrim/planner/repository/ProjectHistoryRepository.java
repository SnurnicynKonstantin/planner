package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.ProjectHistory;

import java.util.List;

public interface ProjectHistoryRepository extends JpaRepository<ProjectHistory, Long> {
    List<ProjectHistory> findAllByUserIdOrderByWorkStartDateDesc(Long id);

    List<ProjectHistory> findByProjectId(Long projectId);
}

package xrm.extrim.planner.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByName(String name);

    Page<Project> findAllByOrderByIdDesc(Pageable pageable);
}

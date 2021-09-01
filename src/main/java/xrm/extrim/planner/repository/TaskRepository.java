package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);
    List<Task> findAllByParentTaskIsNull();
    List<Task> findAllByUser_IdAndParentTaskIsNull(Long userId);
}

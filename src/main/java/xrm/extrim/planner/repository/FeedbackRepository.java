package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Feedback;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByOrderByCreationDateDesc();

    List<Feedback> findByIsArchivedTrueOrderByCreationDateDesc();

    List<Feedback> findByIsArchivedFalseOrderByCreationDateDesc();
}

package xrm.extrim.planner.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.RequestComment;

import java.util.List;

public interface RequestCommentRepository extends JpaRepository<RequestComment, Long> {
    List<RequestComment> findAllByRequest_Id(Long requestId, Sort sort);
}

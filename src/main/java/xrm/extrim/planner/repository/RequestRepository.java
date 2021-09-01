package xrm.extrim.planner.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import xrm.extrim.planner.domain.Request;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.domain.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>,
        QuerydslPredicateExecutor<Request> {

    List<Request> findAllByCreator(User creator);

    @Query("select r from Request r where " +
            "r.title like :title and " +
            "(:category is null or r.category = :category) and " +
            "r.creator.id = :creatorId")
    List<Request> filter(String title, RequestCategory category, Long creatorId);

    @Query("select r from Request r where " +
            "r.title like :title and " +
            "(:category is null or r.category = :category)")
    List<Request> filter(String title, RequestCategory category);

    default Page<Request> findAllByPredicate(Predicate predicate, Pageable pageable) {
        return findAll(predicate, pageable);
    }
}

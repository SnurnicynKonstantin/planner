package xrm.extrim.planner.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import xrm.extrim.planner.view.UserView;

import java.util.List;

public interface UserViewRepository extends JpaRepository<UserView, Long>,
        QuerydslPredicateExecutor<UserView> {

    default Page<UserView> findAllByPredicate(Predicate predicate, Pageable pageable) {
        return findAll(predicate, pageable);
    }

    default List<UserView> findAllByPredicate(Predicate predicate) {
        return (List<UserView>) findAll(predicate);
    }
}

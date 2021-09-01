package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.CategorySubscriber;
import xrm.extrim.planner.domain.RequestCategory;
import xrm.extrim.planner.domain.User;

import java.util.List;

public interface CategorySubscriberRepository  extends JpaRepository<CategorySubscriber, Long> {
    CategorySubscriber findBySubscriberAndRequestCategory(User subscriber, RequestCategory requestCategory);

    List<CategorySubscriber> findAllByRequestCategory(RequestCategory requestCategory);

    List<CategorySubscriber> findAllBySubscriber(User subscriber);
}

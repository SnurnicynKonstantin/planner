package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import xrm.extrim.planner.domain.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>,
        QuerydslPredicateExecutor<User> {

    @Query("SELECT u FROM User u " +
            "where " +
            "   (:name is null or u.name = :name) and " +
            "   (:surname is null or u.surname = :surname) and " +
            "   (:login is null or u.login = :login)")
    List<User> findAllFilteredUser(String name, String surname, String login);

    @Query("select u from User u " +
            "where u.id in " +
            "   (select t.user.id from UserSkill t " +
            "               where t.skill.id in :skills)")
    List<User> filter(List<Long> skills);

    User findByLogin(String login);

    List<User> findAllByIdpDateBetween(LocalDate currentDate, LocalDate idpDate);

    boolean existsByLogin(String login);

    @Query("select u from User u where u.id in " +
            "(select va.id from VacationApprover va where va.requestId = :requestId and va.isApproved = true)")
    List<User> findAllApproversNotApproveByRequestId(Long requestId);

    List<User> findAllByLoginIn(Collection<String> login);
}

package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByName(String string);

    boolean existsByName(String name);
}

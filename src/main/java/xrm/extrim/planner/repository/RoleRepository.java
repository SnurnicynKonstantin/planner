package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByName(String name);
}

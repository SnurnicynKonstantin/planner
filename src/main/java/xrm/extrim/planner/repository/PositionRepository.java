package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Position;


public interface PositionRepository extends JpaRepository<Position, Long> {
    boolean existsByName(String name);
    Position getByName(String name);
    Position findByDescription(String description);
    Position findByName(String name);
}

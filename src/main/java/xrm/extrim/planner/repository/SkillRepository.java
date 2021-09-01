package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xrm.extrim.planner.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Skill findByName(String name);
}

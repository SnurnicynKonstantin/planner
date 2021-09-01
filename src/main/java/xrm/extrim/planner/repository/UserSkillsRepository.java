package xrm.extrim.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xrm.extrim.planner.domain.UserSkill;

import java.util.List;

public interface UserSkillsRepository extends JpaRepository<UserSkill, Long> {
    @Query(value = "select id from (" +
                        "select max(id) as id, skill_id, max(version)" +
                            "from user_skill " +
                                "where user_id = ?1 " +
                                "group by skill_id) as table1;", nativeQuery = true)
    List<Long> findLatestVersionsIdsByUserId(Long userId);
    List<UserSkill> findAllByUserId(Long userId);
    UserSkill findByUserIdAndSkillId(Long userId, Long skillId);

    @Query(value = "select id from (" +
                        "select max(id) as id, skill_id, max(version) as version " +
                            "from user_skill " +
                                "where user_id = ?1 and skill_id = ?2 " +
                                "group by skill_id) as table1;",
            nativeQuery = true)
    Long findLatestVersionIdBySkillIdAndUserId(Long userId, Long skillId);
}

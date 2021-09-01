package xrm.extrim.planner.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_skill_v")
public class UserSkillView {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "skill_rate")
    private Integer skillRate;

    @Column(name = "version")
    private Integer version;

    @Column(name = "skill_id")
    private Long skillId;
}

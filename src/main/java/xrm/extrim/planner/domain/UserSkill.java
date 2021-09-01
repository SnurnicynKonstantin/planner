package xrm.extrim.planner.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "user_skill")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSkill extends BaseEntity{

    private Integer rate;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @JsonBackReference("user-userSkills")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference("skill-userSkills")
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "version")
    private Integer version;

    @Column(name = "comment")
    private String comment;

    @Column(name = "deleted")
    private boolean isDeleted;
}

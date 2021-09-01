package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "skill")
@Data
@EqualsAndHashCode(callSuper = true)
public class Skill extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @JsonManagedReference("skill-rateDescription")
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<RateDescription> rateDescriptions;


    @JsonManagedReference("skill-userSkills")
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<UserSkill> userSkills;

    @JsonBackReference("skill-group")
    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group group;

}

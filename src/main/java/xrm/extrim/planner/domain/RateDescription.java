package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "rate_description")
@EqualsAndHashCode(callSuper = true)
@Data
public class RateDescription extends BaseEntity {

    @Column(name = "rate_number")
    private int rateNumber;

    @Column(name = "skill_id", insertable = false, updatable = false)
    private Long skillId;

    @Column(name = "description")
    private String description;

    @JsonBackReference("skill-rateDescription")
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "skill_id")
    @ToString.Exclude
    private Skill skill;
}

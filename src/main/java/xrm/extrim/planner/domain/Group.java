package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "group")
@Data
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity {
    @NotNull
    @Column(name = "name")
    private String name;

    @JsonManagedReference("skill-group")
    @OneToMany(mappedBy = "group")
    private List<Skill> skills;

}

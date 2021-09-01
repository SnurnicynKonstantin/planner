package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "department")
@NoArgsConstructor
public class Department extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2526133066067199720L;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Department(Long id, String name, String description) {
        this.setId(id);
        this.name = name;
        this.description = description;
    }
}

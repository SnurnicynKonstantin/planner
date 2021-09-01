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
@Table(name = "position")
@NoArgsConstructor
public class Position extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1419725748018220091L;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public Position(Long id, String name, String description) {
        this.setId(id);
        this.name = name;
        this.description = description;
    }
}

package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "request_category")
@NoArgsConstructor
public class RequestCategory extends BaseEntity {
    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}

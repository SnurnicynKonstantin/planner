package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import xrm.extrim.planner.view.ProjectViews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project  extends BaseEntity {
    @JsonView(ProjectViews.IdNameDate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq")
    @GenericGenerator(name = "seq", strategy="increment")
    private Long id;

    @JsonView(ProjectViews.IdNameDate.class)
    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @JsonView(ProjectViews.IdNameDate.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonView(ProjectViews.IdNameDate.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    private List<ProjectHistory> projectHistories;
}

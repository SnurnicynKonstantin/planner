package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "history")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectHistory extends BaseEntity {
    @NotNull
    @Column(name = "project_name")
    private String projectName;

    @NotNull
    @Column(name = "work_start_date")
    private LocalDate workStartDate;

    @NotNull
    @Column(name = "work_end_date")
    private LocalDate workEndDate;

    @Column(name = "comment")
    private String comment;

    @JsonBackReference("user-projectHistory")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference("project-projectHistory")
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
}

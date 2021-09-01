package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xrm.extrim.planner.service.TaskVisitor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "task_type_code", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Task extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "is_complete")
    private boolean isComplete;

    @Column(name = "set_complete_date")
    private LocalDate setCompleteDate;

    @Column(name = "create_date")
    private LocalDate createDate;

    @JsonBackReference("parent")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Task parentTask;

    @JsonManagedReference("parent")
    @OneToMany(mappedBy = "parentTask",  cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @JsonBackReference("user-task")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference("user-task-setter")
    @ManyToOne
    @JoinColumn(name = "user_setter_id")
    private User userSetter;

    @Column(name = "data_change")
    private LocalDate dateChange;

    @JsonBackReference("user-task-change")
    @ManyToOne
    @JoinColumn(name = "user_change_id")
    private User userChange;

    @NotNull
    @Column(name = "title")
    private String title;

    @Column(name = "actual")
    private boolean isActual;

    public abstract Task increment(TaskVisitor visitor);
}

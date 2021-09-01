package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "feedback")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Feedback extends BaseEntity {
    @Column(name = "creator_id")
    private Long creatorId;

    @NotNull
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "is_archived")
    private Boolean isArchived;
}

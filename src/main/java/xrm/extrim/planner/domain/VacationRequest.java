package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "vacation_request")
@Data
@EqualsAndHashCode(callSuper = true)
public class VacationRequest extends BaseEntity {
    @NotNull
    @Column(name = "date_from")
    private LocalDate dateFrom;

    @NotNull
    @Column(name = "date_to")
    private LocalDate dateTo;

    @NotNull
    @Column(name = "creator_id")
    private Long creatorId;

    @NotNull
    @Column(name = "is_draft")
    private Boolean isDraft;
}

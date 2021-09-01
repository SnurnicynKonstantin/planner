package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vacation_approver")
@Data
@EqualsAndHashCode(callSuper = true)
public class VacationApprover extends BaseEntity {
    @NotNull
    @Column(name = "request_id")
    private Long requestId;

    @NotNull
    @Column(name = "approver_id")
    private Long approverId;

    @NotNull
    @Column(name = "is_approved")
    private boolean isApproved;
}

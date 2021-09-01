package xrm.extrim.planner.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "vacation_mail")
public class MailVacationApprover extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "vacation_request_id")
    private VacationRequest request;

    @ManyToOne
    @JoinColumn(name = "vacation_user_id")
    private User approver;
}

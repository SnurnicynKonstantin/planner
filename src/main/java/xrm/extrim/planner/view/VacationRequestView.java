package xrm.extrim.planner.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vacation_request_v")
public class VacationRequestView {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column(name = "date_from")
    private LocalDate dateFrom;
}

package xrm.extrim.planner.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vacation_v")
public class VacationView {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "vacation_id")
    private Long vacationId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Column(name = "approved")
    private boolean approved;
}

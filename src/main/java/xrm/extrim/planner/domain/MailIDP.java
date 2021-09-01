package xrm.extrim.planner.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "mail_idp")
@Data
@EqualsAndHashCode(callSuper = true)
public class MailIDP extends BaseEntity {

    @Column(name = "IDP_date")
    private LocalDate idpDate;

    @Column(name = "user_id")
    private Long userId;


}

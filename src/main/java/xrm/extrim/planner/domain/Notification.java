package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Notification extends BaseEntity {
    @NotNull
    @Column(name = "text")
    private String text;

    @Column(name = "is_checked")
    private boolean isChecked;

    @Column(name = "recipient_id")
    private Long recipientId;

    @Column(name = "creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    public Notification(Long recipientId, String text, LocalDateTime date) {
        this.text = text;
        this.recipientId = recipientId;
        this.createDate = date;
    }
}

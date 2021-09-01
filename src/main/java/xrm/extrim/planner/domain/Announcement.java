package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "announcement")
@NoArgsConstructor
public class Announcement extends BaseEntity {

    @NotNull
    @Column(name = "creator_id")
    private Long creatorId;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @Column(name = "is_active")
    private Boolean isActive;
}

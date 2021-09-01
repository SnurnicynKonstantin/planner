package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "category_subscriber")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategorySubscriber extends BaseEntity{
    @NotNull
    @OneToOne
    @JoinColumn(name = "subscriber")
    private User subscriber;

    @NotNull
    @OneToOne
    @JoinColumn(name = "request_category")
    private RequestCategory requestCategory;
}

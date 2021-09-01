package xrm.extrim.planner.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xrm.extrim.planner.service.TaskVisitor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("2")
public class IncrementedTask extends Task{
    @NotNull
    @Column(name = "counter")
    private Integer counter = 1;

    @Override
    public Task increment(TaskVisitor visitor) {
        return visitor.incrementIncrementedTask(this);
    }
}

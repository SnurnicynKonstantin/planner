package xrm.extrim.planner.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xrm.extrim.planner.service.TaskVisitor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("1")
public class SimpleTask extends Task{
    @Override
    public Task increment(TaskVisitor visitor) {
        return visitor.incrementSimpleTask(this);
    }
}

package xrm.extrim.planner.service;

import xrm.extrim.planner.domain.IncrementedTask;
import xrm.extrim.planner.domain.SimpleTask;
import xrm.extrim.planner.domain.Task;

public interface TaskVisitor {
    Task incrementIncrementedTask(IncrementedTask task);
    Task incrementSimpleTask(SimpleTask task);
}

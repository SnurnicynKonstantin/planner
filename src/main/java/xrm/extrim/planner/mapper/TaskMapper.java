package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.TaskDto;
import xrm.extrim.planner.domain.IncrementedTask;
import xrm.extrim.planner.domain.SimpleTask;
import xrm.extrim.planner.domain.Task;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.TaskType;
import xrm.extrim.planner.service.TaskService;
import xrm.extrim.planner.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserAuthenticationHelper.class)
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod"})
public abstract class TaskMapper {
    @Autowired
    protected UserService userService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected UserMapper userMapper;

    public Task taskDtoToTask(TaskDto taskDto) {
        if ( taskDto == null ) {
            return null;
        }

        Task task = taskDto.getTaskType() == TaskType.INCREMENTED ? new IncrementedTask() : new SimpleTask();
        task.setId(taskDto.getId());
        if(taskDto.getParentTaskId() != null) {
            task.setParentTask(taskService.getTaskById(taskDto.getParentTaskId()));
        }
        task.setTitle(taskDto.getTitle());
        task.setDescription( taskDto.getDescription());
        User userSetter = userService.getUser(UserAuthenticationHelper.getAuthenticatedUserData().getId());

        task.setUser(userService.getUser(taskDto.getUserId()));
        task.setActual(taskDto.getIsActual());
        task.setUserSetter( userSetter );
        return task;
    }

    @Mappings({
            @Mapping(target = "parentTaskId", source = "task.parentTask.id"),
            @Mapping(target = "completeDate", source = "setCompleteDate"),
            @Mapping(target = "userId", source = "task.user.id"),
            @Mapping(target = "isActual", source = "actual"),
            @Mapping(target = "userChange", expression = "java(userMapper.userToUserDto(task.getUserChange()))")
    })
    public abstract TaskDto taskToTaskDto(Task task);

    public abstract List<TaskDto> listTaskToListTaskDto(List<Task> tasks);
    public abstract List<Task> listTaskDtoToListTask(List<TaskDto> tasks);

}

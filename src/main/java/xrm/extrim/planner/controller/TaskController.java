package xrm.extrim.planner.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import xrm.extrim.planner.controller.dto.TaskDto;
import xrm.extrim.planner.domain.Task;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.mapper.TaskMapper;
import xrm.extrim.planner.service.TaskService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/task")
public class TaskController {
    private final static String TASK_ID = "Task id";
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final static String TASK_OPERATIONS = "hasRole('tasksOperations')";

    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get task by id", response = Task.class)
    public TaskDto getTaskById(@ApiParam(TASK_ID) @PathVariable(name = "id") Long id) {
        return taskMapper.taskToTaskDto(taskService.getTaskById(id));
    }

    @GetMapping
    @ApiOperation(value = "Get all tasks", response = Task.class, responseContainer = "list")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PreAuthorize(TASK_OPERATIONS)
    @PostMapping
    @ApiOperation(value = "Create new task", response = Task.class)
    public TaskDto createTask(@ApiParam("Data for create task") @RequestBody TaskDto taskDto) {
        return taskMapper.taskToTaskDto(taskService.createTask(taskMapper.taskDtoToTask(taskDto)));
    }

    @PreAuthorize(TASK_OPERATIONS)
    @PutMapping("complete/{id}")
    @ApiOperation(value = "Set complete task", response = Task.class)
    public TaskDto setCompleteTask(@ApiParam(TASK_ID) @PathVariable(name = "id") Long id) {
        return taskMapper.taskToTaskDto(taskService.setCompleteTask(id));
    }

    @PreAuthorize(TASK_OPERATIONS)
    @PutMapping("open/{id}")
    @ApiOperation(value = "Set open task", response = Task.class)
    public TaskDto setOpenTask(@ApiParam(TASK_ID) @PathVariable("id") Long id) {
        return taskMapper.taskToTaskDto(taskService.setOpenTask(id));
    }

    @PreAuthorize(TASK_OPERATIONS)
    @PutMapping("{id}")
    @ApiOperation(value = "Update task", response = Task.class)
    public TaskDto updateTask(@ApiParam(TASK_ID) @PathVariable(name = "id") Long id,
                                  @ApiParam("Data with new title and description") @RequestBody TaskDto taskDto) {
        return taskMapper.taskToTaskDto(taskService.updateTask(id, taskDto));
    }

    @PreAuthorize("hasRole('watchOthersTasks')")
    @GetMapping("user/{id}")
    @ApiOperation(value = "Get all user's tasks", response = Task.class, responseContainer = "list")
    public List<TaskDto> getAllTasksByUser(@ApiParam("User id") @PathVariable(name = "id") Long id) {
        return taskMapper.listTaskToListTaskDto(taskService.getAllTasksByUser(id));
    }

    @GetMapping("user")
    @ApiOperation(value = "Get all user's own tasks", response = Task.class, responseContainer = "list")
    public List<TaskDto> getAllUserTask(@ApiIgnore @AuthenticationPrincipal User user) {
        return taskMapper.listTaskToListTaskDto(taskService.getAllTasksByUser(user.getId()));
    }

    @PreAuthorize(TASK_OPERATIONS)
    @DeleteMapping("{id}")
    @ApiOperation("Delete task")
    public void deleteTask(@ApiParam(TASK_ID) @PathVariable(name = "id") Long id) {
        taskService.deleteTask(id);
    }

    @PreAuthorize("hasRole('taskOperations')")
    @PostMapping("{id}/increment")
    @ApiOperation("Increment counter task")
    public TaskDto incrementTask(@ApiParam(TASK_ID) @PathVariable(name = "id") Long id) {
        return taskMapper.taskToTaskDto(taskService.incrementCounter(id));
    }

    @PutMapping("{id}/change-actual")
    public Task changeTaskActual(@PathVariable(name = "id") Long id) {
        return taskService.changeTaskActual(id);
    }

}

package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.domain.IncrementedTask;
import xrm.extrim.planner.domain.SimpleTask;
import xrm.extrim.planner.domain.Task;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.controller.dto.TaskDto;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService implements TaskVisitor {
    private final TaskRepository taskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {

        if (task.getParentTask()!= null && task.getParentTask().getParentTask() != null) {
            throw new PlannerException(Exception.SUB_TASK_NOT_SUPPORTED.getDescription());
        }

        task.setCreateDate(LocalDate.now());
        task.setDateChange(LocalDate.now());
        task.setUserSetter(UserAuthenticationHelper.getAuthenticatedUserData());
        task.setUserChange(UserAuthenticationHelper.getAuthenticatedUserData());
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null) {
            throw new EntityNotFoundException(String.format(Exception.TASK_NOT_FOUND.getDescription(), id));
        }
        return task;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAllByParentTaskIsNull();
    }

    public Task setCompleteTask(Long id) {
        Task task = getTaskById(id);
        if(!task.getSubTasks().isEmpty() && !task.getSubTasks().stream().allMatch(Task::isComplete)) {
            throw new PlannerException(String.format(Exception.SUBTASK_NOT_COMPLETE.getDescription(), id));
        }
        User userChanger = UserAuthenticationHelper.getAuthenticatedUserData();
        task.setUserChange(userChanger);
        task.setDateChange(LocalDate.now());
        task.setComplete(true);
        task.setSetCompleteDate(LocalDate.now());
        taskRepository.save(task);
        return task;
    }

    public Task setOpenTask(Long id) {
        Task task = getTaskById(id);
        if(task.getParentTask()!= null) {
           setOpenTask(task.getParentTask().getId());
        }
        task.setComplete(false);
        task.setSetCompleteDate(null);
        User userChanger = UserAuthenticationHelper.getAuthenticatedUserData();
        task.setUserChange(userChanger);
        task.setDateChange(LocalDate.now());
        taskRepository.save(task);
        return task;
    }

    public Task updateTask(Long taskId, TaskDto taskDto) {
        User userChanger = UserAuthenticationHelper.getAuthenticatedUserData();
        Task task = getTaskById(taskId);
        task.setUserChange(userChanger);
        task.setDateChange(LocalDate.now());
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        if(taskDto.getIsActual() != null) {
            task.setActual(taskDto.getIsActual());
        }
        taskRepository.save(task);
        return task;
    }

    public List<Task> getAllTasksByUser(Long userId) {
        return taskRepository.findAllByUser_IdAndParentTaskIsNull(userId);
    }

    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    public Task incrementCounter(Long taskId) {
        Task task = getTaskById(taskId);
        task.increment(this);
        return taskRepository.save(task);
    }

    @Override
    public Task incrementIncrementedTask(IncrementedTask task) {
        task.setCounter(task.getCounter() + 1);
        return taskRepository.save(task);
    }

    @Override
    public Task incrementSimpleTask(SimpleTask task) {
        throw new PlannerException(String.format(Exception.TASK_TYPE_NOT_INCREMENTED.getDescription(), task.getId()));
    }

    public Task changeTaskActual(Long taskId) {
        Task task = getTaskById(taskId);
        task.setActual(!task.isActual());
        return taskRepository.save(task);
    }
}

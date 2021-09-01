package xrm.extrim.planner.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import xrm.extrim.planner.enums.TaskType;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private boolean complete;
    private TaskType taskType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate completeDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateChange;
    private UserDto userChange;
    private Long parentTaskId;
    private List<TaskDto> subTasks;
    private Boolean isActual;
}

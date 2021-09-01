package xrm.extrim.planner.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ProjectHistoryDto {
    private Long id;
    private String projectName;
    private Long userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workEndDate;
    private String comment;
    private Long projectId;
}

package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDto {
    private Long id;
    private Long creatorId;
    private String creatorName;
    private LocalDate creationDate;
    private String text;
    private Boolean isArchived;
}

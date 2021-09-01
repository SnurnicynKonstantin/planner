package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationDto {
    private Long id;
    private Long requestId;
    private Long creatorId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Boolean isDraft;
    private List<Long> approversIds;
    private List<String> approversNames = new ArrayList<>();
    private List<Boolean> approversMarks = new ArrayList<>();
}

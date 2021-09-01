package xrm.extrim.planner.controller.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilterScopeUserDto {
    private String initials;
    private List<UserSkillDto> skills;
    private String position;
    private String department;
    private Boolean isOnVacation;
    private String orderBy;
    private Sort.Direction direction;
    private LocalDate startMoment;
    private LocalDate endMoment;
    private String personalInformation;
}

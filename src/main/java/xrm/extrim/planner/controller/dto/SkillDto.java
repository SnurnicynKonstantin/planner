package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDto {
    private Long id;
    private String name;
    private Long groupId;
    private String description;
    private List<RateDescriptionDto> rateDescriptions;
}

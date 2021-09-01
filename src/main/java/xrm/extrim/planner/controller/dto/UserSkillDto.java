package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillDto {
    private Long id;
    private Integer rate;
    private String name;
    private String description;
    private List<RateDescriptionDto> rateDescriptions;
    private Boolean confirmed;
    private Integer version;
    private String comment;
}

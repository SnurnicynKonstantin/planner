package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementDto {
    private Long id;
    private Long creatorId;
    private String text;
    private Boolean isActive;
    private String creatorName;
}

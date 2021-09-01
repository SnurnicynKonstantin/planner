package xrm.extrim.planner.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private boolean skillOperations;
    private boolean setSkillToUser;
    private boolean rateSkill;
    private boolean watchOthersTasks;
    private boolean tasksOperations;
    private boolean editOtherUser;
    private boolean importSkills;
    private boolean departmentOperations;
    private boolean positionOperations;
    private boolean requestOperations;
    private boolean announcementOperations;
}

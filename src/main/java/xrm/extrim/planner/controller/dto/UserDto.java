package xrm.extrim.planner.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String name;
    private LocalDate idpDate;
    private String surname;
    private String login;
    private ContactDto contact;
    private String personalInformation;
    private RoleDto role;
    private UserDto manager;
    private DirectoryDto position;
    private DirectoryDto department;
    private boolean teamLead;
    private Boolean isOnVacation;
    private LocalDate birthday;
}

package xrm.extrim.planner.view;

import lombok.Data;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.Role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "user_v")
public class UserView {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_surname")
    private String userSurname;

    @Column(name = "user_personal_information")
    private String userPersonalInformation;

    @Column(name = "is_on_vacation")
    private Boolean isOnVacation;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "position_description")
    private String positionDescription;

    @Column(name = "department_description")
    private String departmentDescription;

    @Column(name = "user_login")
    private String userLogin;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}

package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import xrm.extrim.planner.view.RoleViews;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Role extends BaseEntity implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 5157600800250401721L;
    @Column(name = "name")
    private String name;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "skill_operations")
    private boolean skillOperations;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "set_skill_to_user")
    private boolean setSkillToUser;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "rate_skill")
    private boolean rateSkill;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "watch_others_tasks")
    private boolean watchOthersTasks;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "tasks_operations")
    private boolean tasksOperations;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "edit_other_user")
    private boolean editOtherUser;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "import_skills")
    private boolean importSkills;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "department_operations")
    private boolean departmentOperations;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "position_operations")
    private boolean positionOperations;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "request_operations")
    private boolean requestOperations;

    @JsonView(RoleViews.Permissions.class)
    @Column(name = "announcement_operations")
    private boolean announcementOperations;

    public Collection<GrantedAuthority> getRoleAuthorities(){
        return  Arrays.stream(Role.class.getDeclaredFields())
                .filter(field ->  field.getType().equals(boolean.class))
                .filter(field -> {
                    try {
                        return (boolean) field.get(this);
                    } catch (IllegalAccessException ignored) {
                        log.warn("field in role class not bool");
                    }
                    return false;
                })
                .map(field -> new SimpleGrantedAuthority(field.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}

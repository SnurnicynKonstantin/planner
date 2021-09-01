package xrm.extrim.planner.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@EqualsAndHashCode(of = {"id"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({"PMD.TooManyFields"})
public class User implements LdapUserDetails {
    private final static String USER_STRING = "user";
    private static final long serialVersionUID = 6494070368919380949L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq")
    @GenericGenerator(name = "seq", strategy = "increment")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(name = "surname")
    private String surname;

    @NotNull
    @Column(name = "login")
    private String login;

    @Column(name = "team_lead")
    private boolean teamLead;

    @Column(name = "idp_date")
    private LocalDate idpDate;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "personal_information")
    private String personalInformation;

    @JsonManagedReference("user-contact")
    @OneToOne(mappedBy = USER_STRING, cascade = CascadeType.ALL)
    private Contact contact;

    @JsonManagedReference("user-userSkills")
    @OneToMany(mappedBy = USER_STRING, cascade = CascadeType.MERGE)
    private List<UserSkill> userSkills;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @JsonBackReference("user-task")
    @OneToMany(mappedBy = USER_STRING, cascade = CascadeType.ALL)
    List<Task> tasks;

    @JsonBackReference("user-task-setter")
    @OneToMany(mappedBy = "userSetter", cascade = CascadeType.ALL)
    private List<Task> taskSetted;

    @JsonBackReference("user-task-change")
    @OneToMany(mappedBy = "userChange", cascade = CascadeType.ALL)
    private List<Task> tasksChanged;

    @JsonManagedReference("user-manager")
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @JsonBackReference("user-manager")
    @OneToMany(mappedBy = "manager")
    private List<User> subordinates;

    @JsonManagedReference("user-projectHistory")
    @OneToMany(mappedBy = USER_STRING)
    private List<ProjectHistory> projectHistory;

    @ManyToMany(mappedBy = "attachedUsers")
    private List<File> attachedFiles;

    @OneToMany(mappedBy = "uploader")
    private List<File> uploadedFiles;

    @JsonManagedReference("user-position")
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @JsonManagedReference("user-department")
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public User(String name, String surname, String login, Position position,Department department,Role role) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.role = role;
        this.position = position;
        this.department = department;
    }

    @Override
    public String getDn() {
        return null;
    }

    @Override
    public void eraseCredentials() {

    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        authorities.addAll(role.getRoleAuthorities());
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

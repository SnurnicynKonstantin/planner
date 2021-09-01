package xrm.extrim.planner.configuration;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import xrm.extrim.planner.domain.Contact;
import xrm.extrim.planner.domain.Messengers;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.ContactRepository;
import xrm.extrim.planner.repository.DepartmentRepository;
import xrm.extrim.planner.repository.PositionRepository;
import xrm.extrim.planner.repository.RoleRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.service.UserMigrateService;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.Collection;

@Slf4j
@Configuration
@SuppressWarnings({"PMD.GuardLogStatement"})
public class CustomUserDetailsContextMapper extends LdapUserDetailsMapper implements UserDetailsContextMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMigrateService userMigrateService;

    private static final int NAME = 0;
    private static final int SURNAME = 1;
    private static final String MAIL = "mail";
    private static final String COMMON_NAME = "cn";
    private static final String POSITION = "UNDEFINED";
    private static final String USER_ROLE = "USER";
    private static final String DEPARTMENT = "UNDEFINED";


    @Override
    public LdapUserDetails mapUserFromContext(DirContextOperations ctx, String login, Collection<? extends GrantedAuthority> authorities) {
        Attributes attributes = ctx.getAttributes();

        String[] userNameSurname;
        String mail;

        try {
            userNameSurname = attributes.get(COMMON_NAME).get().toString().split(" ");
            mail = attributes.get(MAIL).get().toString();
        } catch (NamingException e) {
            throw new PlannerException(e, Exception.ERROR_WHILE_PARSING_ATTRIBUTE.getDescription());
        }

        User user = userRepository.findByLogin(login);

        if (user == null) {
            user = createUserWithInfo(userNameSurname[NAME], userNameSurname[SURNAME], login, mail);
        }

        return user;
    }

    private User createUserWithInfo(String name, String surname, String login, String mail) {
        User user = userRepository.save(
                new User(
                        name,
                        surname,
                        login,
                        positionRepository.getByName(POSITION),
                        departmentRepository.findByName(DEPARTMENT),
                        roleRepository.getByName(USER_ROLE)
                )
        );
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setEmail(mail);
        contact.setMessengers(new Messengers());
        contactRepository.save(contact);
        user.setContact(contact);

        try {
            userMigrateService.updateUserFromConfluence(user);
        } catch (JSONException e) {
           log.error("cant connect to confluence" + e.getMessage());
        }
        return user;
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        // default
    }
}

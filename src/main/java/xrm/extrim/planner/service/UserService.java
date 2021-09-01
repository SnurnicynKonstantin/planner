package xrm.extrim.planner.service;

import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.Gravatar;
import xrm.extrim.planner.common.NotificationMessages;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.FilterScopeUserDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Contact;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.Role;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.ContactMapper;
import xrm.extrim.planner.repository.ContactRepository;
import xrm.extrim.planner.repository.DepartmentRepository;
import xrm.extrim.planner.repository.PositionRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.repository.UserSkillsRepository;
import xrm.extrim.planner.repository.UserViewRepository;
import xrm.extrim.planner.service.predicate.UserFilter;
import xrm.extrim.planner.view.UserView;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class UserService {
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final UserSkillsRepository userSkillsRepository;
    private final ContactMapper contactMapper;
    private final NotificationService notificationService;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleService roleService;
    private final UserViewRepository userViewRepository;

    @Autowired
    public UserService(UserRepository userRepository, ContactRepository contactRepository,
                       UserSkillsRepository userSkillsRepository, ContactMapper contactMapper,
                       NotificationService notificationService, PositionRepository positionRepository,
                       DepartmentRepository departmentRepository, RoleService roleService, UserViewRepository userViewRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.userSkillsRepository = userSkillsRepository;
        this.contactMapper = contactMapper;
        this.notificationService = notificationService;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.roleService = roleService;
        this.userViewRepository = userViewRepository;
    }


    private User getUserWithLatestVersionsSkills(User user) {
        List<UserSkill> userSkills = userSkillsRepository.findLatestVersionsIdsByUserId(user.getId()).stream()
                .map(id -> userSkillsRepository.findById(id).orElse(null))
                .filter(userSkill -> userSkill != null && !userSkill.isDeleted())
                .collect(Collectors.toList());
        user.setUserSkills(userSkills);
        return user;
    }

    public String getAvatarURL(Long id, int size) {
        User user = getUser(id);
        return Gravatar.getAvatarURLFromMail(user.getContact().getEmail(), size);
    }

    public User getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), id));
        }

        return getUserWithLatestVersionsSkills(user);
    }

    public User getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), login));
        }
        return getUserWithLatestVersionsSkills(user);
    }

    public boolean checkIfUserExistByLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new PlannerException(String.format(Exception.USER_NOT_FOUND.getDescription(), login));
        }
        return userRepository.existsByLogin(login);
    }

    public List<User> getUsers() {
        return userRepository.findAll().stream().map(this::getUserWithLatestVersionsSkills).collect(Collectors.toList());
    }

    public List<UserView> getUsersView() {
        return userViewRepository.findAll();
    }

    // TODO: поторопились и вмержили некорректный код. Нужно откатить как здесь с сохранением логики контактов
    public User createUser(User user) {
        User userDb = userRepository.save(user);
        Contact contact = new Contact();
        contact.setUser(userDb);
        contactRepository.save(contact);
        return getUserWithLatestVersionsSkills(userDb);
    }

    public User update(Long id, User user) {
        user.setId(id);
        User userFromDb = getUser(id);
        User manager = user.getManager();
        if (userFromDb.getManager() != manager) {
            checkPossibilitySetManager(user, manager);
        }
        user.getContact().setId(userFromDb.getContact().getId());
        user.getContact().setUser(user);
        userFromDb = userRepository.save(user);
        sendProfileInfoUpdatedNotification(id, NotificationMessages.WHOLE_USER_DATA_UPDATE.getMessage());
        return getUserWithLatestVersionsSkills(userFromDb);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Contact updateContacts(Contact userContact, Contact contact) {
        contactMapper.updateContact(userContact, contact);
        userContact.getUser().setContact(userContact);
        sendProfileInfoUpdatedNotification(userContact.getUser().getId(), NotificationMessages.CONTACT_UPDATE.getMessage());
        return contactRepository.save(userContact);
    }

    public List<User> filterUsers(String name, String surname, String login) {
        return userRepository.findAllFilteredUser(name, surname, login)
                .stream().map(this::getUserWithLatestVersionsSkills).collect(Collectors.toList());
    }

    public List<User> filterBySkills(List<Long> skills) {
        return userRepository.filter(skills).stream().map(this::getUserWithLatestVersionsSkills).collect(Collectors.toList());
    }

    public List<User> findUsersByDaysToIDP(int daysToIDP) {
        LocalDate currentDate = LocalDate.now();
        LocalDate idpDate = currentDate.plusDays(daysToIDP);
        return userRepository.findAllByIdpDateBetween(currentDate, idpDate)
                .stream().map(this::getUserWithLatestVersionsSkills).collect(Collectors.toList());
    }

    public void setRole(Long id, String roleName) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), id));
        }

        Role role = roleService.getByName(roleName);
        if (role == null) {
            throw new EntityNotFoundException(String.format(Exception.ROLE_NOT_FOUND.getDescription(), roleName));
        }

        user.setRole(role);
        sendProfileInfoUpdatedNotification(id, String.format(NotificationMessages.ROLE_UPDATE.getMessage(), role.getName()));
        userRepository.save(user);
    }

    public void setPosition(Long id, String name) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), id));
        }

        Position position = positionRepository.findByName(name);
        if (position == null) {
            throw new EntityNotFoundException(String.format(Exception.POSITION_NOT_FOUND.getDescription(), name));
        }

        user.setPosition(position);
        userRepository.save(user);
        sendProfileInfoUpdatedNotification(id, String.format(NotificationMessages.POSITION_UPDATE.getMessage(), position.getName()));
    }

    public void setDepartment(Long id, String name) {
        Department department = departmentRepository.findByName(name);
        if (department == null) {
            throw new EntityNotFoundException(String.format(Exception.DEPARTMENT_NOT_FOUND.getDescription(), name));
        }
        User user = getUser(id);
        user.setDepartment(department);
        userRepository.save(user);
        sendProfileInfoUpdatedNotification(id, String.format(NotificationMessages.DEPARTMENT_UPDATED.getMessage(), department.getName()));
    }

    public User setManager(Long userId, Long managerId) {
        User user = getUser(userId);
        User manager = getUser(managerId);
        checkPossibilitySetManager(user, manager);
        user.setManager(manager);
        user = userRepository.save(user);
        return getUserWithLatestVersionsSkills(user);
    }

    private void checkPossibilitySetManager(User user, User manager) {
        Set<User> managers = getAllBranchManagers(manager);
        if (managers.contains(user)) {
            throw new PlannerException(Exception.INFINITY_LOOP_MANAGERS.getDescription());
        }
        if (user.equals(manager)) {
            throw new PlannerException(Exception.HIMSELF_MANAGER.getDescription());
        }
    }

    private Set<User> getAllBranchManagers(User manager) {
        Set<User> managers = new HashSet<>();
        User currentManager = manager;
        while (currentManager != null) {
            managers.add(currentManager);
            if (currentManager.getManager() == null) {
                break;
            }
            currentManager = userRepository.findById(currentManager.getManager().getId()).orElse(null);
        }
        return managers;
    }

    public User removeManager(Long userId) {
        User user = getUser(userId);
        user.setManager(null);
        user = userRepository.save(user);
        return getUserWithLatestVersionsSkills(user);
    }

    public Page<UserView> filterUserScope(FilterScopeUserDto filterDto, int page, int pageSize) {
        Predicate predicate = UserFilter.getUserFilterPredicate(filterDto);
        Pageable pageable = PageRequest.of(page, pageSize, filterDto.getDirection(), filterDto.getOrderBy());
        return userViewRepository.findAllByPredicate(predicate, pageable);
    }

    public List<UserView> filterUserByVacationDate(FilterScopeUserDto filterDto) {
        Predicate predicate = UserFilter.getUserFilterByVacation(filterDto.getStartMoment(), filterDto.getEndMoment());
        return userViewRepository.findAllByPredicate(predicate);
    }

    public List<User> validateUsers(List<UserDto> userDtos) {
        List<String> usersLogin = userDtos.stream()
                .map(UserDto::getLogin)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return userRepository.findAllByLoginIn(usersLogin);
    }

    private void sendProfileInfoUpdatedNotification(Long recipientId, String text){
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        StringBuilder notificationText = new StringBuilder(text);
        if(!recipientId.equals(currentUser.getId())){
            notificationText.append(String.format(NotificationMessages.YOUR_PROFILE_WAS_CHANGED_BY.getMessage(), currentUser.getName(), currentUser.getSurname()));
        }
        notificationService.sendNotification(recipientId, notificationText.toString());
    }

    public void editPersonalInformation(Long userId, String personalInformation) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new PlannerException(String.format(Exception.USER_NOT_FOUND.getDescription(), userId));
        }
        user.setPersonalInformation(personalInformation);
        userRepository.save(user);
    }

    public Boolean inVacation(Long userId) {
        return userViewRepository.getById(userId).getIsOnVacation();
    }
}

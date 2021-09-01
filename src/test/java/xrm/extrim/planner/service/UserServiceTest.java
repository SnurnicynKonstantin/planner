package xrm.extrim.planner.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.common.ContactTestData;
import xrm.extrim.planner.common.SkillTestData;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.common.UserTestData;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.BaseEntity;
import xrm.extrim.planner.domain.Contact;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.ContactMapper;
import xrm.extrim.planner.markers.UnitTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class UserServiceTest extends PlannerTestBase {
    @Before
    public void setUp() {
        Position position = UserTestData.getUserPosition();
        position.setId(1L);

        when(positionRepository.findByName(any())).thenReturn(position);
        when(positionRepository.findById(any())).thenReturn(Optional.of(position));
    }

    @Test
    public void testCreateUserOk() {
        mockDepartmentAndPosition();
        User userFromDto = userMapper.userDtoToUser(UserTestData.getUserDto());
        Contact contactFromDto = Mappers.getMapper(ContactMapper.class).contactDtoToContact(ContactTestData.getContactDto1());
        Mockito.when(userRepositoryMock.save(any(User.class))).thenReturn(userFromDto);
        Mockito.when(contactRepositoryMock.save(any(Contact.class))).thenReturn(contactFromDto);
        User savedUser = userService.createUser(userFromDto);
        assertEquals(userFromDto, savedUser);
    }

    @Test
    public void testGetUserOk() {
        mockDepartmentAndPosition();
        User userFromDto = userMapper.userDtoToUser(UserTestData.getUserDto());
        when(userRepositoryMock.findById(userFromDto.getId())).thenReturn(Optional.of(userFromDto));
        User gottenUser = userService.getUser(userFromDto.getId());
        assertEquals(userFromDto, gottenUser);
    }

    @Test
    public void testGetUsersOk() {
        mockDepartmentAndPosition();
        List<UserDto> userSetDtos = UserTestData.getUserDroList();
        List<User> usersFromDto = new ArrayList<>();
        userSetDtos.forEach(userSetDto -> usersFromDto.add(userMapper.userDtoToUser(userSetDto)));
        when(userRepositoryMock.findAll()).thenReturn(usersFromDto);
        List<User> usersFromMock = userService.getUsers();
        assertEquals(usersFromDto, usersFromMock);
    }

    @Test
    public void testDeleteOk() {
        mockDepartmentAndPosition();
        User userFromDto = userMapper.userDtoToUser(UserTestData.getUserDto());
        when(userRepositoryMock.save(any(User.class))).thenReturn(userFromDto);
        User savedUser = userService.createUser(userFromDto);
        userService.delete(savedUser.getId());
        verify(userRepositoryMock, times(1)).deleteById(savedUser.getId());
    }

    @Test
    public void testUpdateContactsOk() {
        mockDepartmentAndPosition();
        User userFromDto = userMapper.userDtoToUser(UserTestData.getUserDto());
        Contact contactFromDto = Mappers.getMapper(ContactMapper.class).contactDtoToContact(ContactTestData.getContactDto1());
        userFromDto.setContact(contactFromDto);
        contactFromDto.setUser(userFromDto);
        when(contactRepositoryMock.save(any(Contact.class))).thenReturn(contactFromDto);
        when(userRepositoryMock.findById(userFromDto.getId())).thenReturn(Optional.of(userFromDto));

        try(MockedStatic<UserAuthenticationHelper> mocked = mockStatic(UserAuthenticationHelper.class)) {
            mocked.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(userFromDto);

            Contact updatedContact = userService.updateContacts(userFromDto.getContact(), contactFromDto);
            assertEquals(contactFromDto, updatedContact);
        }
    }

    @Test
    public void testFilterUsersOk() {
        mockDepartmentAndPosition();
        User userFromDto = userMapper.userDtoToUser(UserTestData.getUserDto());
        List<User> usersFromDto = new ArrayList<>();
        usersFromDto.add(userFromDto);
        when(userRepositoryMock.findAllFilteredUser(userFromDto.getName(), userFromDto.getSurname(), userFromDto.getLogin())).thenReturn(usersFromDto);
        List<User> usersFromMock = userService.filterUsers(userFromDto.getName(), userFromDto.getSurname(), userFromDto.getLogin());
        assertEquals(usersFromDto, usersFromMock);
    }

    @Test
    public void testFilterBySkillsOk() {
        mockDepartmentAndPosition();
        List<UserDto> userSetDtos = UserTestData.getUserDroList();
        List<User> usersFromDto = new ArrayList<>();
        userSetDtos.forEach(userSetDto -> usersFromDto.add(userMapper.userDtoToUser(userSetDto)));
        List<Skill> skills = SkillTestData.getSkills();
        List<Long> skillsIds = skills.stream().map(BaseEntity::getId).collect(Collectors.toList());
        when(userRepositoryMock.filter(skillsIds)).thenReturn(usersFromDto);
        List<User> filteredUsers = userService.filterBySkills(skillsIds);
        assertEquals(usersFromDto, filteredUsers);
    }

    @Test
    public void testSetManagerOk() {
        Pair<User, User> userAndManager = createUserAndManager();
        User user = userAndManager.getFirst();
        User manager = userAndManager.getSecond();

        User userFromService = userService.setManager(user.getId(), manager.getId());

        assertNotNull(userFromService.getManager());
        assertEquals(userFromService.getManager(), manager);
    }

    @Test
    public void testSetManagerLoopManagersWithTwoUserWrong() {
        Pair<User, User> userAndManager = createUserAndManager();
        User user = userAndManager.getFirst();
        User manager = userAndManager.getSecond();

        User userFromService = userService.setManager(user.getId(), manager.getId());

        assertNotNull(userFromService.getManager());
        assertEquals(userFromService.getManager(), manager);

        assertThrows(PlannerException.class, () -> userService.setManager(manager.getId(), user.getId()));
    }

    @Test
    public void testSetManagerUserManagerIsUserHimselfWrong() {
        Pair<User, User> userAndManager = createUserAndManager();
        User user = userAndManager.getFirst();

        assertThrows(PlannerException.class, () -> userService.setManager(user.getId(), user.getId()));
    }

    @Test
    public void testSetManagerLoopManagersWithThreeUserWrong() {
        User user1 = UserTestData.getUser(1L);
        User user2 = UserTestData.getUser(2L);
        User user3 = UserTestData.getUser(3L);

        when(userRepositoryMock.findById(eq(1L))).thenReturn(Optional.of(user1));
        when(userRepositoryMock.findById(eq(2L))).thenReturn(Optional.of(user2));
        when(userRepositoryMock.findById(eq(3L))).thenReturn(Optional.of(user3));

        when(userRepositoryMock.save(eq(user1))).thenReturn(user1);
        when(userRepositoryMock.save(eq(user2))).thenReturn(user2);
        when(userRepositoryMock.save(eq(user3))).thenReturn(user3);

        User userFromService = userService.setManager(user1.getId(), user2.getId());
        assertNotNull(userFromService.getManager());
        assertEquals(userFromService.getManager(), user2);

        userFromService = userService.setManager(user2.getId(), user3.getId());
        assertNotNull(userFromService.getManager());
        assertEquals(userFromService.getManager(), user3);
        assertThrows(PlannerException.class, () -> userService.setManager(user3.getId(), user1.getId()));
    }

    private Pair createUserAndManager() {
        mockDepartmentAndPosition();
        User user = userMapper.userDtoToUser(UserTestData.getUserDto());
        user.setId(1L);
        User manager = userMapper.userDtoToUser(UserTestData.getUserDto());
        manager.setId(2L);

        when(userRepositoryMock.findById(eq(1L))).thenReturn(Optional.of(user));
        when(userRepositoryMock.findById(eq(2L))).thenReturn(Optional.of(manager));
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);
        return Pair.of(user, manager);
    }

    private void mockDepartmentAndPosition() {
        Department department = UserTestData.getUserDepartment();
        when(departmentRepository.existsByName(department.getName())).thenReturn(true);
        when(departmentRepository.findByName(department.getName())).thenReturn(department);

        Position position = UserTestData.getUserPosition();
        when(positionRepository.existsByName(position.getName())).thenReturn(true);
        when(positionRepository.findByName(position.getName())).thenReturn(position);
    }

    @Test
    public void testDeleteManager() {
        Pair<User, User> userAndManager = createUserAndManager();
        User user = userAndManager.getFirst();
        User manager = userAndManager.getSecond();
        userService.setManager(user.getId(), manager.getId());

        User userFromService = userService.removeManager(user.getId());
        assertNull(userFromService.getManager());
    }

    @Test
    public void testCreateUserWithManagerOk() {
        User user = UserTestData.getUser1();
        User manager = UserTestData.getUser2();
        Contact contactFromDto = new Contact();
        user.setManager(manager);

        when(userRepositoryMock.findById(any())).thenReturn(Optional.ofNullable(manager));
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);
        when(contactRepositoryMock.save(any(Contact.class))).thenReturn(contactFromDto);

        User savedUser = userService.createUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    public void testUpdateUserSetManagerOk() {
        User user = UserTestData.getUser1();

        User userData = UserTestData.getUser1();
        User manager = UserTestData.getUser2();
        userData.setManager(manager);
        user.setContact(new Contact());
        user.getContact().setId(3L);
        user.setId(4L);

        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenReturn(userData);

        try(MockedStatic<UserAuthenticationHelper> mocked = mockStatic(UserAuthenticationHelper.class)) {
            mocked.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            User updatedUser = userService.update(user.getId(), userData);
            assertEquals(userData, updatedUser);
        }
    }

    @Test
    public void testUpdateUserDeleteManagerOk() {
        User user = UserTestData.getUser1();
        User manager = UserTestData.getUser2();
        user.setManager(manager);
        user.setContact(new Contact());
        user.getContact().setId(3L);
        user.setId(4L);

        User userData = UserTestData.getUser1();

        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenReturn(user);

        try(MockedStatic<UserAuthenticationHelper> mocked = mockStatic(UserAuthenticationHelper.class)) {
            mocked.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            User updatedUser = userService.update(user.getId(), userData);
            assertEquals(userData, updatedUser);
        }
    }

    @Test
    public void testUpdateUserWithExistManagerOk() {
        User user = UserTestData.getUser1();
        User manager1 = UserTestData.getUser2();

        manager1.setId(1L);
        user.setManager(manager1);
        user.setContact(new Contact());
        user.getContact().setId(3L);
        user.setId(4L);

        User userData = UserTestData.getUser3();
        userData.setManager(manager1);
        userData.setContact(new Contact());

        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(user));
        when(userRepositoryMock.save(any(User.class))).thenReturn(userData);

        try(MockedStatic<UserAuthenticationHelper> mocked = mockStatic(UserAuthenticationHelper.class)) {
            mocked.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            User updatedUser = userService.update(user.getId(), userData);
            assertEquals(userData, updatedUser);
        }
    }
}

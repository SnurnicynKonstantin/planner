package xrm.extrim.planner.mappers;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.common.UserTestData;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.controller.dto.RoleDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Contact;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Role;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.markers.UnitTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class UserMapperTests extends PlannerTestBase{

    @Test
    public void userToUserDto() {
        User user = new User();
        user.setId(2L);
        user.setName("Test_name");
        user.setSurname("Test_surname");
        user.setLogin("Test_login");
        Contact contact = new Contact();
        contact.setId(3L);
        contact.setUser(user);
        contact.setPhoneNumber("123456");
        contact.setEmail("test@test.com");

        user.setContact(contact);

        Role role = new Role();
        role.setName("admin");
        user.setRole(role);

        Position position = UserTestData.getUserPosition();
        user.setPosition(position);

        ArrayList<UserSkill> userSkills = new ArrayList<>();
        userSkills.add(createUserSkill());
        user.setUserSkills(userSkills);
        UserDto userDto = userMapper.userToUserDto(user);

        assert user.getId().equals(userDto.getId());
        assert user.getName().equals(userDto.getName());
        assert user.getSurname().equals(userDto.getSurname());
        assert user.getLogin().equals(userDto.getLogin());
        assert user.getRole().getName().equals(userDto.getRole().getName());
        assert user.getPosition().getName().equals(userDto.getPosition().getName());

        Contact userContact = user.getContact();
        ContactDto userDtoContact = userDto.getContact();
        assert userContact.getEmail().equals(userDtoContact.getEmail());
        assert userContact.getPhoneNumber().equals(userDtoContact.getPhoneNumber());
    }

    private UserSkill createUserSkill() {
        UserSkill userSkill = new UserSkill();
        userSkill.setId(3L);
        userSkill.setRate(4);
        userSkill.setConfirmed(true);

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setDescription("desc");
        skill.setName("name");

        List<RateDescription> descs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            RateDescription rateDescription = new RateDescription();
            rateDescription.setId((long) i);
            rateDescription.setSkill(skill);
            rateDescription.setRateNumber(i);
            rateDescription.setDescription("desc " + i);
            descs.add(rateDescription);
        }
        skill.setRateDescriptions(descs);

        userSkill.setSkill(skill);
        return userSkill;
    }

    @Test
    public void userSetDtoToUser() {
        Position position = UserTestData.getUserPosition();
        Department department = UserTestData.getUserDepartment();

        UserDto userDto = new UserDto();
        userDto.setName("test");
        userDto.setSurname("test-1");
        userDto.setLogin("test-2");
        userDto.setPosition(UserTestData.getDirectoryDto());
        userDto.setDepartment(UserTestData.getDirectoryDto());
        userDto.setRole(new RoleDto());

        mockDepartmentAndPosition();

        User user = userMapper.userDtoToUser(userDto);

        assert user.getName().equals(userDto.getName());
        assert user.getSurname().equals(userDto.getSurname());
        assert user.getLogin().equals(userDto.getLogin());
        assert user.getPosition().equals(position);
        assert user.getDepartment().equals(department);
    }

    private void mockDepartmentAndPosition() {
        Department department = UserTestData.getUserDepartment();
        when(departmentRepository.existsByName(department.getName())).thenReturn(true);
        when(departmentRepository.findByName(department.getName())).thenReturn(department);

        Position position = UserTestData.getUserPosition();
        when(positionRepository.existsByName(position.getName())).thenReturn(true);
        when(positionRepository.findByName(position.getName())).thenReturn(position);
    }
}

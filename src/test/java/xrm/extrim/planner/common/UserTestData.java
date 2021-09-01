package xrm.extrim.planner.common;

import org.mapstruct.factory.Mappers;
import xrm.extrim.planner.controller.dto.DirectoryDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Department;
import xrm.extrim.planner.domain.Position;
import xrm.extrim.planner.domain.Role;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.mapper.UserMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class UserTestData {
    private static Position position = new Position(1L, "UNDEFINED", "description");

    private static DirectoryDto directoryDto = new DirectoryDto(1L, "UNDEFINED", "description");

    private static Department department = new Department(1L, "UNDEFINED", "description");

    private static Role roleUser = new Role();

    private static User user1 = new User("name_1",  "surname_1", "login_1", position, department, roleUser);

    private static User user2 = new User("name_2","surname_2", "login_2", position, department, roleUser);

    private static User user3 = new User("name_3","surname_3", "login_3", position, department, roleUser);

    private UserTestData() {
    }

    public static User getUser1() {
        return user1;
    }

    public static User getUser2() {
        return user2;
    }

    public static User getUser3() {
        return user3;
    }

    public static Position getUserPosition() {
        return position;
    }

    public static Department getUserDepartment() {
        return department;
    }

    public static DirectoryDto getDirectoryDto(){ return directoryDto;}

    public static User getUser() {
        return getUser(1L);
    }

    public static User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("Artemiy");
        user.setSurname("Ivanov");
        user.setLogin("Ivanovich");
        user.setPosition(position);
        user.setRole(roleUser);
        user.setDepartment(department);
        return user;
    }

    public static UserDto getUserDto() {
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        return userMapper.userToUserDto(getUser());
    }

    public static List<UserDto> getUserDroList() {
        return new ArrayList<>(Arrays.asList(getUserDto(), getUserDto(), getUserDto()));
    }
}

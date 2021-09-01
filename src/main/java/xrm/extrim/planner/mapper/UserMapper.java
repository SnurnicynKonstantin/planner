package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.controller.dto.RoleDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.service.DepartmentService;
import xrm.extrim.planner.service.PositionService;
import xrm.extrim.planner.view.UserView;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserSkillMapper.class)
public abstract class UserMapper {
    private static final String CONTACT = "contact";

    @Autowired
    protected PositionService positionService;

    @Autowired
    protected DepartmentService departmentService;

    @Mappings({
            @Mapping(target = CONTACT, source = CONTACT, resultType = ContactDto.class)
    })
    public abstract UserDto userToUserDto(User user);

    public abstract List<UserDto> userToUserDto(List<User> user);

    @Mappings({
            @Mapping(target = "role", source = "role", resultType = RoleDto.class),
            @Mapping(target = CONTACT, source = CONTACT, resultType = ContactDto.class)
    })
    public abstract User userDtoToUser(UserDto userDto);

   @Mappings({
           @Mapping(target = "name", source = "userName"),
           @Mapping(target = "surname", source = "userSurname"),
           @Mapping(target = "login", source = "userLogin"),
   })
    public abstract UserDto userViewToUserDto(UserView userView);
}

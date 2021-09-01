package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.ContactDto;
import xrm.extrim.planner.controller.dto.FilterScopeUserDto;
import xrm.extrim.planner.controller.dto.FilterUserDto;
import xrm.extrim.planner.controller.dto.FilterUserSkillsDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.enums.PageSize;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.mapper.ContactMapper;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.service.UserService;
import xrm.extrim.planner.view.UserView;

import java.util.List;
import java.util.stream.Collectors;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/user")
@Api(description = "Operations pertaining to users")
public class UserController {

    private final static String USER_ID = "User id";
    private final static String USER_ID_PATH_VARIABLE = "user_id";
    private final UserService userService;
    private final UserMapper userMapper;
    private final ContactMapper contactMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, ContactMapper contactMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.contactMapper = contactMapper;
    }

    @PostMapping("/get-avatar/{id}/{size}")
    @ApiOperation(value = "Getting user's avatar", response = User.class)
    public String getAvatarURL(@PathVariable("id") Long id, @PathVariable("size") int size) {
        return userService.getAvatarURL(id, size);
    }

    @GetMapping
    @ApiOperation(value = "Get all users", response = UserDto.class, responseContainer = "List")
    public List<UserDto> getUsers() {
        return userMapper.userToUserDto(userService.getUsers());
    }

    @GetMapping("/view")
    @ApiOperation(value = "Get all users", response = UserDto.class, responseContainer = "List")
    public List<UserDto> filterUsers() {
        List<UserView> users = userService.getUsersView();
        return users.stream().map(userMapper::userViewToUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id", response = UserDto.class)
    public UserDto getUser(@ApiParam(USER_ID) @PathVariable("id") Long id) {
        return userMapper.userToUserDto(userService.getUser(id));
    }

    @GetMapping("/login/{login}")
    @ApiOperation(value = "Get user by login", response = UserDto.class)
    public UserDto getUserByLogin(@ApiParam("User login") @PathVariable("login") String login) {
        return userMapper.userToUserDto(userService.getUserByLogin(login));
    }

    @GetMapping("/profile")
    @ApiOperation(value = "Get user own profile", response = UserDto.class)
    public UserDto getProfile(@ApiIgnore @AuthenticationPrincipal User user) {
        return userMapper.userToUserDto(userService.getUser(user.getId()));
    }

    @PreAuthorize("hasRole('editOtherUser')")
    @PutMapping("{id}")
    @ApiOperation("Update data about user")
    public void update(@ApiParam(USER_ID) @PathVariable("id") Long id,
                       @ApiParam("Changed data about user") @RequestBody UserDto user) {
        userService.update(id, userMapper.userDtoToUser(user));
    }

    @PreAuthorize("hasRole('editOtherUser')")
    @DeleteMapping("{id}")
    @ApiOperation("Delete user")
    public void delete(@ApiParam(USER_ID) @PathVariable("id") Long id) {
        userService.delete(id);
    }

    // TODO: Логику двух строчек выше лучше перенести в сервис
    @PutMapping("/{id}/contact")
    @ApiOperation("Change user contacts")
    public ContactDto updateUserContact(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                        @ApiParam(USER_ID) @PathVariable("id") Long id,
                                        @ApiParam("Changed contacts data") @RequestBody ContactDto contactDto) {
        User user = userService.getUser(id);
        if (currentUser.getId().equals(id) || currentUser.getRole().isEditOtherUser()) {
            userService.updateContacts(user.getContact(), contactMapper.contactDtoToContact(contactDto));
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
        return contactMapper.contactToContactDto(user.getContact());
    }

    @GetMapping("/filter/user-by-fullname")
    @ApiOperation(value = "Filtering users by full name", response = User.class, responseContainer = "List")
    public List<UserDto> filterUsers(@ApiParam("Data to filter") @RequestBody FilterUserDto filterUserDto) {
        List<User> users = userService.filterUsers(filterUserDto.getName(),
                filterUserDto.getSurname(), filterUserDto.getLogin());
        return userMapper.userToUserDto(users);
    }

    @GetMapping("/filter/skills")
    @ApiOperation("Filtering users by skills")
    public List<UserDto> filterUsers(@RequestBody FilterUserSkillsDto filterUserSkillsDto) {
        List<User> users = userService.filterBySkills(filterUserSkillsDto.getSkillsId());
        return userMapper.userToUserDto(users);
    }

    @PostMapping("/filter")
    @ApiOperation("Filtering users")
    public Page<UserDto> filterUsersScope(@ApiParam("Filter data") @RequestBody FilterScopeUserDto filter,
                                          @ApiParam("Page number") @RequestParam Integer page,
                                          @ApiParam("Page size") @RequestParam PageSize pageSize) {
        Page<UserView> users = userService.filterUserScope(filter, page, pageSize.size);
        return users.map(userMapper::userViewToUserDto);
    }

    @PostMapping("/filter/vacation")
    @ApiOperation("Filtering users by vacation")
    public List<UserDto> filerUserByVacation(@ApiParam("Filter data") @RequestBody FilterScopeUserDto filter) {
        List<UserView> users = userService.filterUserByVacationDate(filter);
        return users.stream().map(userMapper::userViewToUserDto).collect(Collectors.toList());
    }

    @PutMapping("{id}/role")
    @ApiOperation("Update user role")
    public void updateUserRole(@ApiIgnore @AuthenticationPrincipal User currentUser,
                               @ApiParam(USER_ID) @PathVariable("id") Long id,
                               @ApiParam("New user role") @RequestBody String roleName) {
        if (userService.getUser(currentUser.getId()).getRole().isEditOtherUser()) {
            userService.setRole(id, roleName);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @PutMapping("{id}/position")
    @ApiOperation("Update user position")
    public void updateUserPosition(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                   @ApiParam(USER_ID) @PathVariable("id") Long id,
                                   @ApiParam("New user position id") @RequestBody String position) {
        if (currentUser.getId().equals(id) || currentUser.getRole().isEditOtherUser()) {
            userService.setPosition(id, position);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @PutMapping("{id}/department")
    @ApiOperation("Update user department")
    public void updateUserDepartment(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                     @ApiParam(USER_ID) @PathVariable("id") Long id,
                                     @ApiParam("New user department id") @RequestBody String name) {
        if (currentUser.getId().equals(id) || currentUser.getRole().isEditOtherUser()) {
            userService.setDepartment(id, name);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }

    }

    @PutMapping("{user_id}/manager/set/{manager_id}")
    @ApiOperation("Set manager")
    public UserDto setManager(@ApiParam(USER_ID) @PathVariable(USER_ID_PATH_VARIABLE) Long userId,
                              @ApiParam("Manager id") @PathVariable("manager_id") Long managerId) {
        User user = userService.setManager(userId, managerId);
        return userMapper.userToUserDto(user);
    }

    @PutMapping("{user_id}/manager/delete")
    @ApiOperation("Delete manager")
    public UserDto deleteManager(@ApiParam(USER_ID) @PathVariable(USER_ID_PATH_VARIABLE) Long userId) {
        User user = userService.removeManager(userId);
        return userMapper.userToUserDto(user);
    }

    @PutMapping("{user_id}/personal_information/edit")
    @ApiOperation("Edit personal information")
    public void editPersonalInformation(@ApiParam(USER_ID) @PathVariable(USER_ID_PATH_VARIABLE) Long userId,
                                           @ApiParam("Personal information") @RequestBody(required = false) String personalInformation) {
        if (UserAuthenticationHelper.getAuthenticatedUserData().getRole().isEditOtherUser() || UserAuthenticationHelper.getAuthenticatedUserData().getId().equals(userId)) {
            userService.editPersonalInformation(userId, personalInformation);
        } else {
            throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
    }

    @GetMapping("{user_id}/isOnVacation")
    @ApiOperation("Get user vacation status")
    public Boolean userIsOnVacation(@ApiParam(USER_ID) @PathVariable(USER_ID_PATH_VARIABLE) Long userId) {
        return userService.inVacation(userId);
    }
}

package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.ForbiddenException;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.mapper.UserSkillMapper;
import xrm.extrim.planner.service.UserSkillService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/user-skills")
@Api(description = "Operations pertaining to user's skills")
public class UserSkillController {
    private final static String USER_ID_DESCRIPTION = "User id";
    private final static String SKILL_ID_DESCRIPTION = "Skill id";
    private final static String USER_ID = "user_id";
    private final static String SKILL_ID = "skill_id";

    private final UserSkillService userSkillService;
    private final UserMapper userMapper;
    private final UserSkillMapper userSkillMapper;

    public UserSkillController(UserSkillService userSkillService, UserMapper userMapper, UserSkillMapper userSkillMapper) {
        this.userSkillService = userSkillService;
        this.userMapper = userMapper;
        this.userSkillMapper = userSkillMapper;
    }

    @DeleteMapping("{user_id}/skill/{skill_id}")
    @ApiOperation(value = "Delete skill from user", response = UserDto.class)
    public UserDto deleteUserSkill(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                   @ApiParam(USER_ID_DESCRIPTION) @PathVariable(name = USER_ID) Long userId,
                                   @ApiParam(SKILL_ID_DESCRIPTION) @PathVariable(name = SKILL_ID) Long skillId) {
        if (currentUser.getId().equals(userId) || currentUser.getRole().isSetSkillToUser()) {
            User user = userSkillService.deleteSkill(userId, skillId);
            return userMapper.userToUserDto(user);
        }

        throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
    }

    @PutMapping("/{user_id}/skills")
    @ApiOperation(value = "Evaluate skill", response = UserDto.class)
    public UserDto evaluateSkills(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                  @ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id,
                                  @ApiParam(value = "Data about the list of skills and new grades", collectionFormat = "SetUserSkillsDto")
                                  @RequestBody List<UserSkillDto> userSkillDtos) {
        if (currentUser.getId().equals(id) || currentUser.getRole().isRateSkill()) {
            User user = userSkillService.evaluateSkills(id, userSkillDtos);
            return userMapper.userToUserDto(user);
        }

        throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
    }

    @GetMapping("all_skills/{user_id}")
    @ApiOperation(value = "Get all user's skills with versions", response = UserSkillDto.class, responseContainer = "List")
    public List<UserSkillDto> getAllSkillsWithVersionsById(@ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id) {
        List<UserSkill> userSkills = userSkillService.getAllSkillsWithVersionsById(id);
        return userSkillMapper.listUserSkillToListUserSkillDto(userSkills);
    }


    @PutMapping("/{user_id}/update/{skill_id}")
    @ApiOperation(value = "Update user skill", response = UserSkillDto.class)
    public UserSkillDto updateUserSkill(@ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id,
                                           @ApiParam(SKILL_ID_DESCRIPTION) @PathVariable(SKILL_ID) Long skillId,
                                           @ApiParam("User skill") @RequestBody UserSkillDto userSkillDto) {
        return userSkillMapper.userSkillToUserSkillDto(userSkillService.updateUserSkill(id, skillId, userSkillDto));
    }

    @GetMapping("/{user_id}/get/{skill_id}")
    @ApiOperation(value = "Get users skill by id", response = UserSkillDto.class)
    public UserSkillDto getUserSkillById(@ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long userId,
                                         @ApiParam(SKILL_ID_DESCRIPTION) @PathVariable(SKILL_ID) Long skillId) {
        return userSkillMapper.userSkillToUserSkillDto(userSkillService.getLatestVersionUserSkill(userId, skillId));
    }

    @GetMapping("/{user_id}/skill")
    @ApiOperation(value = "Get users skill by id", response = UserDto.class, responseContainer = "List")
    public List<UserSkillDto> getUserSkill(@ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id) {
        return userSkillMapper.listUserSkillToListUserSkillDto(userSkillService.getUserSkill(id));
    }

    @PostMapping("/{user_id}/skill")
    @ApiOperation(value = "Adding skills to user", response = User.class)
    public UserSkillDto setSkill(@ApiIgnore @AuthenticationPrincipal User currentUser,
                                 @ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id,
                                 @ApiParam(value = "Data about the list of skills and grades", collectionFormat = "SetUserSkillsDto")
                                 @RequestBody UserSkillDto userSkillDtos) {
        if (currentUser.getId().equals(id) || currentUser.getRole().isSetSkillToUser()) {
            return userSkillMapper.userSkillToUserSkillDto(userSkillService.setSkill(id, userSkillDtos));
        }

        throw new ForbiddenException(Exception.ACCESS_DENIED_FOR_NOT_ADMIN.getDescription());
    }

    @PostMapping("/{user_id}/skills")
    @ApiOperation(value = "Adding skills to user", response = User.class)
    public UserDto setSkills(@ApiIgnore @AuthenticationPrincipal User currentUser,
                             @ApiParam(USER_ID_DESCRIPTION) @PathVariable(USER_ID) Long id,
                             @ApiParam(value = "Data about the list of skills and grades", collectionFormat = "SetUserSkillsDto")
                             @RequestBody List<UserSkillDto> userSkillDtos) {
        if (currentUser.getId().equals(id) || currentUser.getRole().isSetSkillToUser()) {
            User user = userSkillService.setSkills(id, userSkillDtos);
            return userMapper.userToUserDto(user);
        }

        throw new ForbiddenException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
    }
}

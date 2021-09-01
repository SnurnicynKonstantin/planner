package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xrm.extrim.planner.controller.dto.GroupDto;
import xrm.extrim.planner.domain.Group;
import xrm.extrim.planner.mapper.GroupMapper;
import xrm.extrim.planner.service.GroupService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/group")
@Api(description = "Operations pertaining to skill group")
public class GroupController {

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    @ApiOperation(value = "Get all groups", response = Group.class, responseContainer = "List")
    public List<Group> getAllGroups() {
        return groupService.getAll();
    }

    @PreAuthorize("hasRole('skillOperations')")
    @PostMapping
    @ApiOperation(value = "Create skill group", response = Group.class, responseContainer = "List")
    public Group createGroup(@ApiParam("Unique group name")
                             @RequestBody GroupDto groupDto) {
        return groupService.createGroup(groupMapper.groupDtoToGroup(groupDto));
    }

}

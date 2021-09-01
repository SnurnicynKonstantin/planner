package xrm.extrim.planner.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
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
import xrm.extrim.planner.controller.dto.RoleDto;
import xrm.extrim.planner.domain.Role;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.service.RoleService;
import xrm.extrim.planner.view.RoleViews;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/role")
@Api(description = "Operations pertaining to roles")
public class RoleController {

    RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ApiOperation("Create role")
    public Role createRole(@ApiParam("Data about the role")
                               @RequestBody RoleDto roleDto) {
        return roleService.createRole(roleDto);
    }

    @GetMapping
    @ApiOperation("Get all roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("{id}")
    @ApiOperation("Get role by id")
    public Role getRoleById(@ApiParam("Role id") @PathVariable("id") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @PutMapping("{id}")
    @ApiOperation("Update role")
    public Role updateRole(@ApiParam("Role id") @PathVariable("id") Long roleId,
                           @ApiParam("Changed data about the role and its descriptions")
                           @RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleId, roleDto);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete role")
    public void deleteRoleById(@ApiParam("Role id") @PathVariable("id") Long roleId) {
        roleService.deleteRoleById(roleId);
    }

    @JsonView(RoleViews.Permissions.class)
    @GetMapping("/permissions")
    @ApiOperation("Get user permissions")
    public Role getUserPermissions(@ApiIgnore @AuthenticationPrincipal User currentUser) {
        return currentUser.getRole();

    }
}

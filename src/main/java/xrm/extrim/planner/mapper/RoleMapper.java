package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.RoleDto;
import xrm.extrim.planner.domain.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role RoleDtoToRole(RoleDto roleDto);
    RoleDto roleToRoleDto(Role role);
}

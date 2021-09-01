package xrm.extrim.planner.service;

import org.springframework.stereotype.Service;
import xrm.extrim.planner.controller.dto.RoleDto;
import xrm.extrim.planner.domain.Role;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.RoleMapper;
import xrm.extrim.planner.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public Role createRole(RoleDto roleDto) {
        Role role = roleMapper.RoleDtoToRole(roleDto);
        if (roleRepository.getByName(role.getName()) != null) {
            throw new PlannerException(String.format(Exception.ROLE_NOT_FOUND.getDescription(), role.getName()));
        }

        role.setId(null);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new PlannerException(String.format(Exception.ROLE_NOT_FOUND.getDescription(), roleId));
        }
        return role;
    }

    public Role updateRole(Long roleId, RoleDto roleDto) {
        if (roleRepository.findById(roleId).orElse(null) == null) {
            throw new PlannerException(String.format(Exception.ROLE_NOT_FOUND.getDescription(), roleId));
        }
        Role role = roleMapper.RoleDtoToRole(roleDto);
        role.setId(roleId);
        return roleRepository.save(role);
    }

    public void deleteRoleById(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }
}

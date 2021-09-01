package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.GroupDto;
import xrm.extrim.planner.domain.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupDto groupToGroupDto(Group group);
    Group groupDtoToGroup(GroupDto groupDto);
}

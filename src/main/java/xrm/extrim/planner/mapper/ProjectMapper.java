package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import xrm.extrim.planner.controller.dto.ProjectDto;
import xrm.extrim.planner.domain.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project projectDtoToProject(ProjectDto projectDto);
    ProjectDto projectToProjectDto(Project project);
}

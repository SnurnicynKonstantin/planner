package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xrm.extrim.planner.controller.dto.ProjectHistoryDto;
import xrm.extrim.planner.domain.ProjectHistory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectHistoryMapper {
    ProjectHistory fromProjectHistoryDtoToProjectHistory(ProjectHistoryDto projectHistoryDto);
    @Mapping(target = "userId", source = "projectHistory.user.id")
    @Mapping(target = "projectId", source = "projectHistory.project.id")

    ProjectHistoryDto fromProjectHistoryToProjectHistoryDto(ProjectHistory projectHistory);
    List<ProjectHistory> formListProjectHistoryDtoToListProjectHistory(List<ProjectHistoryDto> projectHistoryDtoList);
    List<ProjectHistoryDto> formListProjectHistoryToListProjectHistoryDto(List<ProjectHistory> projectHistoryList);
}

package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xrm.extrim.planner.controller.dto.ProjectHistoryDto;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.mapper.ProjectHistoryMapper;
import xrm.extrim.planner.service.ProjectHistoryService;
import java.util.List;
import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/project/history")
@Api(description = "Operations pertaining to the history of the user projects")
public class ProjectHistoryController {
    private final ProjectHistoryService projectHistoryService;
    private final ProjectHistoryMapper projectHistoryMapper;

    @Autowired
    public ProjectHistoryController(ProjectHistoryService projectHistoryService, ProjectHistoryMapper projectHistoryMapper) {
        this.projectHistoryService = projectHistoryService;
        this.projectHistoryMapper = projectHistoryMapper;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Get the history of user projects", response = Skill.class)
    public ProjectHistoryDto getHistoryById(@ApiParam("User id") @PathVariable("id") Long id) {
        return projectHistoryMapper.fromProjectHistoryToProjectHistoryDto(projectHistoryService.getById(id));
    }

    @GetMapping("user/{id}")
    @ApiOperation(value = "Get the history of user projects", response = Skill.class)
    public List<ProjectHistoryDto> getHistoryByUserId(@ApiParam("User id") @PathVariable("id") Long id) {
        return projectHistoryMapper.formListProjectHistoryToListProjectHistoryDto(projectHistoryService.getAllByUserId(id));
    }

    @PostMapping
    @ApiOperation("Create project history")
    public ProjectHistoryDto create(@ApiParam(value = "Data about project history", format = "project history") @RequestBody ProjectHistoryDto projectHistoryDto) {
        return projectHistoryMapper.fromProjectHistoryToProjectHistoryDto(projectHistoryService.create(projectHistoryDto));
    }

    @PutMapping("{id}")
    @ApiOperation("Update project history")
    public ProjectHistoryDto update(@ApiParam("Project history id") @PathVariable("id") Long id,
                                 @ApiParam("Changed data about project history") @RequestBody ProjectHistoryDto projectHistoryDto) {
        return projectHistoryMapper.fromProjectHistoryToProjectHistoryDto(projectHistoryService.update(id, projectHistoryDto));
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete project history")
    public void delete(@ApiParam("Project history id") @PathVariable("id") Long id) {
        projectHistoryService.delete(id);
    }
}

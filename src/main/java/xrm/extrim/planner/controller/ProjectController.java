package xrm.extrim.planner.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.ProjectDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Project;
import xrm.extrim.planner.enums.PageSize;
import xrm.extrim.planner.service.ProjectService;
import xrm.extrim.planner.view.ProjectViews;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/project")
@Api(description = "Operations pertaining to project")
public class ProjectController {

    private final ProjectService projectService;
    private final static String PROJECT_ID = "Project id";

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @JsonView(ProjectViews.IdNameDate.class)
    @GetMapping
    @ApiOperation(value = "Get all projects", response = Project.class, responseContainer = "List")
    public List<Project> getAllProjects() {
        return projectService.getAll();
    }

    @PostMapping
    @ApiOperation("Create project")
    public Project createProject(@ApiParam("Data about project")
                                 @RequestBody ProjectDto projectDto) {
        return projectService.createProject(projectDto);
    }

    @GetMapping("{id}")
    @ApiOperation("Get project by id")
    public Project getProjectById(@ApiParam(PROJECT_ID) @PathVariable("id") Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @PutMapping("{id}")
    @ApiOperation("Update project")
    public Project updateProject(@ApiParam(PROJECT_ID) @PathVariable("id") Long projectId,
                                 @ApiParam("Changed data about the project") @RequestBody ProjectDto projectDto) {
        return projectService.updateProject(projectId, projectDto);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Delete project")
    public void deleteProjectById(@ApiParam(PROJECT_ID) @PathVariable("id") Long projectId) {
        projectService.deleteProjectById(projectId);
    }

    @GetMapping("{id}/participants")
    @ApiOperation("Get project participants by proj id")
    public List<UserDto> getProjectParticipantsById(@ApiParam(PROJECT_ID) @PathVariable("id") Long projectId) {
        return projectService.getProjectParticipants(projectId);
    }

    @GetMapping("/page")
    @ApiOperation("Get all projects by page")
    public Page<Project> getAllProjectsByPage(@ApiParam("Page number") @RequestParam(defaultValue = "0") Integer page,
                                              @ApiParam("Page size") @RequestParam(defaultValue = "SMALL") PageSize pageSize) {
        return projectService.getAllProjectsByPage(page, pageSize.size);
    }
}

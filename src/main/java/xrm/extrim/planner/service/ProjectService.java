package xrm.extrim.planner.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.controller.dto.ProjectDto;
import xrm.extrim.planner.controller.dto.UserDto;
import xrm.extrim.planner.domain.Project;
import xrm.extrim.planner.domain.ProjectHistory;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.ProjectMapper;
import xrm.extrim.planner.mapper.UserMapper;
import xrm.extrim.planner.repository.ProjectHistoryRepository;
import xrm.extrim.planner.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectHistoryRepository projectHistoryRepository;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectHistoryRepository projectHistoryRepository,
                          ProjectMapper projectMapper, UserMapper userMapper) {
        this.projectRepository = projectRepository;
        this.projectHistoryRepository = projectHistoryRepository;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
    }

    public List<Project> getAll(){
        return projectRepository.findAll();
    }

    public Project createProject(ProjectDto projectDto){
        Project project = projectMapper.projectDtoToProject(projectDto);

        if(projectRepository.findByName(project.getName()) != null){
            throw new PlannerException("Project with that name already existed");
        }

        return projectRepository.save(project);
    }

    public Project getProjectById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new PlannerException(String.format(Exception.PROJECT_NOT_FOUND.getDescription(), projectId)));
    }

    public List<UserDto> getProjectParticipants(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new PlannerException(String.format(Exception.PROJECT_NOT_FOUND.getDescription(), projectId)))
                .getProjectHistories().stream()
                .map(ProjectHistory::getUser)
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public Project updateProject(Long projectId, ProjectDto projectDto) {
        if(projectRepository.findById(projectId).orElse(null) == null){
            throw new PlannerException(String.format(Exception.PROJECT_NOT_FOUND.getDescription(), projectId));
        }
        Project project  = projectMapper.projectDtoToProject(projectDto);
        project.setId(projectId);
        return projectRepository.save(project);
    }

    public void deleteProjectById(Long projectId){
        for (ProjectHistory projectHistory : projectHistoryRepository.findByProjectId(projectId)) {
            projectHistory.setProject(null);
            projectHistoryRepository.save(projectHistory);
        }
        projectRepository.deleteById(projectId);
    }

    public Page<Project> getAllProjectsByPage(int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return projectRepository.findAllByOrderByIdDesc(pageable);
    }
}

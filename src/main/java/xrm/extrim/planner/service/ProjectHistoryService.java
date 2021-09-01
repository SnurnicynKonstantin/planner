package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.controller.dto.ProjectHistoryDto;
import xrm.extrim.planner.domain.ProjectHistory;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.ProjectHistoryMapper;
import xrm.extrim.planner.repository.ProjectHistoryRepository;

import java.util.List;

@Service
public class ProjectHistoryService {
    private final ProjectHistoryMapper mapper;
    private final ProjectHistoryRepository projectHistoryRepository;
    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public ProjectHistoryService(ProjectHistoryMapper mapper, ProjectHistoryRepository projectHistoryRepository, UserService userService, ProjectService projectService) {
        this.mapper = mapper;
        this.projectHistoryRepository = projectHistoryRepository;
        this.userService = userService;
        this.projectService = projectService;
    }

    public ProjectHistory getById(Long id) throws EntityNotFoundException {
        ProjectHistory projectHistory =  projectHistoryRepository.findById(id).orElse(null);
        if (projectHistory == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), id));
        }
        return projectHistory;
    }

    public List<ProjectHistory> getAllByUserId(Long id) {
        return projectHistoryRepository.findAllByUserIdOrderByWorkStartDateDesc(id);
    }

    public ProjectHistory create(ProjectHistoryDto projectHistoryDto) {
        if (projectHistoryDto.getWorkEndDate()!= null &&
                projectHistoryDto.getWorkEndDate().isBefore(projectHistoryDto.getWorkStartDate())) {
            throw new PlannerException(Exception.WRONG_DATE_FORMAT.getDescription());
        }
        User user = userService.getUser(projectHistoryDto.getUserId());
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), projectHistoryDto.getUserId()));
        }

        ProjectHistory projectHistory = mapper.fromProjectHistoryDtoToProjectHistory(projectHistoryDto);
        projectHistory.setUser(user);

        if(projectHistoryDto.getProjectId() != null){
            projectHistory.setProject(projectService.getProjectById(projectHistoryDto.getProjectId()));
        }

        return projectHistoryRepository.save(projectHistory);
    }

    public ProjectHistory update(Long id, ProjectHistoryDto projectHistoryDto) {
        if (projectHistoryDto.getWorkEndDate()!= null &&
                projectHistoryDto.getWorkEndDate().isBefore(projectHistoryDto.getWorkStartDate())) {
            throw new PlannerException(Exception.WRONG_DATE_FORMAT.getDescription());
        }
        User user = userService.getUser(projectHistoryDto.getUserId());
        if(user == null) {
            throw  new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), projectHistoryDto.getUserId()));
        }
        ProjectHistory projectHistoryFromDb = projectHistoryRepository.findById(id).orElse(null);
        if(projectHistoryFromDb == null) {
            throw  new EntityNotFoundException(String.format(Exception.PROJECT_HISTORY_NOT_FOUND.getDescription(), id));
        }

        ProjectHistory projectHistory = mapper.fromProjectHistoryDtoToProjectHistory(projectHistoryDto);
        projectHistory.setId(id);
        projectHistory.setUser(user);
        return projectHistoryRepository.save(projectHistory);
    }

    public void delete(Long id) {
        projectHistoryRepository.deleteById(id);
    }

}

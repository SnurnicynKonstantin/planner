package xrm.extrim.planner.common;

import xrm.extrim.planner.controller.dto.ProjectHistoryDto;
import xrm.extrim.planner.domain.ProjectHistory;
import xrm.extrim.planner.domain.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class ProjectHistoryTestData {

    private ProjectHistoryTestData() {
    }

    public static ProjectHistory getProjectHistory() {
        ProjectHistory projectHistory = new ProjectHistory();
        projectHistory.setId(1L);
        projectHistory.setProjectName("Name");
        projectHistory.setWorkStartDate(LocalDate.parse("2021-07-01"));
        projectHistory.setWorkEndDate(LocalDate.parse("2021-07-23"));
        projectHistory.setUser(UserTestData.getUser());
        projectHistory.setComment("comment");
        return projectHistory;
    }

    public static ProjectHistory getProjectHistory(String projectName, String workStartDate, String workEndDate, User user) {
        ProjectHistory projectHistory = new ProjectHistory();
        projectHistory.setProjectName(projectName);
        projectHistory.setWorkStartDate(LocalDate.parse(workStartDate));
        projectHistory.setWorkEndDate(LocalDate.parse(workEndDate));
        projectHistory.setUser(user);
        return projectHistory;
    }

    public static List<ProjectHistory> getProjectHistoryList() {
        ArrayList<ProjectHistory> list = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            list.add(getProjectHistory());
        }
        return list;
    }

    public static ProjectHistoryDto getProjectHistoryDTO() {
        ProjectHistoryDto projectHistoryDTO = new ProjectHistoryDto();
        projectHistoryDTO.setUserId(1L);
        projectHistoryDTO.setProjectName("Name");
        projectHistoryDTO.setWorkStartDate(LocalDate.parse("2021-07-01"));
        projectHistoryDTO.setWorkEndDate(LocalDate.parse("2021-07-23"));
        projectHistoryDTO.setComment("comment");
        return projectHistoryDTO;
    }

    public static ProjectHistoryDto getProjectHistoryDTOWithWrongDate() {
        ProjectHistoryDto projectHistoryDTO = new ProjectHistoryDto();
        projectHistoryDTO.setUserId(1L);
        projectHistoryDTO.setProjectName("Name");
        projectHistoryDTO.setWorkStartDate(LocalDate.parse("2021-07-01"));
        projectHistoryDTO.setWorkEndDate(LocalDate.parse("2021-06-23"));
        return projectHistoryDTO;
    }
}

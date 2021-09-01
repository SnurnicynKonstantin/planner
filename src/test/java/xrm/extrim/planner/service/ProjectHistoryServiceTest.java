package xrm.extrim.planner.service;

import xrm.extrim.planner.markers.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.common.ProjectHistoryTestData;
import xrm.extrim.planner.common.UserTestData;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.ProjectHistoryDto;
import xrm.extrim.planner.domain.ProjectHistory;
import xrm.extrim.planner.domain.User;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class ProjectHistoryServiceTest extends PlannerTestBase {
    @Test
    public void getByIdOkTest() {
        ProjectHistory projectHistory = ProjectHistoryTestData.getProjectHistory();
        Mockito.when(projectHistoryRepositoryMock.findById(1L)).thenReturn(Optional.of(projectHistory));
        Assert.assertEquals(projectHistory,projectHistoryService.getById(1L));
    }

    @Test
    public void getAllByUserIdOkTest() {
        List<ProjectHistory> projectHistoryList = ProjectHistoryTestData.getProjectHistoryList();
        Mockito.when(projectHistoryRepositoryMock.findAllByUserIdOrderByWorkStartDateDesc(Mockito.anyLong())).thenReturn(projectHistoryList);
        Assert.assertEquals(projectHistoryList,projectHistoryService.getAllByUserId(1L));
    }

    @Test
    public void createOkTest() {
        User user = UserTestData.getUser();
        ProjectHistory projectHistory = ProjectHistoryTestData.getProjectHistory();
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTO();
        Mockito.when(userRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);

        ProjectHistory projectHistoryFromService = projectHistoryService.create(projectHistoryDTO);
        projectHistoryFromService.setId(1L);
        Assert.assertEquals(projectHistory,projectHistoryFromService);
    }

    @Test(expected = RuntimeException.class)
    public void createUserNotFoundTest() {
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTO();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(null);
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);
        projectHistoryService.create(projectHistoryDTO);
    }

    @Test(expected = RuntimeException.class)
    public void createWrongDateFormatTest() {
        User user = UserTestData.getUser();
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTOWithWrongDate();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);
        projectHistoryService.create(projectHistoryDTO);
    }

    @Test
    public void updateOkTest() {
        User user = UserTestData.getUser();
        ProjectHistory projectHistory = ProjectHistoryTestData.getProjectHistory();
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTO();
        Mockito.when(userRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectHistoryRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(projectHistory));
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);

        ProjectHistory projectHistoryFromService = projectHistoryService.update(1L, projectHistoryDTO);
        projectHistoryFromService.setId(1L);
        Assert.assertEquals(projectHistory,projectHistoryFromService);
    }

    @Test(expected = RuntimeException.class)
    public void updateHistoryNotFoundTest() {
        User user = UserTestData.getUser();
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTO();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);
        Mockito.when(projectHistoryRepositoryMock.findById(Mockito.anyLong()).orElse(null)).thenReturn(null);
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);
        projectHistoryService.update(1L, projectHistoryDTO);
    }

    @Test(expected = RuntimeException.class)
    public void updateUserNotFoundTest() {
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTO();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(null);
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);
        projectHistoryService.update(1L, projectHistoryDTO);
    }

    @Test(expected = RuntimeException.class)
    public void updateWrongDateFormatTest() {
        User user = UserTestData.getUser();
        ProjectHistoryDto projectHistoryDTO = ProjectHistoryTestData.getProjectHistoryDTOWithWrongDate();
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(user);
        Mockito.when(projectHistoryRepositoryMock.save(Mockito.any(ProjectHistory.class))).thenAnswer(i -> i.getArguments()[0]);
        projectHistoryService.update(1L, projectHistoryDTO);
    }

    @Test
    public void deleteOkTest() {
        projectHistoryService.delete(1L);
        Mockito.verify(projectHistoryRepositoryMock).deleteById(1L);
    }
}

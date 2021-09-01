package xrm.extrim.planner.service;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.common.SkillTestData;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.common.UserTestData;
import xrm.extrim.planner.common.UsersSkillsTestData;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.markers.UnitTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class UserSkillServiceTest extends PlannerTestBase {

    @Test
    public void testDeleteSkillOk() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        user.setUserSkills(Lists.list(userSkill));

        assertFalse(userSkill.isDeleted());
        userSkillService.deleteSkill(user.getId(), skill.getId());
        assertTrue(userSkill.isDeleted());
    }

    @Test
    public void testDeleteSkillAlreadyDeletedWrong() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        userSkill.setDeleted(true);
        user.setUserSkills(Lists.list(userSkill));

        assertThrows(PlannerException.class, () -> userSkillService.deleteSkill(user.getId(), skill.getId()));
    }

    @Test
    public void testDeleteSkillUserIdAndSkillIdIncorrectWrong() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        userSkill.setDeleted(true);
        user.setUserSkills(Lists.list(userSkill));

        assertThrows(EntityNotFoundException.class, () -> userSkillService.deleteSkill(user.getId() + 1, skill.getId()));
        assertThrows(EntityNotFoundException.class, () -> userSkillService.deleteSkill(user.getId(), skill.getId() + 1));
    }

    @Test
    public void testSetSkillsUserIdAndSkillIdIncorrectWrong() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        userSkill.setDeleted(true);
        user.setUserSkills(Lists.list(userSkill));

        UserSkillDto userSkillDto = new UserSkillDto();
        userSkillDto.setId(skill.getId() + 1);
        userSkillDto.setRate(userSkill.getRate() - 1);

        try (MockedStatic<UserAuthenticationHelper> theMock = Mockito.mockStatic(UserAuthenticationHelper.class)) {
            theMock.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            assertThrows(EntityNotFoundException.class, () -> userSkillService.setSkills(user.getId(), Lists.list(userSkillDto)));

            userSkillDto.setId(skill.getId());
            assertThrows(EntityNotFoundException.class, () -> userSkillService.setSkills(user.getId() + 1, Lists.list(userSkillDto)));
        }
    }

    @Test
    public void testSetSkillsNewSkillsOk() {
        User userFromDto = UserTestData.getUser();
        Skill skill = SkillTestData.getSkill();
        List<UserSkillDto> usersSkillsDtos = UsersSkillsTestData.getUserSkillDtoList();
        List<UserSkill> userSkillsFromDto = UsersSkillsTestData.getUserSkillList();
        userFromDto.setUserSkills(userSkillsFromDto);
        UserSkill userSkill = UsersSkillsTestData.getUserSkill();
        when(skillRepositoryMock.findById(anyLong())).thenReturn(Optional.of(skill));
        when(userSkillsRepositoryMock.save(any(UserSkill.class))).thenReturn(userSkill);
        when(userRepositoryMock.findById(userFromDto.getId())).thenReturn(Optional.of(userFromDto));

        try (MockedStatic<UserAuthenticationHelper> theMock = Mockito.mockStatic(UserAuthenticationHelper.class)) {
            theMock.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(userFromDto);

            User changedUser = userSkillService.setSkills(userFromDto.getId(), usersSkillsDtos);
            assertEquals(userFromDto, changedUser);
        }
    }

    @Test
    public void testSetSkillsDeletedSkillsOk() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        userSkill.setDeleted(true);
        user.setUserSkills(Lists.list(userSkill));

        UserSkillDto userSkillDto = new UserSkillDto();
        userSkillDto.setId(skill.getId());
        userSkillDto.setRate(userSkill.getRate() - 1);

        UserSkill secondVersionSkill = new UserSkill();
        secondVersionSkill.setId(userSkill.getId() + 1);
        secondVersionSkill.setRate(userSkillDto.getRate());
        secondVersionSkill.setUser(user);
        secondVersionSkill.setDeleted(false);
        secondVersionSkill.setComment(userSkillDto.getComment());
        secondVersionSkill.setSkill(skill);

        UserSkillBuilder mockBuilder = mock(UserSkillBuilder.class);
        when(mockBuilder.build()).thenReturn(secondVersionSkill);
        when(userSkillsRepositoryMock.save(secondVersionSkill)).thenReturn(secondVersionSkill);
        when(userSkillsRepositoryMock.findLatestVersionIdBySkillIdAndUserId(user.getId(), skill.getId())).thenReturn(secondVersionSkill.getId());
        when(userSkillsRepositoryMock.getById(secondVersionSkill.getId())).thenReturn(secondVersionSkill);
        when(userSkillsRepositoryMock.findById(secondVersionSkill.getId())).thenReturn(Optional.of(secondVersionSkill));
        when(userSkillsRepositoryMock.findLatestVersionsIdsByUserId(user.getId())).thenReturn(Lists.list(secondVersionSkill.getId()));

        try (MockedStatic<UserAuthenticationHelper> theMock = Mockito.mockStatic(UserAuthenticationHelper.class)) {
            theMock.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            User resultUser = userSkillService.setSkills(user.getId(), Lists.list(userSkillDto));
            assertEquals(resultUser.getUserSkills().size(), 1);
            assertEquals(resultUser.getUserSkills().get(0), secondVersionSkill);
        }
    }

    @Test
    public void testGetUserSkillOk() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkill userSkill = getTestUserSkill(user, skill);
        userSkill.setDeleted(true);

        UserSkill secondVersionSkill = new UserSkill();
        secondVersionSkill.setId(userSkill.getId() + 1);
        secondVersionSkill.setDeleted(false);
        secondVersionSkill.setSkill(skill);
        secondVersionSkill.setVersion(1);

        user.setUserSkills(Lists.list(userSkill, secondVersionSkill));
        when(userSkillsRepositoryMock.save(secondVersionSkill)).thenReturn(secondVersionSkill);
        when(userSkillsRepositoryMock.findLatestVersionIdBySkillIdAndUserId(user.getId(), skill.getId())).thenReturn(secondVersionSkill.getId());
        when(userSkillsRepositoryMock.getById(secondVersionSkill.getId())).thenReturn(secondVersionSkill);
        when(userSkillsRepositoryMock.findById(secondVersionSkill.getId())).thenReturn(Optional.of(secondVersionSkill));
        when(userSkillsRepositoryMock.findLatestVersionsIdsByUserId(user.getId()))
                .thenReturn(Lists.list(secondVersionSkill.getId(), userSkill.getId()));

        List<UserSkill> resultUser = userSkillService.getUserSkill(user.getId());
        assertEquals(resultUser.size(), 1);
        assertEquals(resultUser.get(0), secondVersionSkill);
    }

    @Test
    public void testEvaluateSkillsUserHasNoSkillWrong() {
        User user = getTestUser();
        Skill skill = getTestSkill();
        UserSkillDto userSkillDto = new UserSkillDto();
        userSkillDto.setId(skill.getId() + 1);
        userSkillDto.setRate(3);

        try (MockedStatic<UserAuthenticationHelper> theMock = Mockito.mockStatic(UserAuthenticationHelper.class)) {
            theMock.when(UserAuthenticationHelper::getAuthenticatedUserData).thenReturn(user);

            assertThrows(PlannerException.class,() -> userSkillService.evaluateSkills(user.getId(), Lists.list(userSkillDto)));
        }
    }

    private User getTestUser() {
        User user = UserTestData.getUser();
        when(userRepositoryMock.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepositoryMock.existsById(user.getId())).thenReturn(true);
        return user;
    }

    private Skill getTestSkill() {
        Skill skill = SkillTestData.getSkill();
        when(skillRepositoryMock.findById(skill.getId())).thenReturn(Optional.of(skill));
        when(skillRepositoryMock.existsById(skill.getId())).thenReturn(true);
        return skill;
    }

    private UserSkill getTestUserSkill(User user, Skill skill) {
        UserSkill userSkill = UsersSkillsTestData.getUserSkill();
        when(userSkillsRepositoryMock.save(any(UserSkill.class))).thenReturn(userSkill);
        when(userSkillsRepositoryMock.findLatestVersionIdBySkillIdAndUserId(user.getId(), skill.getId())).thenReturn(userSkill.getId());
        when(userSkillsRepositoryMock.getById(userSkill.getId())).thenReturn(userSkill);
        when(userSkillsRepositoryMock.findById(userSkill.getId())).thenReturn(Optional.of(userSkill));
        return userSkill;
    }
}

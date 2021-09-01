package xrm.extrim.planner.service;


import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.markers.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.common.GroupTestData;
import xrm.extrim.planner.common.RateDescriptionTestData;
import xrm.extrim.planner.common.SkillTestData;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.domain.Group;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.UserSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
public class SkillServiceTests extends PlannerTestBase {
    private static final String NAME = "name";
    @Test
    public void getAllSkillOkTest() {
        List<Skill> skillsFromMock = SkillTestData.getSkills();
        Mockito.when(skillRepositoryMock.findAll()).thenReturn(skillsFromMock);
        List<Skill> skills = skillService.getAllSkills();
        Assert.assertEquals(skills,skillsFromMock);
    }

    @Test
    public void getSkillByIdOkTest() {
        Skill skill = SkillTestData.getSkill();
        skill.setId(1L);
        Mockito.when(skillRepositoryMock.findById(skill.getId())).thenReturn(Optional.of(skill));
        Assert.assertEquals(skill,skillService.getSkillById(skill.getId()));
    }

    @Test
    public void deleteSkillOkTest() {
        Skill skill = SkillTestData.getSkill();
        skill.setUserSkills(new ArrayList<>());
        Mockito.when(skillRepositoryMock.findById(skill.getId())).thenReturn(Optional.of(skill));
        skillService.deleteSkillById(skill.getId());
        Mockito.verify(skillRepositoryMock).deleteById(skill.getId());
    }

    @Test(expected = RuntimeException.class)
    public void deleteSkillNoExistTest() {
        Mockito.when(skillRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        skillService.deleteSkillById(123L);
    }

    @Test(expected = RuntimeException.class)
    public void deleteSkillWithUsersTest() {
        Skill skill = SkillTestData.getSkill();
        List<UserSkill> userSkillsMock = new ArrayList<>();
        userSkillsMock.add(new UserSkill());
        skill.setUserSkills(userSkillsMock);
        Mockito.when(skillRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(skill));
        skillService.deleteSkillById(1L);
    }

    @Test
    public void createSkillOkTest() {
        List<RateDescription> descriptions = RateDescriptionTestData.getRateDescriptions();
        Group group = GroupTestData.getGroup(1L, NAME);
        SkillDto skillDto = SkillTestData.getSkillDto(NAME,group.getId(), "discrpit", descriptions);
        skillDto.setRateDescriptions(rateDescriptionMapper.listRateDescriptionsToListRateDescriptionDtos(descriptions));
        Skill skill = SkillTestData.getSkill(1L,skillDto.getName(),skillDto.getDescription(),group, descriptions);
        skill.setRateDescriptions(descriptions);

        Mockito.when(skillRepositoryMock.save(Mockito.any(Skill.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(groupRepositoryMock.getById(Mockito.anyLong())).thenReturn(group);
        Mockito.when(descriptionRepositoryMock.save(Mockito.any(RateDescription.class))).thenAnswer(i -> i.getArguments()[0]);

        Skill skillFromService = skillService.createSkill(skillDto);
        for (RateDescription rateDescription : skillFromService.getRateDescriptions()) {
                Assert.assertEquals(skillFromService, rateDescription.getSkill());
        }
        Assert.assertEquals(skill.toString(), skillFromService.toString());
    }

    @Test
    public void updateSkillOkTest() {
        List<RateDescription> descriptions = RateDescriptionTestData.getRateDescriptions();
        Group group = GroupTestData.getGroup(1L,NAME);
        SkillDto skillDto = SkillTestData.getSkillDto(NAME,group.getId(), "discrpit", descriptions);
        Skill skill = SkillTestData.getSkill(1L,NAME,"discrpit",group, descriptions);
        skill.setRateDescriptions(descriptions);

        Mockito.when(skillRepositoryMock.getById(Mockito.anyLong())).thenReturn(skill);
        Mockito.when(skillRepositoryMock.save(Mockito.any(Skill.class))).thenAnswer(i -> i.getArguments()[0]);
        Mockito.when(groupRepositoryMock.getById(1L)).thenReturn(group);
        Mockito.when(descriptionRepositoryMock.save(Mockito.any(RateDescription.class))).thenAnswer(i -> i.getArguments()[0]);

        Skill skillFromService = skillService.updateSkill(1L, skillDto);
        skillFromService.setId(1L);

        Assert.assertEquals(skill, skillFromService);
    }

}

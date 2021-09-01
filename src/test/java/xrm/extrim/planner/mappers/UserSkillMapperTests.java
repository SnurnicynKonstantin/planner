package xrm.extrim.planner.mappers;

import xrm.extrim.planner.markers.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.UserSkill;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class UserSkillMapperTests extends PlannerTestBase {

    @Test
    public void UserSkillToUserSkillDto() {
        UserSkill userSkill = new UserSkill();
        userSkill.setId(3L);
        userSkill.setRate(4);
        userSkill.setConfirmed(true);

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setDescription("desc");
        skill.setName("name");

        List<RateDescription> descs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            RateDescription rateDescription = new RateDescription();
            rateDescription.setId((long) i);
            rateDescription.setSkill(skill);
            rateDescription.setRateNumber(i);
            rateDescription.setDescription("desc " + i);
            descs.add(rateDescription);
        }
        skill.setRateDescriptions(descs);

        userSkill.setSkill(skill);

        UserSkillDto userSkillDto = userSkillMapper.userSkillToUserSkillDto(userSkill);

        assert userSkillDto.getId().equals(userSkill.getSkill().getId());
        assert userSkillDto.getRate().equals(userSkill.getRate());
        assert userSkillDto.getConfirmed().equals(userSkill.isConfirmed());
        assert userSkillDto.getName().equals(userSkill.getSkill().getName());
        assert userSkillDto.getDescription().equals(userSkill.getSkill().getDescription());
        assert userSkillDto.getRateDescriptions().size() == userSkill.getSkill().getRateDescriptions().size();
        for(int i = 0; i < userSkillDto.getRateDescriptions().size(); i++){
            assert userSkillDto.getRateDescriptions().get(i).getRateNumber() == userSkill.getSkill().getRateDescriptions().get(i).getRateNumber();
            assert userSkillDto.getRateDescriptions().get(i).getDescription().equals(userSkill.getSkill().getRateDescriptions().get(i).getDescription());
        }
    }
}

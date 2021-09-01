package xrm.extrim.planner.mappers;

import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.markers.UnitTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xrm.extrim.planner.configuration.PlannerTestBase;
import xrm.extrim.planner.configuration.PlannerTestConfiguration;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.mapper.RateDescriptionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = PlannerTestConfiguration.class)
@RunWith(SpringRunner.class)
@Category(UnitTest.class)
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class SkillMapperTests extends PlannerTestBase {
    @Test
    public void skillToSkillDtoTest() {
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

        SkillDto skillDto = skillMapper.skillToSkillDto(skill);

        assert skillDto.getName().equals(skill.getName());
        assert skillDto.getDescription().equals(skill.getDescription());
        assert skillDto.getRateDescriptions().size() == skill.getRateDescriptions().size();
        for(int i = 0; i < skillDto.getRateDescriptions().size(); i++) {
            assert skillDto.getRateDescriptions().get(i).getDescription().equals(skill.getRateDescriptions().get(i).getDescription());
            assert skillDto.getRateDescriptions().get(i).getRateNumber() == (skill.getRateDescriptions().get(i).getRateNumber());
        }
    }

    @Test
    public void skillDtoToSkill() {
        SkillDto dto = new SkillDto();
        dto.setDescription("desc");
        dto.setName("name");

        List<RateDescription> descs = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            RateDescription rateDescription = new RateDescription();
            rateDescription.setRateNumber(i);
            rateDescription.setDescription("desc " + i);
            descs.add(rateDescription);
        }

        dto.setRateDescriptions(descs.stream().map(rateDescription -> Mappers.getMapper(RateDescriptionMapper.class).rateDescriptionToRateDescriptionDto(rateDescription)).collect(Collectors.toList()));

        Skill skill = skillMapper.skillDtoToSkill(dto);

        assert dto.getName().equals(skill.getName());
        assert dto.getDescription().equals(skill.getDescription());
        assert dto.getRateDescriptions().size() == skill.getRateDescriptions().size();

        for(int i = 0; i < dto.getRateDescriptions().size(); i++) {
            RateDescription dtoRate = Mappers.getMapper(RateDescriptionMapper.class).rateDescriptionDtoToRateDescription(dto.getRateDescriptions().get(i));
            RateDescription skillRate = skill.getRateDescriptions().get(i);

            assert skillRate.getId() == null;
            assert skillRate.getSkill() != null;
            assert dtoRate.getDescription().equals(skillRate.getDescription());
            assert dtoRate.getRateNumber() == skillRate.getRateNumber();
        }
    }
}

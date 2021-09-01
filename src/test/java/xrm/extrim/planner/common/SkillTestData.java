package xrm.extrim.planner.common;

import org.mapstruct.factory.Mappers;
import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.domain.Group;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.mapper.RateDescriptionMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class SkillTestData {
    private SkillTestData() {
    }

    public static Skill getSkill() {
        Skill skill = new Skill();
        skill.setId(1L);
        skill.setName("Name");
        skill.setDescription("Description");
        skill.setGroup(GroupTestData.getGroup());
        return skill;
    }

    public static Skill getSkill(Long id, String name, String description, Group group, List<RateDescription> descriptions) {
        Skill skill = new Skill();
        skill.setId(id);
        skill.setName(name);
        skill.setDescription(description);
        skill.setGroup(group);
        skill.setRateDescriptions(descriptions);
        return skill;
    }

    public static List<Skill> getSkills() {
        ArrayList<Skill> list = new ArrayList<>();
        for(int i = 0; i < 5 ;i++) {
            Skill skill = getSkill();
            skill.setId((long) i);
            list.add(skill);
        }
        return list;
    }

    public static SkillDto getSkillDro() {
        SkillDto skillDto = new SkillDto();
        skillDto.setName("Name");
        skillDto.setDescription("Description");
        skillDto.setGroupId(GroupTestData.getGroup().getId());
        return skillDto;
    }

    public static SkillDto getSkillDto(String name, Long groupId, String description, List<RateDescription> descriptions) {
        RateDescriptionMapper rateDescriptionMapper = Mappers.getMapper(RateDescriptionMapper.class);
        SkillDto skillDto = new SkillDto();
        skillDto.setName(name);
        skillDto.setGroupId(groupId);
        skillDto.setDescription(description);
        skillDto.setRateDescriptions(descriptions.stream().map(rateDescriptionMapper::rateDescriptionToRateDescriptionDto).collect(Collectors.toList()));
        return skillDto;
    }

}

package xrm.extrim.planner.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.service.GroupService;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SkillMapper {
    @Autowired
    RateDescriptionMapper rateDescriptionMapper;
    @Autowired
    GroupService groupService;

    @Mapping(target = "description", source = "skill.description")
    @Mapping(target = "groupId", source = "skill.group.id")
    public abstract SkillDto skillToSkillDto(Skill skill);

    @Mapping(target = "description", source = "skill.description")
    @Mapping(target = "groupId", source = "skill.group.id")
    public abstract List<SkillDto> skillToSkillDto(List<Skill> skill);

    public Skill skillDtoToSkill(SkillDto skillDto) {
        if (skillDto == null) {
            return null;
        }
        Skill skill = new Skill();
        skill.setName(skillDto.getName());
        skill.setDescription(skillDto.getDescription());
        skill.setRateDescriptions(rateDescriptionMapper.liseRateDescriptionDtoToListRateDescription(skillDto.getRateDescriptions()));
        for(RateDescription rateDescription: skill.getRateDescriptions()) {
            rateDescription.setSkill(skill);
        }
        skill.setGroup(groupService.getById(skillDto.getGroupId()));
        return skill;
    }
}

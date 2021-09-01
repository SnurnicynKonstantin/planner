package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.UserSkill;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserSkillMapper {
    @Mappings({
            @Mapping(target = "id", source = "userSkill.skill.id"),
            @Mapping(target = "name", source = "userSkill.skill.name"),
            @Mapping(target = "description", source = "userSkill.skill.description"),
            @Mapping(target = "rateDescriptions", source = "userSkill.skill.rateDescriptions")
    })
    UserSkillDto userSkillToUserSkillDto(UserSkill userSkill);

    @Mappings({
            @Mapping(target = "id", source = "userSkills.skill.id"),
            @Mapping(target = "name", source = "userSkills.skill.name"),
            @Mapping(target = "description", source = "userSkills.skill.description"),
            @Mapping(target = "rateDescriptions", source = "userSkills.skill.rateDescriptions")
    })
    List<UserSkillDto> listUserSkillToListUserSkillDto(List<UserSkill> userSkills);
}

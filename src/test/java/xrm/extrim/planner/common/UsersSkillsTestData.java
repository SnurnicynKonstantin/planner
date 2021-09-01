package xrm.extrim.planner.common;

import org.mapstruct.factory.Mappers;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.mapper.UserSkillMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions"})
public final class UsersSkillsTestData {

    private UsersSkillsTestData() {
    }

    public static List<UserSkill> getUserSkillList() {
        UserSkill userSkill1 = new UserSkill();
        userSkill1.setId(1L);
        userSkill1.setRate(5);
        UserSkill userSkill2 = new UserSkill();
        userSkill2.setId(2L);
        userSkill2.setRate(1);
        return new ArrayList<>(Arrays.asList(userSkill1, userSkill2));
    }

    public static List<UserSkillDto> getUserSkillDtoList() {
        UserSkillMapper userSkillMapper = Mappers.getMapper(UserSkillMapper.class);
        List<UserSkillDto> userSkillDtos =  userSkillMapper.listUserSkillToListUserSkillDto(getUserSkillList());
        for(UserSkillDto userSkillDto: userSkillDtos) {
            userSkillDto.setId(1L);
        }
        return userSkillDtos;
    }

    public static UserSkill getUserSkill() {
        UserSkill userSkill = new UserSkill();
        userSkill.setId(1L);
        userSkill.setRate(5);
        userSkill.setVersion(1);
        return userSkill;
    }
}

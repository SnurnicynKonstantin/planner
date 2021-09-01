package xrm.extrim.planner.service;

import org.springframework.stereotype.Service;
import xrm.extrim.planner.controller.dto.SkillDto;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.SkillMapper;
import xrm.extrim.planner.repository.RateDescriptionRepository;
import xrm.extrim.planner.repository.SkillRepository;
import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    private final RateDescriptionRepository rateDescriptionRepository;
    private final SkillMapper skillMapper;
    private final GroupService groupService;

    public SkillService(SkillRepository skillRepository, RateDescriptionRepository rateDescriptionRepository,
                        GroupService groupService, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.rateDescriptionRepository = rateDescriptionRepository;
        this.groupService = groupService;
        this.skillMapper = skillMapper;
    }

    public Skill getSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    //TODO разобраться, почему rateDescription не сохраняется вместе с skill
    public Skill createSkill(SkillDto skillDto) {
        if (skillRepository.findByName(skillDto.getName()) != null) {
            throw new PlannerException(String.format(Exception.SKILL_ALREADY_EXISTS.getDescription(), skillDto.getName()));        }
        Skill skill = skillMapper.skillDtoToSkill(skillDto);

        skill.setGroup(groupService.getById(skillDto.getGroupId()));
        List<RateDescription> rateDescriptions = skill.getRateDescriptions();
        skill.setRateDescriptions(null);

        Skill savedSkill = skillRepository.save(skill);
        rateDescriptions.forEach(rateDescription -> {
            rateDescription.setSkill(savedSkill);
            rateDescriptionRepository.save(rateDescription);
        });

        savedSkill.setRateDescriptions(rateDescriptions);
        return savedSkill;
    }

    public Skill updateSkill(Long id, SkillDto skillDto) {
        Skill skillFromDB = skillRepository.getById(id);
        skillFromDB.setName(skillDto.getName());
        skillFromDB.setDescription(skillDto.getDescription());
        skillFromDB.setGroup(groupService.getById(skillDto.getGroupId()));
        for(int i=0; i < 5; i++) {
            skillFromDB.getRateDescriptions().get(i).setDescription(skillDto.getRateDescriptions().get(i).getDescription());
        }
        return skillRepository.save(skillFromDB);
    }

    public void deleteSkillById(Long id) {
        if (skillRepository.findById(id).get().getUserSkills().isEmpty()) {
            skillRepository.deleteById(id);
        } else {
            throw new PlannerException(Exception.USERS_HAVE_SKILL.getDescription());
        }
    }
}

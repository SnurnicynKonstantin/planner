package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.controller.dto.RateDescriptionDto;
import xrm.extrim.planner.domain.RateDescription;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.RateDescriptionMapper;
import xrm.extrim.planner.repository.RateDescriptionRepository;
import xrm.extrim.planner.repository.SkillRepository;
import java.util.List;

@Service
public class RateDescriptionService {
    private final RateDescriptionRepository rateDescriptionRepository;
    private final SkillRepository skillRepository;
    private final RateDescriptionMapper rateDescriptionMapper;

    @Autowired
    public RateDescriptionService(RateDescriptionRepository rateDescriptionRepository, SkillRepository skillRepository, RateDescriptionMapper rateDescriptionMapper) {
        this.rateDescriptionRepository = rateDescriptionRepository;
        this.skillRepository = skillRepository;
        this.rateDescriptionMapper = rateDescriptionMapper;
    }

    public List<RateDescription> getAllRateDescriptions() {
        return rateDescriptionRepository.findAll();
    }

    public RateDescription getRateDescriptionById(Long id) {
        return rateDescriptionRepository.findById(id).orElse(null);
    }

    public RateDescription createRateDescription(RateDescriptionDto rateDescriptionDto) {
        if (rateDescriptionDto.getRateNumber() < 1 || rateDescriptionDto.getRateNumber() > 5) {
            throw new PlannerException(Exception.INCORRECT_RATE_NUMBER.getDescription());
        }
        if (!(rateDescriptionRepository.findBySkillIdAndRateNumber(rateDescriptionDto.getSkillId(), rateDescriptionDto.getRateNumber()).isEmpty())) {
            throw new PlannerException(String.format(Exception.RATE_DESCRIPTION_ALREADY_EXISTS.getDescription(), rateDescriptionDto.getRateNumber()));
        }
        Skill skillFromDB = skillRepository.findById(rateDescriptionDto.getSkillId()).orElse(null);
        if (skillFromDB == null) {
            throw new EntityNotFoundException(String.format(Exception.SKILL_NOT_FOUND.getDescription(), rateDescriptionDto.getSkillId()));
        }
        RateDescription rateDescription = rateDescriptionMapper.rateDescriptionDtoToRateDescription(rateDescriptionDto);
        skillFromDB.getRateDescriptions().add(rateDescription);
        rateDescription.setSkill(skillFromDB);
        return rateDescriptionRepository.save(rateDescription);
    }

    public RateDescription updateRateDescription(Long id, RateDescriptionDto rateDescriptionDto) {
        Skill skillFromDB = skillRepository.findById(rateDescriptionDto.getSkillId()).orElse(null);
        if (skillFromDB == null) {
            throw new EntityNotFoundException(String.format(Exception.SKILL_NOT_FOUND.getDescription(), rateDescriptionDto.getSkillId()));
        }
        RateDescription rateDescriptionFromDto = rateDescriptionMapper.rateDescriptionDtoToRateDescription(rateDescriptionDto);
        rateDescriptionFromDto.setId(id);
        skillFromDB.getRateDescriptions().stream().map(rateDescription ->
                rateDescription.getId().equals(rateDescriptionFromDto.getId()) ? rateDescriptionFromDto : rateDescription);
        Skill savedSkillFromDB = skillRepository.save(skillFromDB);
        rateDescriptionFromDto.setSkill(savedSkillFromDB);
        return rateDescriptionRepository.save(rateDescriptionFromDto);
    }

    public List<RateDescription> findBySkillId(Long id) {
        return rateDescriptionRepository.findBySkillId(id);
    }

    public void deleteRateDescriptionById(Long id) {
        rateDescriptionRepository.deleteById(id);
    }
}

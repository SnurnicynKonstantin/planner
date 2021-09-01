package xrm.extrim.planner.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.UserSkillDto;
import xrm.extrim.planner.domain.Skill;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.domain.UserSkill;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.EntityNotFoundException;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.repository.SkillRepository;
import xrm.extrim.planner.repository.UserRepository;
import xrm.extrim.planner.repository.UserSkillsRepository;

import java.util.List;
import java.util.Objects;

@Service
@SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"})
public class UserSkillService {
    private static final int START_VERSION_NUMBER = 1;

    private final UserRepository userRepository;
    private final UserSkillsRepository userSkillsRepository;
    private final SkillRepository skillRepository;
    private final UserService userService;
    private final SkillApprovedMessageService skillApprovedMessageService;

    @Autowired
    public UserSkillService(UserRepository userRepository, UserSkillsRepository userSkillsRepository, SkillRepository skillRepository, UserService userService, SkillApprovedMessageService skillApprovedMessageService) {
        this.userRepository = userRepository;
        this.userSkillsRepository = userSkillsRepository;
        this.skillRepository = skillRepository;
        this.userService = userService;
        this.skillApprovedMessageService = skillApprovedMessageService;
    }

    public UserSkill getLatestVersionUserSkill(Long userId, Long skillId) {
        Long userSkillId = userSkillsRepository.findLatestVersionIdBySkillIdAndUserId(userId, skillId);
        return userSkillId == null ? null : userSkillsRepository.getById(userSkillId);
    }

    public List<UserSkill> getAllSkillsWithVersionsById(Long id) {
        User user = getUserById(id);
        return user.getUserSkills();
    }

    public List<UserSkill> getUserSkill(Long userId) {
        User user = userService.getUser(userId);
        return user.getUserSkills();
    }

    public UserSkill updateUserSkill(Long userId, Long skillId, UserSkillDto userSkillDto) {
        checkUserAndSkillExist(userId, skillId);
        UserSkill lastVersionUserSkill = getLatestVersionUserSkill(userId, skillId);
        checkLastVersionUserSkill(lastVersionUserSkill, userId, skillId);

        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();

        UserSkill updatedUserSkill = new UserSkill();
        BeanUtils.copyProperties(lastVersionUserSkill, updatedUserSkill, "id", "version");
        updatedUserSkill.setRate(userSkillDto.getRate());
        updatedUserSkill.setComment(userSkillDto.getComment());
        updatedUserSkill.setConfirmed(userSkillDto.getConfirmed() && currentUser.getRole().isRateSkill());

        if (Objects.equals(lastVersionUserSkill.getRate(), updatedUserSkill.getRate()) &&
                lastVersionUserSkill.getComment().equals(updatedUserSkill.getComment()) &&
                lastVersionUserSkill.isConfirmed() == updatedUserSkill.isConfirmed()) {
            return updatedUserSkill;
        }
        updatedUserSkill.setVersion(lastVersionUserSkill.getVersion() + 1);

        if (skillApprovedMessageService.checkIfSend.test(lastVersionUserSkill, updatedUserSkill)) {
            skillApprovedMessageService.sendNotification(userId, currentUser, getSkillById(skillId));
        }

        return userSkillsRepository.save(updatedUserSkill);
    }

    public UserSkill setSkill(Long userId, UserSkillDto userSkillDto) {
        Skill skill = getSkillById(userSkillDto.getId());

        UserSkill lastVersionUserSkill = getLatestVersionUserSkill(userId, skill.getId());
        if (lastVersionUserSkill != null && !lastVersionUserSkill.isDeleted()) {
            return null;
        }

        User user = getUserById(userId);
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();
        UserSkill userSkill = new UserSkillBuilder()
                .setSkill(skill)
                .setComment(userSkillDto.getComment())
                .setUser(user)
                .setVersion(lastVersionUserSkill == null ? START_VERSION_NUMBER : lastVersionUserSkill.getVersion() + 1)
                .setRate(userSkillDto.getRate())
                .setIsConfirmed(userSkillDto.getConfirmed() && currentUser.getRole().isRateSkill())
                .build();

        userSkill = saveNewUserSkill(userSkill, user, skill);

        return userSkill;
    }

    public User setSkills(Long userId, List<UserSkillDto> userSkillsDto) {
        User user = getUserById(userId);
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();

        for (UserSkillDto skillDto : userSkillsDto) {
            Skill skill = getSkillById(skillDto.getId());

            UserSkill lastVersionUserSkill = getLatestVersionUserSkill(userId, skill.getId());
            if (lastVersionUserSkill != null && !lastVersionUserSkill.isDeleted()) {
                continue;
            }

            UserSkill userSkill = new UserSkillBuilder()
                    .setSkill(skill)
                    .setComment(skillDto.getComment())
                    .setIsConfirmed(skillDto.getConfirmed() && currentUser.getRole().isRateSkill())
                    .setUser(user)
                    .setVersion(lastVersionUserSkill == null ? START_VERSION_NUMBER : lastVersionUserSkill.getVersion() + 1)
                    .setRate(skillDto.getRate())
                    .build();

            saveNewUserSkill(userSkill, user, skill);
        }
        return userService.getUser(userId);
    }

    private UserSkill saveNewUserSkill(UserSkill userSkill, User user, Skill skill) {
        if (userSkill.getVersion() != START_VERSION_NUMBER) {
            return userSkillsRepository.save(userSkill);
        } else {
            if (user.getUserSkills().stream().noneMatch(x -> x.getSkill() == skill)) {
                return userSkillsRepository.save(userSkill);
            }
        }
        return null;
    }

    public User evaluateSkills(Long userId, List<UserSkillDto> userSkillsDto) {
        User currentUser = UserAuthenticationHelper.getAuthenticatedUserData();

        for (UserSkillDto skillDto : userSkillsDto) {
            Skill skill = getSkillById(skillDto.getId());

            UserSkill lastVersionUserSkill = getLatestVersionUserSkill(userId, skill.getId());
            if (lastVersionUserSkill == null) {
                throw new PlannerException(Exception.ADDING_SKILLS_IN_WRONG_METHOD.getDescription());
            }
            checkLastVersionUserSkill(lastVersionUserSkill, userId, skill.getId());

            UserSkill updatedUserSkill = new UserSkillBuilder()
                    .setSkill(lastVersionUserSkill.getSkill())
                    .setIsConfirmed(skillDto.getConfirmed() && currentUser.getRole().isRateSkill())
                    .setUser(lastVersionUserSkill.getUser())
                    .setVersion(lastVersionUserSkill.getVersion() + 1)
                    .setRate(skillDto.getRate())
                    .build();

            userSkillsRepository.save(updatedUserSkill);

            if (skillApprovedMessageService.checkIfSend.test(lastVersionUserSkill, updatedUserSkill)) {
                skillApprovedMessageService.sendNotification(userId, currentUser, skill);
            }
        }

        return userService.getUser(userId);
    }

    public User deleteSkill(Long userId, Long skillId) {
        checkUserAndSkillExist(userId, skillId);

        UserSkill latestVersionUserSkill = getLatestVersionUserSkill(userId, skillId);
        if (latestVersionUserSkill == null) {
            throw new PlannerException(String.format(Exception.USER_HAVE_NOT_SKILL.getDescription(), userId, skillId));
        }
        checkLastVersionUserSkill(latestVersionUserSkill, userId, skillId);

        latestVersionUserSkill.setDeleted(true);
        userSkillsRepository.save(latestVersionUserSkill);
        return userService.getUser(userId);
    }

    private void checkUserAndSkillExist(Long userId, Long skillId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), userId));
        }
        if (!skillRepository.existsById(skillId)) {
            throw new EntityNotFoundException(String.format(Exception.SKILL_NOT_FOUND.getDescription(), skillId));
        }
    }

    private void checkLastVersionUserSkill(UserSkill lastVersionUserSkill, Long userId, Long skillId) {
        if (lastVersionUserSkill == null) {
            throw new EntityNotFoundException(String.format(Exception.SKILL_NOT_FOUND.getDescription(), skillId));
        }
        if (lastVersionUserSkill.isDeleted()) {
            throw new PlannerException(String.format(Exception.SKILL_WAS_DELETED.getDescription(), userId, skillId));
        }
    }

    private User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException(String.format(Exception.USER_NOT_FOUND.getDescription(), id));
        }

        return user;
    }

    private Skill getSkillById(Long id) {
        Skill skill = skillRepository.findById(id).orElse(null);
        if (skill == null) {
            throw new EntityNotFoundException(String.format(Exception.SKILL_NOT_FOUND.getDescription(), id));
        }

        return skill;
    }
}

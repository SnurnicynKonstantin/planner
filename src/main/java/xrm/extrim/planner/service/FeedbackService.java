package xrm.extrim.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xrm.extrim.planner.common.UserAuthenticationHelper;
import xrm.extrim.planner.controller.dto.FeedbackDto;
import xrm.extrim.planner.domain.Feedback;
import xrm.extrim.planner.domain.User;
import xrm.extrim.planner.enums.Exception;
import xrm.extrim.planner.exception.PlannerException;
import xrm.extrim.planner.mapper.FeedbackMapper;
import xrm.extrim.planner.repository.FeedbackRepository;
import xrm.extrim.planner.repository.UserRepository;

import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.userRepository = userRepository;
    }

    public Feedback createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = feedbackMapper.feedbackDtoToFeedback(feedbackDto);
        if (feedback.getText() == null) {
            throw new PlannerException(Exception.REQUIRED_FIELD_EMPTY.getDescription());
        }
        if (feedback.getCreatorId() != null && !feedback.getCreatorId().equals(UserAuthenticationHelper.getAuthenticatedUserData().getId())) {
            throw new PlannerException(Exception.DOES_NOT_HAVE_PERMISSION.getDescription());
        }
        return feedbackRepository.save(feedback);
    }

    private List<FeedbackDto> setCreatorsNamesToFeedbacks(List<FeedbackDto> feedbackDtos) {
        feedbackDtos.forEach(feedbackDto -> {
            if (feedbackDto.getCreatorId() == null) {
                feedbackDto.setCreatorName("Аноним");
            } else {
                User user = userRepository.getById(feedbackDto.getCreatorId());
                feedbackDto.setCreatorName(user.getName() + " " + user.getSurname());
            }
        });
        return feedbackDtos;
    }

    public List<FeedbackDto> getAllFeedbacks() {
        List<FeedbackDto> feedbackDtos =  feedbackMapper.listFeedbacksToListFeedbackDtos(feedbackRepository.findByOrderByCreationDateDesc());
        return setCreatorsNamesToFeedbacks(feedbackDtos);
    }

    public List<FeedbackDto> getArchivedFeedbacks() {
        List<FeedbackDto> feedbackDtos = feedbackMapper.listFeedbacksToListFeedbackDtos(feedbackRepository.findByIsArchivedTrueOrderByCreationDateDesc());
        return setCreatorsNamesToFeedbacks(feedbackDtos);
    }

    public List<FeedbackDto> getNotArchivedFeedbacks() {
        List<FeedbackDto> feedbackDtos = feedbackMapper.listFeedbacksToListFeedbackDtos(feedbackRepository.findByIsArchivedFalseOrderByCreationDateDesc());
        return setCreatorsNamesToFeedbacks(feedbackDtos);
    }

    public void archiveFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            throw new PlannerException(String.format(Exception.FEEDBACK_NOT_FOUND.getDescription(), feedbackId));
        }
        feedback.setIsArchived(true);
        feedbackRepository.save(feedback);
    }
}

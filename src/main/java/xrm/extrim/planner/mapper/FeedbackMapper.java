package xrm.extrim.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xrm.extrim.planner.controller.dto.FeedbackDto;
import xrm.extrim.planner.domain.Feedback;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
@SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod"})
public abstract class FeedbackMapper {
    public Feedback feedbackDtoToFeedback(FeedbackDto feedbackDto) {
        if (feedbackDto == null) {
            return null;
        }

        Feedback feedback = new Feedback();
        if (feedbackDto.getCreatorId() > 0 && feedbackDto.getCreatorId() != null) {
            feedback.setCreatorId(feedbackDto.getCreatorId());
        } else {
            feedback.setCreatorId(null);
        }

        feedback.setCreationDate(LocalDate.now());
        feedback.setText(feedbackDto.getText());
        feedback.setIsArchived(false);
        return feedback;
    }

    @Mapping(target = "id", source = "feedbacks.id")
    public abstract List<FeedbackDto> listFeedbacksToListFeedbackDtos(List<Feedback> feedbacks);
}

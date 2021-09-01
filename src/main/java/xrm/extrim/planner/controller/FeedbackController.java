package xrm.extrim.planner.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xrm.extrim.planner.controller.dto.FeedbackDto;
import xrm.extrim.planner.domain.Feedback;
import xrm.extrim.planner.service.FeedbackService;

import java.util.List;

import static xrm.extrim.planner.common.RestUtils.UI_BASE_URL;

@RestController
@RequestMapping(UI_BASE_URL + "/feedback")
@Api(description = "Operations pertaining to feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @ApiOperation("Create feedback")
    public Feedback createFeedback(@ApiParam("Feedback information") @RequestBody FeedbackDto feedbackDto) {
        return feedbackService.createFeedback(feedbackDto);
    }

    @GetMapping
    @ApiOperation("Get all feedbacks")
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("archived")
    @ApiOperation("Get archived feedbacks")
    public List<FeedbackDto> getArchivedFeedbacks() {
        return feedbackService.getArchivedFeedbacks();
    }

    @GetMapping("not-archived")
    @ApiOperation("Get not archived feedbacks")
    public List<FeedbackDto> getNotArchivedFeedbacks() {
        return feedbackService.getNotArchivedFeedbacks();
    }

    @PutMapping("archive/{id}")
    @ApiOperation("Archive feedback")
    public void archiveFeedback(@ApiParam("Feedback id") @PathVariable(name = "id") Long feedbackId) {
        feedbackService.archiveFeedback(feedbackId);
    }
}

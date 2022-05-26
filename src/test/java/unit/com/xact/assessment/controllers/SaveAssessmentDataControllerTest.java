package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.SaveAssessmentDataController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.ParameterLevelAssessment;
import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SaveAssessmentDataControllerTest {

    private SaveAssessmentDataController saveAssessmentDataController;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private ModelMapper mapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        AnswerService answerService = mock(AnswerService.class);
        topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
        saveAssessmentDataController = new SaveAssessmentDataController(answerService, topicAndParameterLevelAssessmentService);
    }

    @Test
    void testSaveAssessmentAtTopicLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(RatingDto.ONE);
        topicRatingAndRecommendation.setRecommendation("some text");

        topicLevelAssessmentRequest.setTopicRatingAndRecommendation(topicRatingAndRecommendation);

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = saveAssessmentDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((TopicLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testSaveAssessmentAtParameterLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(RatingDto.ONE);
        parameterRatingAndRecommendation.setRecommendation("some text");

        parameterLevelAssessmentRequest.setParameterRatingAndRecommendation(parameterRatingAndRecommendation);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = saveAssessmentDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((ParameterLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}

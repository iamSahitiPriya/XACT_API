package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.QuestionDataController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Assessment;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class QuestionDataControllerTest {

    private QuestionDataController questionDataController;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private ModelMapper mapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        AnswerService answerService = mock(AnswerService.class);
        topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
        questionDataController = new QuestionDataController(answerService, topicAndParameterLevelAssessmentService);
    }

    @Test
    void testSaveAssessmentAtTopicLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        TopicLevelRatingDto topicLevelRatingDto = new TopicLevelRatingDto();
        topicLevelRatingDto.setTopicId(1);
        topicLevelRatingDto.setRating(RatingDto.ONE);
        topicLevelRatingDto.setRecommendation("some text");

        topicLevelAssessmentRequest.setTopicLevelRatingDto(topicLevelRatingDto);

        List<AnswerDto> answerDtoList = new ArrayList<>();

        AnswerDto answerDto1 = new AnswerDto(1, "some text");
        AnswerDto answerDto2 = new AnswerDto(2, "some more text");
        answerDtoList.add(answerDto1);
        answerDtoList.add(answerDto2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerDto(answerDtoList);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = questionDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((TopicLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testSaveAssessmentAtParameterLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        List<AnswerDto> answerDtoList = new ArrayList<>();

        AnswerDto answerDto1 = new AnswerDto(1, "some text");
        AnswerDto answerDto2 = new AnswerDto(2, "some more text");
        answerDtoList.add(answerDto1);
        answerDtoList.add(answerDto2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerDto(answerDtoList);

        ParameterLevelRatingDto parameterLevelRatingDto = new ParameterLevelRatingDto();
        parameterLevelRatingDto.setParameterId(1);
        parameterLevelRatingDto.setRating(RatingDto.ONE);
        parameterLevelRatingDto.setRecommendation("some text");

        parameterLevelAssessmentRequest.setParameterLevelRatingDto(parameterLevelRatingDto);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = questionDataController.saveAnswer(assessmentId, topicLevelAssessmentRequest);

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation((ParameterLevelAssessment) any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}

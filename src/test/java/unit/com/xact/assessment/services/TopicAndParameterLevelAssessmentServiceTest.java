package unit.com.xact.assessment.services;


import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TopicAndParameterLevelAssessmentServiceTest {

    private ModelMapper mapper = new ModelMapper();
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    private QuestionService questionService;
    private AnswerService answerService;

    private ParameterService parameterService;

    private TopicService topicService;

    @BeforeEach
    public void beforeEach() {
        questionService = mock(QuestionService.class);
        answerService = mock(AnswerService.class);
        parameterService = mock(ParameterService.class);
        topicService = mock(TopicService.class);
        topicAndParameterLevelAssessmentService = new TopicAndParameterLevelAssessmentService(parameterService, answerService, topicService, questionService);
    }

    @Test
    void shouldSaveAssessmentRatingAndRecommendationForTopicLevel() {
        Integer assessmentId = 1;

        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);

        topicRatingAndRecommendation.setRating(1);


        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendation("some text");
        topicLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("LOW");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);


        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicService.saveRatingAndRecommendation(topicLevelAssessment)).thenReturn(topicLevelAssessment);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);

        when(topicService.saveTopicLevelRecommendation(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        TopicLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());
        assertEquals(topicLevelRecommendation.getRecommendationEffort(), actualResponse1.getRecommendationEffort());

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());

    }


}


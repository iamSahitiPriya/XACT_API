package unit.com.xact.assessment.services;


import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.RecommendationDeliveryHorizon.*;
import static com.xact.assessment.models.RecommendationEffort.MEDIUM;
import static com.xact.assessment.models.RecommendationImpact.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
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
        topicLevelRecommendationRequest.setDeliveryHorizon(LATER);
        topicLevelRecommendationRequest.setImpact(HIGH);
        topicLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelRating topicLevelRating = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating.setTopicLevelId(topicLevelId);


        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicService.saveRatingAndRecommendation(topicLevelRating)).thenReturn(topicLevelRating);
        TopicLevelRating actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelRating);

        when(topicService.saveTopicLevelRecommendation(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        TopicLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);

        assertEquals(topicLevelRating.getRating(), actualResponse.getRating());
        assertEquals(topicLevelRating.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());
        assertEquals(topicLevelRecommendation.getRecommendationEffort(), actualResponse1.getRecommendationEffort());

        assertEquals(topicLevelRating.getRating(), actualResponse.getRating());
        assertEquals(topicLevelRating.getTopicLevelId(), actualResponse.getTopicLevelId());

    }

    @Test
    void shouldGetTopicRecommendations() {
        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", new Organisation(), Active, new Date(), new Date());

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation(1,assessment1,assessmentTopic,"recommendation",LOW,MEDIUM,NOW,new Date(),new Date());

        when(topicService.getTopicRecommendationByAssessmentId(assessment1.getAssessmentId())).thenReturn(Collections.singletonList(topicLevelRecommendation));

       List <TopicLevelRecommendation> topicLevelRecommendationList = topicAndParameterLevelAssessmentService.getTopicRecommendations(assessment1.getAssessmentId());

       assertEquals(1,topicLevelRecommendationList.size());
    }

    @Test
    void shouldGetParameterRecommendations() {
        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", new Organisation(), Active, new Date(), new Date());

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic1);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation(1,assessment1,assessmentParameter,"recommendation1",LOW,MEDIUM,NEXT,new Date(),new Date());

        when(parameterService.getParameterRecommendationByAssessmentId(assessment1.getAssessmentId())).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List <ParameterLevelRecommendation> parameterLevelRecommendationList = topicAndParameterLevelAssessmentService.getParameterRecommendations(assessment1.getAssessmentId());

        assertEquals(1,parameterLevelRecommendationList.size());
    }
}


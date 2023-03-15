package unit.com.xact.assessment.services;


import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.*;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static com.xact.assessment.dtos.RecommendationImpact.HIGH;
import static com.xact.assessment.dtos.RecommendationImpact.LOW;
import static com.xact.assessment.models.AssessmentStatus.Active;
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

    private static final ModelMapper modelMapper = new ModelMapper();

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


        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setRecommendationText("some text");
        recommendationRequest.setDeliveryHorizon(LATER);
        recommendationRequest.setImpact(HIGH);
        recommendationRequest.setEffort(RecommendationEffort.LOW);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelRating topicLevelRating = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating.setTopicLevelId(topicLevelId);


        TopicLevelRecommendation topicLevelRecommendation = mapper.map(recommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicService.saveTopicRating(topicLevelRating)).thenReturn(topicLevelRating);
        TopicLevelRating actualResponse = topicAndParameterLevelAssessmentService.saveTopicRating(topicLevelRating);

        assertEquals(topicLevelRating.getRating(), actualResponse.getRating());
        assertEquals(topicLevelRating.getTopicLevelId(), actualResponse.getTopicLevelId());
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

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation(assessmentTopic);
        topicLevelRecommendation.setRecommendationText("recommendation");
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setAssessment(assessment1);
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setDeliveryHorizon(LATER);

        when(topicService.getTopicRecommendations(assessment1.getAssessmentId())).thenReturn(Collections.singletonList(topicLevelRecommendation));

        List<TopicLevelRecommendation> topicLevelRecommendationList = topicAndParameterLevelAssessmentService.getTopicRecommendations(assessment1.getAssessmentId());

        assertEquals(1, topicLevelRecommendationList.size());
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

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation(assessmentParameter);
        parameterLevelRecommendation.setRecommendationText("recommendation");
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setAssessment(assessment1);
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setDeliveryHorizon(LATER);

        when(parameterService.getParameterRecommendations(assessment1.getAssessmentId())).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = topicAndParameterLevelAssessmentService.getParameterRecommendations(assessment1.getAssessmentId());

        assertEquals(1, parameterLevelRecommendationList.size());
    }

    @Test
    void shouldReturnTopicRecommendationAfterSaved() {
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        Assessment assessment = new Assessment();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationText("text");

        when(topicService.saveTopicRecommendation(recommendationRequest, assessment, 1)).thenReturn(topicLevelRecommendation);

        TopicLevelRecommendation topicLevelRecommendation1 = topicAndParameterLevelAssessmentService.saveTopicRecommendation(recommendationRequest, assessment, 1);

        assertEquals("text", topicLevelRecommendation1.getRecommendationText());
    }

    @Test
    void shouldReturnRecommendationTextWhenActivityTypeIsGiven() {
        when(topicService.getTopicRecommendationById(1)).thenReturn("text");

        String text = topicAndParameterLevelAssessmentService.getRecommendationById(1, ActivityType.TOPIC_RECOMMENDATION);

        assertEquals("text", text);
    }

    @Test
    void shouldReturnTopicRecommendation() {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationText("text");

        when(topicService.searchTopicRecommendation(1)).thenReturn(Optional.of(topicLevelRecommendation));

        Optional<TopicLevelRecommendation> topicLevelRecommendation1 = topicAndParameterLevelAssessmentService.searchTopicRecommendation(1);

        assertEquals("text", topicLevelRecommendation1.get().getRecommendationText());
    }


    @Test
    void shouldReturnParameterLevelRecommendation() {
       RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationText("text");
        parameterLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.HIGH);
        parameterLevelRecommendationRequest.setDeliveryHorizon(RecommendationDeliveryHorizon.LATER);

        Assessment assessment = new Assessment();


        ParameterLevelRecommendation parameterLevelRecommendation = modelMapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);

        when(topicAndParameterLevelAssessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, 1)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation parameterLevelRecommendation1 = topicAndParameterLevelAssessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, 1);

        assertEquals("text", parameterLevelRecommendation1.getRecommendationText());
    }
}


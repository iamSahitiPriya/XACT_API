package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.RecommendationImpact;
import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import com.xact.assessment.services.AssessmentTopicReferenceService;
import com.xact.assessment.services.TopicLevelRatingService;
import com.xact.assessment.services.TopicLevelRecommendationService;
import com.xact.assessment.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class TopicServiceTest {
    private ModelMapper mapper = new ModelMapper();
    private TopicService topicService;
    private AssessmentTopicRepository assessmentTopicRepository;

    private  AssessmentTopicReferenceService assessmentTopicReferenceService;

    private TopicLevelRatingService topicLevelRatingService;
    private TopicLevelRecommendationService topicLevelRecommendationService;

    @BeforeEach
    public void beforeEach() {
        assessmentTopicRepository = mock(AssessmentTopicRepository.class);
        assessmentTopicReferenceService=mock(AssessmentTopicReferenceService.class);
        topicLevelRatingService =mock(TopicLevelRatingService.class);
        topicLevelRecommendationService=mock(TopicLevelRecommendationService.class);
        topicService = new TopicService(assessmentTopicRepository, assessmentTopicReferenceService,  topicLevelRatingService, topicLevelRecommendationService);
    }

    @Test
    void shouldGetDetailsForParticularTopicId() {
        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.findById(topicId)).thenReturn(Optional.of(assessmentTopic));
        Optional<AssessmentTopic> actualTopic = topicService.getTopic(topicId);

        assertEquals(assessmentTopic.getTopicId(), actualTopic.get().getTopicId());
        assertEquals(assessmentTopic.getTopicName(), actualTopic.get().getTopicName());

    }

    @Test
    void shouldSaveTopicsWhenInputIsGiven() {
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.save(assessmentTopic)).thenReturn(assessmentTopic);
        topicService.createTopic(assessmentTopic);

        verify(assessmentTopicRepository).save(assessmentTopic);
    }

    @Test
    void shouldUpdateTopic() {
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.update(assessmentTopic)).thenReturn(assessmentTopic);
        topicService.updateTopic(assessmentTopic);

        verify(assessmentTopicRepository).update(assessmentTopic);

    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;

        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);

        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setRecommendationId(1);
        recommendationRequest.setRecommendationText("some text");
        recommendationRequest.setDeliveryHorizon(LATER);
        recommendationRequest.setImpact(RecommendationImpact.HIGH);
        recommendationRequest.setEffort(RecommendationEffort.MEDIUM);


        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);


        topicLevelRatingService.save(topicLevelRating1);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(recommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        List<TopicLevelRecommendation> topicLevelRecommendationList = Collections.singletonList(topicLevelRecommendation);
        when(topicLevelRatingService.existsByID(topicLevelRating1)).thenReturn(true);
        when(topicLevelRatingService.save(topicLevelRating1)).thenReturn(topicLevelRating1);
        TopicLevelRating actualResponse = topicService.saveTopicRating(topicLevelRating1);
        when(topicLevelRecommendationService.existsById(1)).thenReturn(true);


        assertEquals(topicLevelRating1.getRating(), actualResponse.getRating());
        assertEquals(topicLevelRating1.getTopicLevelId(), actualResponse.getTopicLevelId());
    }


    @Test
    void shouldDeleteRatingForTopicLevelAssessment() {
        Integer assessmentId1 = 1;
        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);


        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setRecommendationId(1);
        recommendationRequest.setRecommendationText("text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);

        topicLevelRatingService.save(topicLevelRating1);

        topicLevelRating1.setRating(null);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(recommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationText(null);

        when(topicLevelRatingService.existsByID(topicLevelRating1)).thenReturn(true);
        topicService.saveTopicRating(topicLevelRating1);

        verify(topicLevelRatingService).delete(topicLevelRating1);
    }

    @Test
    void shouldDeleteRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;
        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(topicId);
        topicRatingAndRecommendation.setRating(1);

        RecommendationRequest recommendationRequest = new RecommendationRequest();
        recommendationRequest.setRecommendationId(2);
        recommendationRequest.setRecommendationText("some text");
        recommendationRequest.setDeliveryHorizon(LATER);
        recommendationRequest.setImpact(RecommendationImpact.HIGH);
        recommendationRequest.setEffort(RecommendationEffort.LOW);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        TopicLevelRecommendation topicLevelRecommendation = mapper.map(recommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        doNothing().when(topicLevelRecommendationService).deleteById(recommendationRequest.getRecommendationId());

        topicLevelRecommendationService.deleteById(recommendationRequest.getRecommendationId());

        verify(topicLevelRecommendationService).deleteById(recommendationRequest.getRecommendationId());
    }

    @Test
    void shouldReturnTopicRecommendationWhenRecommendationIdIsGiven() {
        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationText("text");
        when(topicLevelRecommendationService.findById(1)).thenReturn(Optional.of(topicLevelRecommendation1));

        Optional<TopicLevelRecommendation> topicLevelRecommendation = topicService.searchTopicRecommendation(1);

        assertEquals("text",topicLevelRecommendation.get().getRecommendationText());
    }

    @Test
    void shouldReturnRecommendationTextWhenRecommendationIdIsGiven() {
        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationText("text");
        when(topicLevelRecommendationService.findById(1)).thenReturn(Optional.of(topicLevelRecommendation1));

        String recommendation = topicService.getTopicRecommendationById(1);
        
        assertEquals("text",recommendation);
    }

    @Test
    void name() {
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendationList.add(topicLevelRecommendation1);

        when(topicLevelRecommendationService.findByAssessment(1)).thenReturn(topicLevelRecommendationList);

        List<TopicLevelRecommendation> topicLevelRecommendationList1 = topicService.getTopicRecommendations(1);

        assertEquals(1,topicLevelRecommendationList1.size());
    }

    @Test
    void shouldReturnRecommendationAfterSaved() {
        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationText("text");
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        Assessment assessment = new Assessment();
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        when(topicLevelRecommendationService.saveTopicRecommendation(recommendationRequest,assessment,assessmentTopic)).thenReturn(topicLevelRecommendation1);
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation = topicService.saveTopicRecommendation(recommendationRequest,assessment,assessmentTopic.getTopicId());

        assertEquals("text",topicLevelRecommendation.getRecommendationText());
    }

    @Test
    void shouldGetTopicRecommendationById() {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationText("This is a recommendation");

        when(topicLevelRecommendationService.findById(any(Integer.class))).thenReturn(Optional.of(topicLevelRecommendation));

        topicService.getTopicRecommendationById(1);

        verify(topicLevelRecommendationService).findById(any(Integer.class));
    }
}



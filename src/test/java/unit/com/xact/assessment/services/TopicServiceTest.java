package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentTopicReferenceRepository;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import com.xact.assessment.repositories.TopicLevelRatingRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import com.xact.assessment.services.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class TopicServiceTest {
    private ModelMapper mapper = new ModelMapper();
    private TopicService topicService;
    private AssessmentTopicRepository assessmentTopicRepository;

    private  AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;

    private TopicLevelRatingRepository topicLevelRatingRepository;
    private TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    @BeforeEach
    public void beforeEach() {
        assessmentTopicRepository = mock(AssessmentTopicRepository.class);
        assessmentTopicReferenceRepository=mock(AssessmentTopicReferenceRepository.class);
        topicLevelRatingRepository =mock(TopicLevelRatingRepository.class);
        topicLevelRecommendationRepository=mock(TopicLevelRecommendationRepository.class);
        topicService = new TopicService(assessmentTopicRepository, assessmentTopicReferenceRepository, topicLevelRatingRepository, topicLevelRatingService, topicLevelRecommendationRepository);
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

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setRecommendation("some text");
        topicLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("LOW");


        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);

        topicLevelRatingRepository.save(topicLevelRating1);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        topicLevelRecommendationRepository.save(topicLevelRecommendation);

        List<TopicLevelRecommendation> topicLevelRecommendationList = Collections.singletonList(topicLevelRecommendation);
        when(topicLevelRatingRepository.existsById(topicLevelId1)).thenReturn(true);
        when(topicLevelRatingRepository.update(topicLevelRating1)).thenReturn(topicLevelRating1);
        TopicLevelRating actualResponse = topicService.saveRatingAndRecommendation(topicLevelRating1);
        when(topicService.getTopicAssessmentRecommendationData(assessmentId1,topicId)).thenReturn(topicLevelRecommendationList);
        when(topicLevelRecommendationRepository.existsById(1)).thenReturn(true);


        when(topicLevelRecommendationRepository.existsById(topicLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(topicLevelRecommendationRepository.update(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        TopicLevelRecommendation actualResponse1 = topicService.saveTopicLevelRecommendation(topicLevelRecommendation);


        assertEquals(topicLevelRating1.getRating(), actualResponse.getRating());
        assertEquals(topicLevelRating1.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());
        assertEquals(topicLevelRecommendation.getRecommendationImpact(), actualResponse1.getRecommendationImpact());

    }




    @Test
    void shouldDeleteRatingForTopicLevelAssessment() {
        Integer assessmentId1 = 1;
        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);


        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setRecommendation("text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);

        topicLevelRatingRepository.save(topicLevelRating1);

        topicLevelRating1.setRating(null);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        topicLevelRecommendationRepository.save(topicLevelRecommendation);

        topicLevelRecommendation.setRecommendation(null);

        when(topicLevelRatingRepository.existsById(topicLevelId1)).thenReturn(true);
        topicService.saveRatingAndRecommendation(topicLevelRating1);

        verify(topicLevelRatingRepository).delete(topicLevelRating1);

        when(topicLevelRecommendationRepository.existsById(topicLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        topicService.saveTopicLevelRecommendation(topicLevelRecommendation);

        verify(topicLevelRecommendationRepository).delete(topicLevelRecommendation);
    }

    @Test
    void shouldDeleteRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;
        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(topicId);
        topicRatingAndRecommendation.setRating(1);

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(2);
        topicLevelRecommendationRequest.setRecommendation("some text");
        topicLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("LOW");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);


        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        doNothing().when(topicLevelRecommendationRepository).deleteById(topicLevelRecommendationRequest.getRecommendationId());


        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        topicLevelRecommendationRepository.deleteById(topicLevelRecommendationRequest.getRecommendationId());

        verify(topicLevelRecommendationRepository).deleteById(topicLevelRecommendationRequest.getRecommendationId());
    }


}



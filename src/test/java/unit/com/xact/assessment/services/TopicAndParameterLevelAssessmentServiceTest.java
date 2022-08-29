package unit.com.xact.assessment.services;


import com.xact.assessment.dtos.ParameterLevelRecommendationRequest;
import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TopicAndParameterLevelAssessmentServiceTest {

    private ModelMapper mapper = new ModelMapper();
    private TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private AnswerService answerService;

    private TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    private ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    @BeforeEach
    public void beforeEach() {
        topicLevelAssessmentRepository = mock(TopicLevelAssessmentRepository.class);
        parameterLevelAssessmentRepository = mock(ParameterLevelAssessmentRepository.class);
        answerService = mock(AnswerService.class);
        topicLevelRecommendationRepository = mock(TopicLevelRecommendationRepository.class);
        parameterLevelRecommendationRepository=mock(ParameterLevelRecommendationRepository.class);
        topicAndParameterLevelAssessmentService = new TopicAndParameterLevelAssessmentService(topicLevelAssessmentRepository, parameterLevelAssessmentRepository, topicLevelRecommendationRepository, parameterLevelRecommendationRepository, answerService);
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

        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);

        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        TopicLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());
        assertEquals(topicLevelRecommendation.getRecommendationEffort(), actualResponse1.getRecommendationEffort());

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());

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
        TopicLevelAssessment topicLevelAssessment1 = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment1.setTopicLevelId(topicLevelId1);

        topicLevelAssessmentRepository.save(topicLevelAssessment1);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        topicLevelRecommendationRepository.save(topicLevelRecommendation);


        when(topicLevelAssessmentRepository.existsById(topicLevelId1)).thenReturn(true);
        when(topicLevelAssessmentRepository.update(topicLevelAssessment1)).thenReturn(topicLevelAssessment1);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment1);


        when(topicLevelRecommendationRepository.existsById(topicLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(topicLevelRecommendationRepository.update(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        TopicLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);


        assertEquals(topicLevelAssessment1.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment1.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());
        assertEquals(topicLevelRecommendation.getRecommendationImpact(), actualResponse1.getRecommendationImpact());

    }

    @Test()
    void shouldSaveAssessmentRatingAndRecommendationForParameterLevel() {
        Integer assessmentId = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(parameterId);

        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other text");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);

        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);


        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);

        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);


        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);


        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationRepository.update(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

    }

    @Test
    void shouldReturnParameterAssessmentData() {
        Integer assessmentId = 1;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");
        ParameterLevelId parameterLevelId = new ParameterLevelId();
        parameterLevelId.setAssessment(assessment);
        parameterLevelId.setParameter(assessmentParameter);

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessment.setRating(2);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon("some dummy text");
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);

        when(parameterLevelAssessmentRepository.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelAssessment));

        List<ParameterLevelAssessment> parameterLevelAssessmentList = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);

        when(parameterLevelRecommendationRepository.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<ParameterLevelRecommendation> parameterLevelRecommendationList=topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId);

        assertEquals(parameterLevelAssessmentList.get(0).getRating(), parameterLevelAssessment.getRating());
        assertEquals(parameterLevelRecommendationList.get(0).getRecommendation(), parameterLevelRecommendation.getRecommendation());
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
        TopicLevelAssessment topicLevelAssessment1 = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment1.setTopicLevelId(topicLevelId1);

        topicLevelAssessmentRepository.save(topicLevelAssessment1);

        topicLevelAssessment1.setRating(null);

        TopicLevelRecommendation topicLevelRecommendation = mapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment1);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        topicLevelRecommendation.setTopic(assessmentTopic);

        topicLevelRecommendationRepository.save(topicLevelRecommendation);

        topicLevelRecommendation.setRecommendation(null);

        when(topicLevelAssessmentRepository.existsById(topicLevelId1)).thenReturn(true);
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment1);

        verify(topicLevelAssessmentRepository).delete(topicLevelAssessment1);

        when(topicLevelRecommendationRepository.existsById(topicLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);

        verify(topicLevelRecommendationRepository).delete(topicLevelRecommendation);
    }

    @Test
    void shouldDeleteRatingForParameterLevelAssessment(){
        Integer assessmentId1 = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation1 = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation1.setParameterId(1);
        parameterRatingAndRecommendation1.setRating(1);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation1, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation1, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);

        parameterLevelAssessment.setRating(null);

        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);

        verify(parameterLevelAssessmentRepository).delete(parameterLevelAssessment);
    }

    @Test
    void shouldDeleteRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;
        Integer parameterId = 1;
        Integer recommendationId=1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(recommendationId);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");


        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        doNothing().when(parameterLevelRecommendationRepository).deleteById(parameterLevelRecommendationRequest.getRecommendationId());


        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        parameterLevelRecommendationRepository.deleteById(parameterLevelRecommendationRequest.getRecommendationId());

       verify(parameterLevelRecommendationRepository).deleteById(parameterLevelRecommendationRequest.getRecommendationId());
    }

    @Test
    void shouldDeleteRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;
        Integer topicId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(topicId);

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
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

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevelWhenRecommendationIsEAmpty() {

        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendation("");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");
        parameterLevelRecommendationRequest.setImpact("");
        parameterLevelRecommendationRequest.setEffort("");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);

        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);


        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);


        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationRepository.update(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

    }

}


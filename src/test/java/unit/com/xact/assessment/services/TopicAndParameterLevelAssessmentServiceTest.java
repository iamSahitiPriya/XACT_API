package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.services.AnswerService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TopicAndParameterLevelAssessmentServiceTest {

    private ModelMapper mapper = new ModelMapper();
    private TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private AnswerService answerService;

    @BeforeEach
    public void beforeEach() {
        topicLevelAssessmentRepository = mock(TopicLevelAssessmentRepository.class);
        parameterLevelAssessmentRepository = mock(ParameterLevelAssessmentRepository.class);
        answerService = mock(AnswerService.class);
        topicAndParameterLevelAssessmentService = new TopicAndParameterLevelAssessmentService(topicLevelAssessmentRepository, parameterLevelAssessmentRepository, answerService);
    }

    @Test
    void shouldSaveAssessmentRatingAndRecommendationForTopicLevel() {
        Integer assessmentId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);

        topicRatingAndRecommendation.setRating(1);
        topicRatingAndRecommendation.setRecommendation("some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);

        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelAssessment.getRecommendation(), actualResponse.getRecommendation());
    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);
        topicRatingAndRecommendation.setRecommendation("some text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelAssessment topicLevelAssessment1 = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment1.setTopicLevelId(topicLevelId1);

        topicLevelAssessmentRepository.save(topicLevelAssessment1);

        topicLevelAssessment1.setRecommendation("new recommendation");

        when(topicLevelAssessmentRepository.existsById(topicLevelId1)).thenReturn(true);
        when(topicLevelAssessmentRepository.update(topicLevelAssessment1)).thenReturn(topicLevelAssessment1);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment1);

        assertEquals(topicLevelAssessment1.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment1.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelAssessment1.getRecommendation(), actualResponse.getRecommendation());

    }

    @Test()
    void shouldSaveAssessmentRatingAndRecommendationForParameterLevel() {
        Integer assessmentId = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);

        parameterRatingAndRecommendation.setRating(1);
        parameterRatingAndRecommendation.setRecommendation("some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);


        System.out.println(parameterLevelAssessment.parameterLevelId);
        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelAssessment.getRecommendation(), actualResponse.getRecommendation());
    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation1 = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation1.setParameterId(1);
        parameterRatingAndRecommendation1.setRating(1);
        parameterRatingAndRecommendation1.setRecommendation("some text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation1, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation1, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);

        parameterLevelAssessment.setRecommendation("newRecommendation");

        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);

        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelAssessment.getRecommendation(), actualResponse.getRecommendation());

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
        parameterLevelAssessment.setRecommendation("Hello");

        when(parameterLevelAssessmentRepository.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelAssessment));
        List<ParameterLevelAssessment> parameterLevelAssessmentList = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);

        assertEquals(parameterLevelAssessmentList.get(0).getRecommendation(),parameterLevelAssessment.getRecommendation());
    }


}

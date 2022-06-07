package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.RatingDto;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopicAndParameterLevelAssessmentServiceTest {

    private ModelMapper mapper = new ModelMapper();
    private TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;

    @BeforeEach
    public void beforeEach() {
        topicLevelAssessmentRepository = mock(TopicLevelAssessmentRepository.class);
        parameterLevelAssessmentRepository = mock(ParameterLevelAssessmentRepository.class);
        topicAndParameterLevelAssessmentService = new TopicAndParameterLevelAssessmentService(topicLevelAssessmentRepository, parameterLevelAssessmentRepository);
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

        Integer assessmentId2 = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation1 = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation1.setTopicId(1);
        topicRatingAndRecommendation1.setRating(2);
        topicRatingAndRecommendation1.setRecommendation("some update text");

        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(assessmentId2);

        TopicLevelId topicLevelId2 = mapper.map(topicRatingAndRecommendation1, TopicLevelId.class);
        topicLevelId2.setAssessment(assessment2);
        TopicLevelAssessment topicLevelAssessment2 = mapper.map(topicRatingAndRecommendation1, TopicLevelAssessment.class);
        topicLevelAssessment2.setTopicLevelId(topicLevelId2);


        when(topicLevelAssessmentRepository.update(topicLevelAssessment2)).thenReturn(topicLevelAssessment2);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment2);

        assertEquals(topicLevelAssessment2.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment2.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelAssessment2.getRecommendation(), actualResponse.getRecommendation());

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

        Integer assessmentId2 = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation2 = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation2.setParameterId(1);
        parameterRatingAndRecommendation2.setRating(2);
        parameterRatingAndRecommendation2.setRecommendation("some update text");

        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(assessmentId2);

        ParameterLevelId parameterLevelId1 = mapper.map(parameterRatingAndRecommendation2, ParameterLevelId.class);
        parameterLevelId1.setAssessment(assessment2);
        ParameterLevelAssessment parameterLevelAssessment1 = mapper.map(parameterRatingAndRecommendation2, ParameterLevelAssessment.class);
        parameterLevelAssessment1.setParameterLevelId(parameterLevelId1);


        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment1)).thenReturn(parameterLevelAssessment1);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment1);

        assertEquals(parameterLevelAssessment1.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment1.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelAssessment1.getRecommendation(), actualResponse.getRecommendation());

    }
}

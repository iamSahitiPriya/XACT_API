package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterLevelRatingDto;
import com.xact.assessment.dtos.RatingDto;
import com.xact.assessment.dtos.TopicLevelRatingDto;
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
    void shouldSaveAssessmentRatingAndRecommendationForTopicLevel(){
        Integer assessmentId = 1;
        TopicLevelRatingDto topicLevelRatingDto = new TopicLevelRatingDto();
        topicLevelRatingDto.setTopicId(1);

        topicLevelRatingDto.setRating(RatingDto.ONE);
        topicLevelRatingDto.setRecommendation("some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        TopicLevelId topicLevelId = mapper.map(topicLevelRatingDto, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicLevelRatingDto, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);

        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);

        assertEquals(topicLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelAssessment.getRecommendation(),actualResponse.getRecommendation());
    }
    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForTopicLevel() {

        Integer assessmentId1 = 1;
        TopicLevelRatingDto topicLevelRatingDto  = new TopicLevelRatingDto();
        topicLevelRatingDto.setTopicId(1);
        topicLevelRatingDto.setRating(RatingDto.ONE);
        topicLevelRatingDto.setRecommendation("some text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        TopicLevelId topicLevelId1 = mapper.map(topicLevelRatingDto, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelAssessment topicLevelAssessment1 = mapper.map(topicLevelRatingDto, TopicLevelAssessment.class);
        topicLevelAssessment1.setTopicLevelId(topicLevelId1);

        topicLevelAssessmentRepository.save(topicLevelAssessment1);

        Integer assessmentId2 = 1;
        TopicLevelRatingDto topicLevelRatingDto1 = new TopicLevelRatingDto();
        topicLevelRatingDto1.setTopicId(1);
        topicLevelRatingDto1.setRating(RatingDto.TWO);
        topicLevelRatingDto1.setRecommendation("some update text");

        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(assessmentId2);

        TopicLevelId topicLevelId2 = mapper.map(topicLevelRatingDto1, TopicLevelId.class);
        topicLevelId2.setAssessment(assessment2);
        TopicLevelAssessment topicLevelAssessment2 = mapper.map(topicLevelRatingDto1, TopicLevelAssessment.class);
        topicLevelAssessment2.setTopicLevelId(topicLevelId2);


        when(topicLevelAssessmentRepository.update(topicLevelAssessment2)).thenReturn(topicLevelAssessment2);
        TopicLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment2);

        assertEquals(topicLevelAssessment2.getRating(), actualResponse.getRating());
        assertEquals(topicLevelAssessment2.getTopicLevelId(), actualResponse.getTopicLevelId());
        assertEquals(topicLevelAssessment2.getRecommendation(),actualResponse.getRecommendation());

    }

    @Test()
    void shouldSaveAssessmentRatingAndRecommendationForParameterLevel(){
        Integer assessmentId = 1;
        ParameterLevelRatingDto parameterLevelRatingDto = new ParameterLevelRatingDto();
        parameterLevelRatingDto.setParameterId(1);

        parameterLevelRatingDto.setRating(RatingDto.ONE);
        parameterLevelRatingDto.setRecommendation("some text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        ParameterLevelId parameterLevelId = mapper.map(parameterLevelRatingDto, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterLevelRatingDto, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);


        System.out.println(parameterLevelAssessment.parameterLevelId);
        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelAssessment.getRecommendation(),actualResponse.getRecommendation());
    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;
        ParameterLevelRatingDto parameterLevelRatingDto1 = new ParameterLevelRatingDto();
        parameterLevelRatingDto1.setParameterId(1);
        parameterLevelRatingDto1.setRating(RatingDto.ONE);
        parameterLevelRatingDto1.setRecommendation("some text");

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        ParameterLevelId parameterLevelId = mapper.map(parameterLevelRatingDto1, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterLevelRatingDto1, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);

        Integer assessmentId2 = 1;
        ParameterLevelRatingDto parameterLevelRatingDto2 = new ParameterLevelRatingDto();
        parameterLevelRatingDto2.setParameterId(1);
        parameterLevelRatingDto2.setRating(RatingDto.TWO);
        parameterLevelRatingDto2.setRecommendation("some update text");

        Assessment assessment2 = new Assessment();
        assessment2.setAssessmentId(assessmentId2);

        ParameterLevelId parameterLevelId1 = mapper.map(parameterLevelRatingDto2, ParameterLevelId.class);
        parameterLevelId1.setAssessment(assessment2);
        ParameterLevelAssessment parameterLevelAssessment1 = mapper.map(parameterLevelRatingDto2, ParameterLevelAssessment.class);
        parameterLevelAssessment1.setParameterLevelId(parameterLevelId1);


        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment1)).thenReturn(parameterLevelAssessment1);
        ParameterLevelAssessment actualResponse = topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment1);

        assertEquals(parameterLevelAssessment1.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment1.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelAssessment1.getRecommendation(),actualResponse.getRecommendation());

    }
}

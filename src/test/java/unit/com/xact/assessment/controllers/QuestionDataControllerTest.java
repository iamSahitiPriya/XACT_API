//package unit.com.xact.assessment.controllers;
//
//import com.xact.assessment.controllers.QuestionDataController;
//import com.xact.assessment.dtos.AnswerDto;
//import com.xact.assessment.dtos.RatingAndRecommendationDto;
//import com.xact.assessment.dtos.RatingDto;
//import com.xact.assessment.dtos.TopicLevelAssessmentRequest;
//import com.xact.assessment.models.Assessment;
//import com.xact.assessment.services.AnswerService;
//import com.xact.assessment.services.TopicLevelAssessmentService;
//import io.micronaut.http.HttpResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.*;
//
//public class QuestionDataControllerTest {
//
//    private QuestionDataController questionDataController;
//    private TopicLevelAssessmentService topicLevelAssessmentService;
//    private ModelMapper mapper = new ModelMapper();
//
//
//    @BeforeEach
//    public void beforeEach() {
//        AnswerService answerService = mock(AnswerService.class);
//        topicLevelAssessmentService = mock(TopicLevelAssessmentService.class);
//        questionDataController = new QuestionDataController(answerService, topicLevelAssessmentService);
//    }
//
//    @Test
//    void testSaveAssessmentNotes() {
//        Integer assessmentId = 1;
//        List<TopicLevelAssessmentRequest> topicLevelAssessmentRequests = new ArrayList<>(List.of(new TopicLevelAssessmentRequest()));
//
//        AnswerDto answerDto1 = new AnswerDto(1, "some text");
//
//        RatingAndRecommendationDto ratingAndRecommendationDto1 = new RatingAndRecommendationDto();
//        ratingAndRecommendationDto1.setTopicId(1);
//        ratingAndRecommendationDto1.setRating(RatingDto.ONE);
//        ratingAndRecommendationDto1.setRecommendation("some text");
//
//        TopicLevelAssessmentRequest topicLevelAssessmentRequest1 = new TopicLevelAssessmentRequest(answerDto1,ratingAndRecommendationDto1);
//        topicLevelAssessmentRequests.add(topicLevelAssessmentRequest1);
//
//        AnswerDto answerDto2 = new AnswerDto(2, "some more text");
//
//        RatingAndRecommendationDto ratingAndRecommendationDto2 = new RatingAndRecommendationDto();
//        ratingAndRecommendationDto2.setTopicId(1);
//        ratingAndRecommendationDto2.setRating(RatingDto.ONE);
//        ratingAndRecommendationDto2.setRecommendation("some more text");
//
//        TopicLevelAssessmentRequest topicLevelAssessmentRequest2 = new TopicLevelAssessmentRequest(answerDto2,ratingAndRecommendationDto2);
//        topicLevelAssessmentRequests.add(topicLevelAssessmentRequest2);
//
////        List<Answer> answers = new ArrayList<>();
//
//        HttpResponse<AnswerDto> actualResponse = questionDataController.saveAnswer(assessmentId, topicLevelAssessmentRequests);
//
////        for (AnswerDto answerDto : answerDtoList) {
////            assessment.setAssessmentId(assessmentId);
////            AnswerId answerId = mapper.map(answerDto, AnswerId.class);
////            answerId.setAssessment(assessment);
////            Answer answer = mapper.map(answerDto, Answer.class);
////            answer.setAnswerId(answerId);
////            when(answerService.saveAnswer(answer)).thenReturn(answer);
////            answers.add(answer);
////        }
//
////        doNothing().when(topicLevelAssessmentService.saveRatingAndRecommendation(any()));
////        verify(topicLevelAssessmentService).saveRatingAndRecommendation(any());
//        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
//    }
//}

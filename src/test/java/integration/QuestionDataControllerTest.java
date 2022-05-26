//package integration;
//
//import au.com.dius.pact.core.model.Response;
//import com.xact.assessment.dtos.AnswerDto;
//import com.xact.assessment.dtos.RatingDto;
//import com.xact.assessment.dtos.TopicLevelRatingDto;
//import com.xact.assessment.models.*;
//import com.xact.assessment.repositories.AnswerRepository;
//import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
//import io.micronaut.http.HttpRequest;
//import io.micronaut.http.HttpResponse;
//import io.micronaut.http.client.HttpClient;
//import io.micronaut.http.client.annotation.Client;
//import io.micronaut.test.annotation.MockBean;
//import jakarta.inject.Inject;
//import org.junit.jupiter.api.Test;
//import org.modelmapper.ModelMapper;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class QuestionDataControllerTest {
//
//
//    private ModelMapper mapper = new ModelMapper();
//
//    @Inject
//    @Client("/")
//    HttpClient client; //
//
//    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
//
//    @Inject
//    TopicLevelAssessmentRepository topicLevelAssessmentRepository;
//
//    @Inject
//    AnswerRepository answerRepository;
//
//    @MockBean(TopicLevelAssessmentRepository.class)
//    TopicLevelAssessmentRepository topicLevelAssessmentRepository() {
//        return mock(TopicLevelAssessmentRepository.class);
//    }
//
//    @MockBean(AnswerRepository.class)
//    AnswerRepository answerRepository() {
//        return mock(AnswerRepository.class);
//    }
//
//
//    @Test
//    void testSaveTopicLevelAssessmentResponse() throws IOException {
//
//        Integer assessmentId = 1;
//        TopicLevelRatingDto topicLevelRatingDto = new TopicLevelRatingDto();
//        topicLevelRatingDto.setTopicId(1);
//
//        topicLevelRatingDto.setRating(RatingDto.ONE);
//        topicLevelRatingDto.setRecommendation("text");
//
//        Assessment assessment = new Assessment();
//        assessment.setAssessmentId(assessmentId);
//
//        TopicLevelId topicLevelId = mapper.map(topicLevelRatingDto, TopicLevelId.class);
//        topicLevelId.setAssessment(assessment);
//        TopicLevelAssessment topicLevelAssessment = mapper.map(topicLevelRatingDto, TopicLevelAssessment.class);
//        topicLevelAssessment.setTopicLevelId(topicLevelId);
//
//        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);
//
//
//        AnswerDto answerDto = new AnswerDto(1, "text");
//        assessment.setAssessmentId(assessmentId);
//
//        AnswerId answerId = mapper.map(answerDto, AnswerId.class);
//        answerId.setAssessment(assessment);
//        Answer answer = mapper.map(answerDto, Answer.class);
//        answer.setAnswerId(answerId);
//
//        answerRepository.save(answer);
//
//        AnswerDto answerDto1 = new AnswerDto(2, "text2");
//        assessment.setAssessmentId(assessmentId);
//
//        AnswerId answerId1 = mapper.map(answerDto1, AnswerId.class);
//        answerId.setAssessment(assessment);
//        Answer answer1 = mapper.map(answerDto1, Answer.class);
//        answer.setAnswerId(answerId1);
//
//        when(answerRepository.save(answer1)).thenReturn(answer1);
//
//
//        String dataRequest = resourceFileUtil.getJsonString("dto/set-question-data-request.json");
//
//        Response saveResponse = client.toBlocking().retrieve(HttpRequest.POST("/v1/notes/1", dataRequest)
//                .bearerAuth("anything"), Response.class);
//
//        System.out.println(saveResponse);
//        assertEquals(HttpResponse.ok(), saveResponse);
//
//    }
//
//}

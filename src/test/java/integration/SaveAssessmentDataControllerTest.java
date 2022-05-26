package integration;

import au.com.dius.pact.core.model.Response;
import com.xact.assessment.dtos.AnswerRequest;
import com.xact.assessment.dtos.RatingDto;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AnswerRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SaveAssessmentDataControllerTest {


    private ModelMapper mapper = new ModelMapper();

    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    TopicLevelAssessmentRepository topicLevelAssessmentRepository;

    @Inject
    AnswerRepository answerRepository;

    @MockBean(TopicLevelAssessmentRepository.class)
    TopicLevelAssessmentRepository topicLevelAssessmentRepository() {
        return mock(TopicLevelAssessmentRepository.class);
    }

    @MockBean(AnswerRepository.class)
    AnswerRepository answerRepository() {
        return mock(AnswerRepository.class);
    }


    @Test
    void testSaveTopicLevelAssessmentResponse() throws IOException {

        Integer assessmentId = 1;
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);

        topicRatingAndRecommendation.setRating(RatingDto.ONE);
        topicRatingAndRecommendation.setRecommendation("text");

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);

        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);


        AnswerRequest answerRequest = new AnswerRequest(1, "text");
        assessment.setAssessmentId(assessmentId);

        AnswerId answerId = mapper.map(answerRequest, AnswerId.class);
        answerId.setAssessment(assessment);
        Answer answer = mapper.map(answerRequest, Answer.class);
        answer.setAnswerId(answerId);

        answerRepository.save(answer);

        AnswerRequest answerRequest1 = new AnswerRequest(2, "text2");
        assessment.setAssessmentId(assessmentId);

        AnswerId answerId1 = mapper.map(answerRequest1, AnswerId.class);
        answerId.setAssessment(assessment);
        Answer answer1 = mapper.map(answerRequest1, Answer.class);
        answer.setAnswerId(answerId1);

        when(answerRepository.save(answer1)).thenReturn(answer1);


        String dataRequest = resourceFileUtil.getJsonString("dto/set-question-data-request.json");

        Response saveResponse = client.toBlocking().retrieve(HttpRequest.POST("/v1/notes/1", dataRequest)
                .bearerAuth("anything"), Response.class);

        System.out.println(saveResponse);
        assertEquals(HttpResponse.ok(), saveResponse);

    }

}

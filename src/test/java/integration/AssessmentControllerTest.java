/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class AssessmentControllerTest {


    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;
    @Inject
    AssessmentsRepository assessmentsRepository;
    @Inject
    AnswerRepository answerRepository;
    @Inject
    ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    @Inject
    TopicLevelAssessmentRepository topicLevelAssessmentRepository;

    @MockBean(UsersAssessmentsRepository.class)
    UsersAssessmentsRepository usersAssessmentsRepository() {
        return mock(UsersAssessmentsRepository.class);
    }
    @MockBean(AssessmentsRepository.class)
    AssessmentsRepository assessmentsRepository() {
        return mock(AssessmentsRepository.class);
    }
    @MockBean(AnswerRepository.class)
    AnswerRepository answerRepository() {
        return mock(AnswerRepository.class);
    }
    @MockBean(ParameterLevelAssessmentRepository.class)
    ParameterLevelAssessmentRepository parameterLevelAssessmentRepository() {
        return mock(ParameterLevelAssessmentRepository.class);
    }
    @MockBean(TopicLevelAssessmentRepository.class)
    TopicLevelAssessmentRepository topicLevelAssessmentRepository() {
        return mock(TopicLevelAssessmentRepository.class);
    }


    @Test
    void testGetAssessmentsResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessment.setAssessmentId(23);
        assessment.setAssessmentName("Mocked Assessment");


        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(singletonList(assessmentUsers));
        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(singletonList(assessmentUsers));
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-assessments-response.json");

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void testGetAssessmentResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessment.setAssessmentId(123);
        assessment.setAssessmentName("Mocked Assessment");

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("text");
        AnswerId answerId = new AnswerId(assessment,question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("answer");

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment,assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(4);
        parameterLevelAssessment.setRecommendation("recommendation");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(2);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId(assessment,assessmentTopic);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(4);

        when(usersAssessmentsRepository.findByUserEmail(userEmail,123)).thenReturn(assessmentUsers);
        when(answerRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(answer));
        when(parameterLevelAssessmentRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(parameterLevelAssessment));
        when(topicLevelAssessmentRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(topicLevelAssessment));

        String expectedResponse = resourceFileUtil.getJsonString("dto/get-assessment-response.json");

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/123")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }

    @Test
    void finishAssessment() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessment.setAssessmentId(123);
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setAssessmentName("Mocked Assessment");

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Completed);
        expectedAssessment.setAssessmentName("Mocked Assessment");

        when(usersAssessmentsRepository.findByUserEmail(userEmail,123)).thenReturn(assessmentUsers);
        when(assessmentsRepository.update( expectedAssessment)).thenReturn(expectedAssessment);
        String expectedResponse = resourceFileUtil.getJsonString("dto/finish-assessment-response.json");
        MutableHttpRequest request =  HttpRequest.create(HttpMethod.PUT,"/v1/assessments/123/statuses/finish").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request,String.class);

        assertEquals(expectedResponse, assessmentResponse.body());

    }

    @Test
    void reopenAssessment() throws IOException {

        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessment.setAssessmentId(123);
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        assessment.setAssessmentName("Mocked Assessment");

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Completed);
        expectedAssessment.setAssessmentName("Mocked Assessment");

        when(usersAssessmentsRepository.findByUserEmail(userEmail,123)).thenReturn(assessmentUsers);
        when(assessmentsRepository.update( expectedAssessment)).thenReturn(expectedAssessment);
        String expectedResponse = resourceFileUtil.getJsonString("dto/reopen-assessment-response.json");
        MutableHttpRequest request =  HttpRequest.create(HttpMethod.PUT,"/v1/assessments/123/statuses/open").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request,String.class);

        assertEquals(expectedResponse, assessmentResponse.body());

    }


}

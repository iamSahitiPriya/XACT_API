/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import io.micronaut.context.annotation.Property;
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
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class AssessmentControllerTest {

    private ModelMapper mapper = new ModelMapper();

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
    @Inject
    QuestionRepository questionRepository;
    @Inject
    AssessmentParameterRepository assessmentParameterRepository;
    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

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

    @MockBean(QuestionRepository.class)
    QuestionRepository questionRepository(){
        return mock(QuestionRepository.class);
    }

    @MockBean(AssessmentTopicRepository.class)
    AssessmentTopicRepository assessmentTopicRepository() {
        return mock(AssessmentTopicRepository.class);
    }
    @MockBean(AssessmentParameterRepository.class)
    AssessmentParameterRepository assessmentParameterRepository() {
        return mock(AssessmentParameterRepository.class);
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
        Organisation org = new Organisation(12, "testorg", "IT", "Telecom", 10);
        assessment.setOrganisation(org);
        assessment.setAssessmentName("Mocked Assessment");

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("text");
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("answer");

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(4);
        parameterLevelAssessment.setRecommendation("recommendation");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(2);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(4);

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(usersAssessmentsRepository.findUserByAssessmentId(1, AssessmentRole.Owner)).thenReturn(singletonList(assessmentUsers));
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

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(assessmentsRepository.update(expectedAssessment)).thenReturn(expectedAssessment);
        String expectedResponse = resourceFileUtil.getJsonString("dto/finish-assessment-response.json");
        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/123/statuses/finish").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request, String.class);

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

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(assessmentsRepository.update(expectedAssessment)).thenReturn(expectedAssessment);
        String expectedResponse = resourceFileUtil.getJsonString("dto/reopen-assessment-response.json");
        MutableHttpRequest request = HttpRequest.create(HttpMethod.PUT, "/v1/assessments/123/statuses/open").contentLength(0)
                .bearerAuth("anything");
        HttpResponse<String> assessmentResponse = client.toBlocking().exchange(request, String.class);

        assertEquals(expectedResponse, assessmentResponse.body());

    }

    @Test
    void testSaveTopicLevelAssessmentResponse() throws IOException {

        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(), any())).thenReturn(assessmentUsers);

        Answer answer = new Answer();

        when(answerRepository.save(answer)).thenReturn(answer);


        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);

        topicRatingAndRecommendation.setRating(1);
        topicRatingAndRecommendation.setRecommendation("text");

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);


        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);


        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/notes/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testSaveParameterLevelAssessmentResponse() throws IOException {

        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);

        Answer answer = new Answer();

        when(answerRepository.save(answer)).thenReturn(answer);


        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);

        parameterRatingAndRecommendation.setRating(1);
        parameterRatingAndRecommendation.setRecommendation("some text");

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);

        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-level-request.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessments/notes/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateAssessmentAnswer() throws IOException {
        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);

        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionText("Question");
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        AnswerId answerId = new AnswerId(assessment,question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("Ans");
        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        when(answerRepository.save(answer)).thenReturn(answer);

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-string-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/answers/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }
    @Test
    void testUpdateTopicRecommendation() throws IOException {
        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);


        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.findById(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRecommendation("Recommendation");

        when(topicLevelAssessmentRepository.findById(topicLevelId)).thenReturn(Optional.of(topicLevelAssessment));
        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-string-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendation/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRecommendation() throws IOException {
        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);


        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.findById(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRecommendation("Recommendation");

        when(parameterLevelAssessmentRepository.findById(parameterLevelId)).thenReturn(Optional.of(parameterLevelAssessment));
        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-string-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRating() throws IOException {
        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);


        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.findById(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(1);

        when(parameterLevelAssessmentRepository.findById(parameterLevelId)).thenReturn(Optional.of(parameterLevelAssessment));
        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRating/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateTopicRating() throws IOException {
        UserId userId = new UserId();
        userId.setUserEmail("hello@email.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(any(),any())).thenReturn(assessmentUsers);


        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(assessmentTopicRepository.findById(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

        when(topicLevelAssessmentRepository.findById(topicLevelId)).thenReturn(Optional.of(topicLevelAssessment));
        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);


        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-rating-values.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRating/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }



}

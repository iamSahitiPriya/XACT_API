/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.*;
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
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@MicronautTest
class AssessmentControllerTest {

    private ModelMapper mapper = new ModelMapper();

    @Inject
    @Client("/")

    HttpClient client;


    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;
    @Inject
    AssessmentRepository assessmentRepository;
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

    @Inject
    TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    @Inject
    ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    @Inject
    AccessControlRepository accessControlRepository;


    @MockBean(UsersAssessmentsRepository.class)
    UsersAssessmentsRepository usersAssessmentsRepository() {
        return mock(UsersAssessmentsRepository.class);
    }

    @MockBean(AssessmentRepository.class)
    AssessmentRepository assessmentRepository() {
        return mock(AssessmentRepository.class);
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


    @MockBean(TopicLevelRecommendationRepository.class)
    TopicLevelRecommendationRepository topicLevelRecommendationRepository() {
        return mock(TopicLevelRecommendationRepository.class);
    }

    @MockBean(ParameterLevelRecommendationRepository.class)
    ParameterLevelRecommendationRepository parameterLevelRecommendationRepository() {
        return mock(ParameterLevelRecommendationRepository.class);
    }

    @MockBean(AccessControlRepository.class)
    AccessControlRepository accessControlRepository(){return mock(AccessControlRepository.class);}


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

        Organisation org = new Organisation(12, "testorg", "IT", "Telecom", 10);
        assessment.setOrganisation(org);


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
        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon("some text");
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(2);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(4);

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon("some text");
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(usersAssessmentsRepository.findUserByAssessmentId(1, AssessmentRole.Owner)).thenReturn(singletonList(assessmentUsers));
        when(answerRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(answer));
        when(parameterLevelAssessmentRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(parameterLevelAssessment));

        when(parameterLevelRecommendationRepository.findByAssessmentAndParameter(assessment.getAssessmentId(),assessmentParameter.getParameterId())).thenReturn(singletonList(parameterLevelRecommendation));
        when(topicLevelAssessmentRepository.findByAssessment(assessment.getAssessmentId())).thenReturn(singletonList(topicLevelAssessment));
        when(topicLevelRecommendationRepository.findByAssessmentAndTopic(assessment.getAssessmentId(),assessmentTopic.getTopicId())).thenReturn(singletonList(topicLevelRecommendation));


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
        when(assessmentRepository.update(expectedAssessment)).thenReturn(expectedAssessment);

        Organisation org = new Organisation();
        org.setOrganisationName("Org");
        expectedAssessment.setOrganisation(org);
        expectedAssessment.setAssessmentName("Mocked Assessment");

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(assessmentRepository.update(assessment)).thenReturn(expectedAssessment);
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
        when(assessmentRepository.update(expectedAssessment)).thenReturn(expectedAssessment);

        expectedAssessment.setAssessmentStatus(AssessmentStatus.Active);
        Organisation org = new Organisation();
        org.setOrganisationName("Org");
        expectedAssessment.setOrganisation(org);
        expectedAssessment.setAssessmentName("Mocked Assessment");

        when(usersAssessmentsRepository.findByUserEmail(userEmail, 123)).thenReturn(assessmentUsers);
        when(assessmentRepository.update(assessment)).thenReturn(expectedAssessment);

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

        TopicLevelRecommendationRequest topicLevelRecommendationRequest=new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendation("some recommendation");
        topicLevelRecommendationRequest.setEffort("HIGH");
        topicLevelRecommendationRequest.setImpact("MEDIUM");
        topicLevelRecommendationRequest.setDeliveryHorizon("some text");

        TopicLevelId topicLevelId = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = mapper.map(topicRatingAndRecommendation, TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);


        when(topicLevelAssessmentRepository.save(topicLevelAssessment)).thenReturn(topicLevelAssessment);

        TopicLevelRecommendation topicLevelRecommendation=mapper.map(topicLevelRecommendationRequest,TopicLevelRecommendation.class);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(assessmentTopicRepository.findById(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));
        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);

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

      AssessmentParameter assessmentParameter=new AssessmentParameter();
      assessmentParameter.setParameterId(1);


        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(4);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationRequest.setEffort("HIGH");
        parameterLevelRecommendationRequest.setImpact("MEDIUM");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some text");


        ParameterLevelRecommendation parameterLevelRecommendation=mapper.map(parameterLevelRecommendationRequest,ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(assessmentParameterRepository.findById(assessmentParameter.getParameterId())).thenReturn(Optional.of(assessmentParameter));
        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);

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


        TopicLevelRecommendationTextRequest topicLevelRecommendationTextRequest=new TopicLevelRecommendationTextRequest();

        topicLevelRecommendationTextRequest.setRecommendation("some recommendation");
        topicLevelRecommendationTextRequest.setRecommendationId(1);
        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        when(topicLevelRecommendationRepository.existsById(topicLevelRecommendationTextRequest.getRecommendationId())).thenReturn(false);
        when(topicLevelRecommendationRepository.findById(topicLevelRecommendationTextRequest.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));


        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendation(topicLevelRecommendationTextRequest.getRecommendation());
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationTextRequest.getRecommendationId());

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-recommendation-text-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationText/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }



    @Test
    void testUpdateParameterRecommendationText() throws IOException {
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


        ParameterLevelRecommendationRequest parameterLevelRecommendationTextRequest=new ParameterLevelRecommendationRequest();

        parameterLevelRecommendationTextRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationTextRequest.setRecommendationId(1);
        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(false);
        when(parameterLevelRecommendationRepository.findById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));


        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationTextRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationTextRequest.getRecommendationId());

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-parameter-recommendation-text-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }
    @Test
    void testUpdateTopicRecommendationImpact() throws IOException {
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


        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicLevelRecommendationRepository.findById(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationImpact(LOW);

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-recommendation-impact-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationFields/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }
    @Test
    void testUpdateParameterRecommendationImpact() throws IOException {
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


        ParameterLevelRecommendationRequest parameterLevelRecommendationTextRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationTextRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationTextRequest.setRecommendationId(1);
        parameterLevelRecommendationTextRequest.setImpact("LOW");

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(false);
        when(parameterLevelRecommendationRepository.findById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));


        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationTextRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationTextRequest.getRecommendationId());
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.valueOf(parameterLevelRecommendationTextRequest.getImpact()));

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-parameter-recommendation-text-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testUpdateTopicRecommendationEffect() throws IOException {
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


        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicLevelRecommendationRepository.findById(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setEffort("LOW");
        topicLevelRecommendationRequest.setImpact("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-recommendation-effort-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationFields/1/1", dataRequest)
                .bearerAuth("anything"));


        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void testUpdateParameterRecommendationEffort() throws IOException {
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


        ParameterLevelRecommendationRequest parameterLevelRecommendationTextRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationTextRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationTextRequest.setRecommendationId(1);
        parameterLevelRecommendationTextRequest.setImpact("");
        parameterLevelRecommendationTextRequest.setEffort("HIGH");

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(false);
        when(parameterLevelRecommendationRepository.findById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationTextRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationTextRequest.getRecommendationId());
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.valueOf(parameterLevelRecommendationTextRequest.getEffort()));

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-parameter-recommendation-text-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/parameterRecommendation/1/1", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }


    @Test
    void testUpdateTopicRecommendationDeliveryHorizon() throws IOException {
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


        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicLevelRecommendationRepository.findById(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setDeliveryHorizon("some text");
        topicLevelRecommendationRequest.setImpact("");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setDeliveryHorizon("");

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-recommendation-deliveryHorizon-value.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.PATCH("/v1/assessments/topicRecommendationFields/1/1", dataRequest)
                .bearerAuth("anything"));


        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }
    @Test
    void testUpdateParameterRecommendationDeliveryHorizon() throws IOException {
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


        ParameterLevelRecommendationRequest parameterLevelRecommendationTextRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationTextRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationTextRequest.setRecommendationId(1);
        parameterLevelRecommendationTextRequest.setDeliveryHorizon("dummy text");

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(false);
        when(parameterLevelRecommendationRepository.findById(parameterLevelRecommendationTextRequest.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationTextRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationTextRequest.getRecommendationId());
        parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationTextRequest.getDeliveryHorizon());

        String dataRequest = resourceFileUtil.getJsonString("dto/update-particular-parameter-recommendation-text-value.json");

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


    @Test
    void testDeleteParameterRecommendation() throws IOException {
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


        ParameterLevelRecommendationRequest parameterLevelRecommendationTextRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationTextRequest.setRecommendation("some recommendation");
        parameterLevelRecommendationTextRequest.setRecommendationId(1);
        parameterLevelRecommendationTextRequest.setDeliveryHorizon("dummy text");

        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationTextRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationTextRequest.getRecommendationId());
        parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationTextRequest.getDeliveryHorizon());

        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        doNothing().when(parameterLevelRecommendationRepository).deleteById(parameterLevelRecommendationTextRequest.getRecommendationId());

        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        parameterLevelRecommendationRepository.deleteById(parameterLevelRecommendationTextRequest.getRecommendationId());


        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/deleteParameterRecommendation/1/1/1")
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void testDeleteTopicRecommendation() throws IOException {
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
        Integer recommendationId =1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(recommendationId);
        topicLevelRecommendationRequest.setEffort("LOW");
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setRecommendation("some recommendation");
        topicLevelRecommendationRequest.setDeliveryHorizon("dummy text");

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation(topicLevelRecommendationRequest.getRecommendation());
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);

        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        doNothing().when(parameterLevelRecommendationRepository).deleteById(topicLevelRecommendationRequest.getRecommendationId());

        topicLevelRecommendationRepository.save(topicLevelRecommendation);
        topicLevelRecommendationRepository.deleteById(topicLevelRecommendationRequest.getRecommendationId());


        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/assessments/deleteRecommendation/1/1/1")
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }
    @Test
    void testGetAdminAssessmentsResponse() throws IOException, ParseException {

        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);

        List<Assessment> assessments=new ArrayList<>();

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", organisation, Active, created1, updated1);

        assessments.add(assessment1);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";

        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

        when(assessmentRepository.TotalAssessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate))).thenReturn(assessments);
        when(accessControlRepository.getAccessControlRolesByEmail("dummy@test.com")).thenReturn(Optional.of(AccessControlRoles.valueOf("Admin")));


        String expectedResponse = resourceFileUtil.getJsonString("dto/get-admin-assessments-response.json");

        String assessmentResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessments/admin/1/2022-10-13/2022-05-13")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, assessmentResponse);

    }


}

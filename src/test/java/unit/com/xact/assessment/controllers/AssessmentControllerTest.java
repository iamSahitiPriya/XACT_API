/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationEffort.MEDIUM;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class AssessmentControllerTest {


    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UsersAssessmentsService usersAssessmentsService = Mockito.mock(UsersAssessmentsService.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final AnswerService answerService = Mockito.mock(AnswerService.class);
    private final QuestionService questionService = Mockito.mock(QuestionService.class);
    private final ParameterService parameterService = Mockito.mock(ParameterService.class);
    private final TopicService topicService = Mockito.mock(TopicService.class);
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = Mockito.mock(TopicAndParameterLevelAssessmentService.class);
    private final AssessmentController assessmentController = new AssessmentController(usersAssessmentsService, userAuthService, assessmentService, answerService, topicAndParameterLevelAssessmentService, parameterService, topicService, questionService);

    @Test
    void testGetAssessments() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        AssessmentResponse expectedAssessment = new AssessmentResponse();
        expectedAssessment.setAssessmentId(1);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
        expectedAssessment.setUpdatedAt(updated);
        HttpResponse<List<AssessmentResponse>> actualAssessments = assessmentController.getAssessments(authentication);

        assertEquals(expectedAssessment.getAssessmentId(), actualAssessments.body().get(0).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessments.body().get(0).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessments.body().get(0).getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), actualAssessments.body().get(0).getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessments.body().get(0).getUpdatedAt());
        verify(usersAssessmentsService).findAssessments(userEmail);
    }

    @Test
    void testCreateAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("Dummy assessment");
        assessmentRequest.setDomain("Dummy domain");
        assessmentRequest.setIndustry("Dummy Industry");
        assessmentRequest.setOrganisationName("Dummy Org");
        assessmentRequest.setTeamSize(10);
        UserDto userDto = new UserDto();
        userDto.setEmail("dummy@thoughtworks.com");
        userDto.setRole(UserRole.Owner);
        List<UserDto> users = Collections.singletonList(userDto);
        assessmentRequest.setUsers(users);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);


        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(assessmentService.createAssessment(assessmentRequest, user)).thenReturn(assessment);

        HttpResponse<AssessmentResponse> actualAssessments = assessmentController.createAssessment(assessmentRequest, authentication);

        assertNotNull(actualAssessments.body().getAssessmentId());
        assertEquals(assessment.getAssessmentName(), actualAssessments.body().getAssessmentName());
        assertEquals(assessment.getAssessmentStatus().name(), actualAssessments.body().getAssessmentStatus().name());
        assertEquals(assessment.getOrganisation().getOrganisationName(), actualAssessments.body().getOrganisationName());
    }

    @Test
    void testGetAssessment() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Active, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        Integer assessmentId = 123;
        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        List<Answer> answers = new ArrayList<>();
        List<TopicLevelAssessment> topicAssessments = new ArrayList<>();
        List<ParameterLevelAssessment> parameterAssessments = new ArrayList<>();
        Question question = new Question();
        question.setQuestionText("Question");
        question.setQuestionId(1);
        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterName("my param");
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicName("my topic");
        AssessmentModule module = new AssessmentModule();
        module.setModuleName("my module");
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);
        topic.setModule(module);
        parameter.setTopic(topic);
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, parameter);
        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment(parameterLevelId, 4, new Date(), new Date());
        parameterAssessments.add(parameterAssessment);

        List <ParameterRatingAndRecommendation> parameterRatingAndRecommendationList = new ArrayList<>();
        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(parameterLevelId.getParameter().getParameterId());
        parameterRatingAndRecommendation.setRating(2);

        List <ParameterLevelRecommendationRequest> parameterLevelRecommendationRequestList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation=new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setDeliveryHorizon("some text");
        when(parameterService.getParameter(parameter.getParameterId())).thenReturn(Optional.of(parameter));
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentRecommendationData(assessmentId,topic.getTopicId())).thenReturn(Collections.singletonList(parameterLevelRecommendation));
        when(topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));


        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest=new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(parameterLevelRecommendation.getRecommendationId());
        parameterLevelRecommendationRequest.setRecommendation(parameterLevelRecommendation.getRecommendation());
        parameterLevelRecommendationRequest.setImpact(parameterLevelRecommendationRequest.getImpact());
        parameterLevelRecommendationRequest.setEffort(parameterLevelRecommendationRequest.getEffort());
        parameterLevelRecommendationRequest.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());
        parameterLevelRecommendationRequestList.add(parameterLevelRecommendationRequest);

        parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(parameterLevelRecommendationRequestList);
        parameterRatingAndRecommendationList.add(parameterRatingAndRecommendation);


        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);

        List <TopicRatingAndRecommendation> topicRatingAndRecommendationList = new ArrayList<>();
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(topic.getTopicId());
        topicRatingAndRecommendation.setRating(2);

       List <TopicLevelRecommendationRequest> topicLevelRecommendationRequestList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setDeliveryHorizon("some text");
        when(topicService.getTopic(topic.getTopicId())).thenReturn(Optional.of(topic));
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentRecommendationData(assessmentId,topic.getTopicId())).thenReturn(Collections.singletonList(topicLevelRecommendation));
        when(topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId)).thenReturn(Collections.singletonList(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest=new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(topicLevelRecommendation.getRecommendationId());
        topicLevelRecommendationRequest.setRecommendation(topicLevelRecommendation.getRecommendation());
        topicLevelRecommendationRequest.setImpact(topicLevelRecommendationRequest.getImpact());
        topicLevelRecommendationRequest.setEffort(topicLevelRecommendationRequest.getEffort());
        topicLevelRecommendationRequest.setDeliveryHorizon(topicLevelRecommendationRequest.getDeliveryHorizon());
        topicLevelRecommendationRequestList.add(topicLevelRecommendationRequest);

        topicRatingAndRecommendation.setTopicLevelRecommendationRequest(topicLevelRecommendationRequestList);
        topicRatingAndRecommendationList.add(topicRatingAndRecommendation);


        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
        AssessmentResponse expectedAssessment = new AssessmentResponse();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
        expectedAssessment.setUpdatedAt(updated);
        expectedAssessment.setTopicRatingAndRecommendation(topicRatingAndRecommendationList);
        expectedAssessment.setParameterRatingAndRecommendation(parameterRatingAndRecommendationList);

        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.getAssessment(assessmentId, authentication);

        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessment.body().getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), actualAssessment.body().getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessment.body().getUpdatedAt());
        verify(assessmentService).getAssessment(assessmentId, user);
    }

    @Test
    void testFinishAssessment() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Active, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        Integer assessmentId = 123;
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Completed);
        expectedAssessment.setUpdatedAt(updated);
        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(assessmentService.finishAssessment(assessment)).thenReturn(expectedAssessment);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.finishAssessment(assessmentId, authentication);

        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus().name(), actualAssessment.body().getAssessmentStatus().name());
        verify(assessmentService).finishAssessment(assessment);
    }

    @Test
    void testReopenAssessment() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(123, "xact", organisation, AssessmentStatus.Completed, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        Integer assessmentId = 123;
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Active);
        expectedAssessment.setUpdatedAt(updated);
        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(assessmentService.reopenAssessment(assessment)).thenReturn(expectedAssessment);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.reopenAssessment(assessmentId, authentication);

        assertEquals(expectedAssessment.getAssessmentId(), actualAssessment.body().getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessment.body().getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus().name(), actualAssessment.body().getAssessmentStatus().name());
        verify(assessmentService).reopenAssessment(assessment);
    }

    @Test
    void testSaveAssessmentAtTopicLevel() {
        Integer assessmentId = 1;
        Integer topicId =1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);

        List<TopicLevelRecommendationRequest> topicLevelRecommendationRequest = new ArrayList<>();

        TopicLevelRecommendationRequest topicLevelRecommendationRequest1= new TopicLevelRecommendationRequest(1,"some text","HIGH","LOW","text");
        TopicLevelRecommendationRequest topicLevelRecommendationRequest2= new TopicLevelRecommendationRequest(2,"some more text","HIGH","LOW","text");

        topicLevelRecommendationRequest.add(topicLevelRecommendationRequest1);
        topicLevelRecommendationRequest.add(topicLevelRecommendationRequest2);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);

        topicRatingAndRecommendation.setTopicLevelRecommendationRequest(topicLevelRecommendationRequest);

        topicLevelAssessmentRequest.setTopicRatingAndRecommendation(topicRatingAndRecommendation);

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(any(), any())).thenReturn(assessmentUsers.getUserId().getAssessment());
        System.out.println(userAuthService.getLoggedInUser(authentication));

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));
        topicLevelAssessmentRequest.setTopicRatingAndRecommendation((topicRatingAndRecommendation));
        topicLevelAssessmentRequest.getTopicRatingAndRecommendation().setTopicLevelRecommendationRequest(topicLevelRecommendationRequest);

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelAssessment((TopicLevelAssessment) any(),any(),any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testSaveAssessmentAtParameterLevel() {
        Integer assessmentId = 1;
        Integer parameterId=2;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        List<AnswerRequest> answerRequestList = new ArrayList<>();

        AnswerRequest answerRequest1 = new AnswerRequest(1, "some text");
        AnswerRequest answerRequest2 = new AnswerRequest(2, "some more text");
        answerRequestList.add(answerRequest1);
        answerRequestList.add(answerRequest2);

        List<ParameterLevelAssessmentRequest> parameterLevelAssessmentRequestList= new ArrayList<>();

        ParameterLevelAssessmentRequest parameterLevelAssessmentRequest = new ParameterLevelAssessmentRequest();
        parameterLevelAssessmentRequest.setAnswerRequest(answerRequestList);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);


        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(parameterId);
        parameterRatingAndRecommendation.setRating(1);

        List<ParameterLevelRecommendationRequest> parameterLevelRecommendationRequest = new ArrayList<>();

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest1= new ParameterLevelRecommendationRequest(1,"some text","HIGH","LOW","text");
        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest2= new ParameterLevelRecommendationRequest(2,"some more text","HIGH","LOW","text");

        parameterLevelRecommendationRequest.add(parameterLevelRecommendationRequest1);
        parameterLevelRecommendationRequest.add(parameterLevelRecommendationRequest2);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(2);

        parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(parameterLevelRecommendationRequest);

        parameterLevelAssessmentRequest.setParameterRatingAndRecommendation(parameterRatingAndRecommendation);

        parameterLevelAssessmentRequestList.add(parameterLevelAssessmentRequest);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(parameterLevelAssessmentRequestList);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessmentUsers.getUserId().getAssessment());
        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelAssessment((List<ParameterLevelAssessment>) any(), any(), any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testUpdateAssessmentAnswers() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionText("Question");

        when(questionService.getQuestion(questionId)).thenReturn(Optional.of(question));

        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswer("Answer");

        when(answerService.getAnswer(answerId)).thenReturn(Optional.of(answer));
        when(answerService.saveAnswer(answer)).thenReturn(answer);


        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveNotesAnswer(assessmentId, questionId, "Note", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testUpdateAssessmentTopicRecommendationTextWithOutRecommendationId() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));


        TopicLevelRecommendationTextRequest topicLevelRecommendationTextRequest=new TopicLevelRecommendationTextRequest();
        topicLevelRecommendationTextRequest.setRecommendation("some text");


        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendation(topicLevelRecommendationTextRequest.getRecommendation());

        HttpResponse<TopicLevelRecommendationResponse> actualResponse=assessmentController.saveTopicRecommendationText(assessmentId,topicId,topicLevelRecommendationTextRequest,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentTopicRecommendationTextWithRecommendationId() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));


        TopicLevelRecommendationTextRequest topicLevelRecommendationTextRequest=new TopicLevelRecommendationTextRequest();
        topicLevelRecommendationTextRequest.setRecommendation("some text");
        topicLevelRecommendationTextRequest.setRecommendationId(1);

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationTextRequest.getRecommendationId());
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        topicLevelRecommendation.setRecommendation(topicLevelRecommendationTextRequest.getRecommendation());


        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        HttpResponse<TopicLevelRecommendationResponse> actualResponse=assessmentController.saveTopicRecommendationText(assessmentId,topicId,topicLevelRecommendationTextRequest,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentTopicRecommendationImpact() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationImpact(LOW);

        HttpResponse<TopicLevelRecommendationRequest> actualResponse=assessmentController.saveTopicRecommendationFields(assessmentId,topicId,topicLevelRecommendationRequest,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentTopicRecommendationEffect() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setEffort("HIGH");
        topicLevelRecommendationRequest.setImpact("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationEffort(MEDIUM);

        HttpResponse<TopicLevelRecommendationRequest> actualResponse=assessmentController.saveTopicRecommendationFields(assessmentId,topicId,topicLevelRecommendationRequest,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

        assertEquals(topicLevelRecommendation.getRecommendationEffort().toString(),"HIGH");
        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentTopicRecommendationDeliveryHorizon() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest= new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setDeliveryHorizon("some text");
        topicLevelRecommendationRequest.setImpact("");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setDeliveryHorizon("");


        HttpResponse<TopicLevelRecommendationRequest> actualResponse=assessmentController.saveTopicRecommendationFields(assessmentId,topicId,topicLevelRecommendationRequest,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

        assertEquals(topicLevelRecommendation.getDeliveryHorizon(),"some text");
        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testDeleteRecommendationWithRecommendationId() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

         Integer recommendationId =1;

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(recommendationId);
        topicLevelRecommendation.setRecommendation("some dummy recommendation");
        topicLevelRecommendation.setDeliveryHorizon("some text");

        HttpResponse<TopicLevelRecommendationRequest> actualResponse=assessmentController.deleteRecommendation(assessmentId,topicId,recommendationId,authentication);

        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());
        verify(topicAndParameterLevelAssessmentService).deleteRecommendation(recommendationId);
    }
    @Test
    void testUpdateAssessmentTopicRating() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

        when(topicAndParameterLevelAssessmentService.searchTopic(topicLevelId)).thenReturn(Optional.of(topicLevelAssessment));
        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveTopicRating(assessmentId, topicId, "1", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(topicLevelAssessment);


    }

    @Test
    void testUpdateAssessmentParameterRecommendation() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
//        parameterLevelAssessment.setRecommendation("Recommendation");

        when(topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId)).thenReturn(Optional.of(parameterLevelAssessment));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, "Note", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(parameterLevelAssessment);

    }

    @Test
    void testUpdateAssessmentParameterRating() {
        Integer assessmentId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(1);

        when(topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId)).thenReturn(Optional.of(parameterLevelAssessment));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveParameterRating(assessmentId, parameterId, "2", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(parameterLevelAssessment);

    }

    @Test
    void shouldUpdateAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("Assessment Name");
        assessmentRequest.setDomain("Domain Name");
        assessmentRequest.setIndustry("Industry Name");
        assessmentRequest.setOrganisationName("Org Name");
        assessmentRequest.setTeamSize(10);
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto1 = new UserDto("hello@thoughtworks.com", UserRole.Owner);
        UserDto userDto2 = new UserDto("new@thoughtworks.com", UserRole.Facilitator);
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);

        assessmentRequest.setUsers(userDtoList);

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(1, "Name", "Industry", "Domain", 1);
        Assessment assessment = new Assessment(1, "Assessment", organisation, AssessmentStatus.Active, created, updated);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        Set<AssessmentUsers> assessmentUsersSet = new HashSet<>(Set.of());
        UserId userId1 = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Owner);
        assessmentUsersSet.add(assessmentUsers1);

        UserId userId2 = new UserId("new@thoughtworks.com", assessment);
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, AssessmentRole.Facilitator);
        assessmentUsersSet.add(assessmentUsers2);

        when(assessmentService.getAssessmentUsers(assessmentRequest, user, assessment)).thenReturn(assessmentUsersSet);
        doNothing().when(assessmentService).updateAssessment(assessment, assessmentUsersSet);
        doNothing().when(assessmentService).updateAssessment(assessment);

        HttpResponse actualResponse = assessmentController.updateAssessment(assessment.getAssessmentId(), assessmentRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());


    }
}





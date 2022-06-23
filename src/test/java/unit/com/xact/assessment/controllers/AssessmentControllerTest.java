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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment();
        parameterAssessment.setRating(3);
        parameterAssessment.setRecommendation("recommendation");
        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId, 4, "recom", new Date(), new Date());
        topicAssessments.add(topicAssessment);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
        AssessmentResponse expectedAssessment = new AssessmentResponse();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
        expectedAssessment.setUpdatedAt(updated);
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
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);
        topicRatingAndRecommendation.setRecommendation("some text");

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
        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelAssessment((TopicLevelAssessment) any(), any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testSaveAssessmentAtParameterLevel() {
        Integer assessmentId = 1;
        TopicLevelAssessmentRequest topicLevelAssessmentRequest = new TopicLevelAssessmentRequest();

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

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);
        parameterRatingAndRecommendation.setRecommendation("some text");

        parameterLevelAssessmentRequest.setParameterRatingAndRecommendation(parameterRatingAndRecommendation);

        topicLevelAssessmentRequest.setParameterLevelAssessmentRequestList(Collections.singletonList(parameterLevelAssessmentRequest));

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessmentUsers.getUserId().getAssessment());

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveAnswer(assessmentId, topicLevelAssessmentRequest, authentication);

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelAssessment((List<ParameterLevelAssessment>) any(), any());
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    public void testUpdateAssessmentAnswers() {
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
    public void testUpdateAssessmentTopicRecommendation() {
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
        topicLevelAssessment.setRecommendation("Recommendation");

        when(topicAndParameterLevelAssessmentService.searchTopic(topicLevelId)).thenReturn(Optional.of(topicLevelAssessment));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, "Note", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(topicLevelAssessment);

    }

    @Test
    public void testUpdateAssessmentTopicRating() {
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

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveTopicRating(assessmentId, topicId, "1", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(topicLevelAssessment);

    }

    @Test
    public void testUpdateAssessmentParameterRecommendation() {
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
        parameterLevelAssessment.setRecommendation("Recommendation");

        when(topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId)).thenReturn(Optional.of(parameterLevelAssessment));

        HttpResponse<TopicLevelAssessmentRequest> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, "Note", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(parameterLevelAssessment);

    }

    @Test
    public void testUpdateAssessmentParameterRating() {
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


}





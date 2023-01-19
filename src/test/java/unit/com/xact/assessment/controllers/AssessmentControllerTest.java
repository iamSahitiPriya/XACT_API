/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationEffort.MEDIUM;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentControllerTest {


    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UsersAssessmentsService usersAssessmentsService = Mockito.mock(UsersAssessmentsService.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final AnswerService answerService = Mockito.mock(AnswerService.class);
    private final ParameterService parameterService = Mockito.mock(ParameterService.class);
    private final TopicService topicService = Mockito.mock(TopicService.class);
    private final AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);

    private final UserQuestionService userQuestionService = Mockito.mock(UserQuestionService.class);
    private final NotificationService notificationService = Mockito.mock(NotificationService.class);
    private final QuestionService questionService = Mockito.mock(QuestionService.class);
    private final ActivityLogService activityLogService = Mockito.mock(ActivityLogService.class);
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = Mockito.mock(TopicAndParameterLevelAssessmentService.class);
    private final AssessmentController assessmentController = new AssessmentController(usersAssessmentsService, userAuthService, assessmentService, answerService, topicAndParameterLevelAssessmentService, activityLogService, parameterService, topicService,notificationService,userQuestionService,assessmentMasterDataService, questionService);



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
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        AssessmentUser assessmentUser = new AssessmentUser(new UserId("test@thoughtworks.com", assessment), AssessmentRole.Owner);
        assessment.setAssessmentUsers(Collections.singleton(assessmentUser));
        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        AssessmentResponse expectedAssessment = new AssessmentResponse();
        expectedAssessment.setAssessmentId(1);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.Active);
        expectedAssessment.setAssessmentPurpose("Client Purpose");
        expectedAssessment.setUpdatedAt(updated);
        HttpResponse<List<AssessmentResponse>> actualAssessments = assessmentController.getAssessments(authentication);

        assertEquals(expectedAssessment.getAssessmentId(), Objects.requireNonNull(actualAssessments.body()).get(0).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), Objects.requireNonNull(actualAssessments.body()).get(0).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), Objects.requireNonNull(actualAssessments.body()).get(0).getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), Objects.requireNonNull(actualAssessments.body()).get(0).getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), Objects.requireNonNull(actualAssessments.body()).get(0).getUpdatedAt());
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
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessment.setAssessmentUsers(assessmentUsers);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(assessmentService.createAssessment(assessmentRequest, user)).thenReturn(assessment);

        HttpResponse<AssessmentResponse> actualAssessments = assessmentController.createAssessment(assessmentRequest, authentication);

        assertEquals(HttpStatus.OK, actualAssessments.status());
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
        Assessment assessment = new Assessment(123, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        AssessmentUser assessmentUser = new AssessmentUser(new UserId(userEmail, assessment), AssessmentRole.Owner);
        assessmentUsers.add(assessmentUser);
        assessment.setAssessmentUsers(assessmentUsers);


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
        parameter.setParameterId(1);
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
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

        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationList = new ArrayList<>();
        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(parameterLevelId.getParameter().getParameterId());
        parameterRatingAndRecommendation.setRating(2);

        List<ParameterLevelRecommendationRequest> parameterLevelRecommendationRequestList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setDeliveryHorizon("some text");
        when(parameterService.getParameter(parameter.getParameterId())).thenReturn(Optional.of(parameter));
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentRecommendationData(assessmentId, topic.getTopicId())).thenReturn(Collections.singletonList(parameterLevelRecommendation));
        when(topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");
        String questionText = "new question ?";
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");

        when(userQuestionService.findAllUserQuestion(assessmentId)).thenReturn(Collections.singletonList(userQuestion));


        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        Integer recommendationTextId = parameterLevelRecommendationRequest.getRecommendationId() != null ? parameterLevelRecommendationRequest.getRecommendationId() : null;
        parameterLevelRecommendationRequest.setRecommendationId(recommendationTextId);
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

        List<TopicRatingAndRecommendation> topicRatingAndRecommendationList = new ArrayList<>();
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(topic.getTopicId());
        topicRatingAndRecommendation.setRating(2);

        List<TopicLevelRecommendationRequest> topicLevelRecommendationRequestList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setDeliveryHorizon("some text");
        when(topicService.getTopic(topic.getTopicId())).thenReturn(Optional.of(topic));
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentRecommendationData(assessmentId, topic.getTopicId())).thenReturn(Collections.singletonList(topicLevelRecommendation));
        when(topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId)).thenReturn(Collections.singletonList(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        Integer recommendationTextId1 = topicLevelRecommendationRequest.getRecommendationId() != null ? topicLevelRecommendationRequest.getRecommendationId() : null;
        topicLevelRecommendationRequest.setRecommendationId(recommendationTextId1);
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

        assertEquals(expectedAssessment.getAssessmentId(), Objects.requireNonNull(actualAssessment.body()).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), Objects.requireNonNull(actualAssessment.body()).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), Objects.requireNonNull(actualAssessment.body()).getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), Objects.requireNonNull(actualAssessment.body()).getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), Objects.requireNonNull(actualAssessment.body()).getUpdatedAt());
        assertEquals("new question ?",Objects.requireNonNull(actualAssessment.body()).getUserQuestionResponseList().get(0).getQuestion());
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
        Assessment assessment = new Assessment(123, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        Integer assessmentId = 123;
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Completed);
        expectedAssessment.setUpdatedAt(updated);
        expectedAssessment.setOrganisation(organisation);
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, expectedAssessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        expectedAssessment.setAssessmentUsers(assessmentUsers);
        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(assessmentService.finishAssessment(assessment)).thenReturn(expectedAssessment);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.finishAssessment(assessmentId, authentication);

        assertEquals(expectedAssessment.getAssessmentId(), Objects.requireNonNull(actualAssessment.body()).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), Objects.requireNonNull(actualAssessment.body()).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus().name(), Objects.requireNonNull(actualAssessment.body()).getAssessmentStatus().name());
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
        Assessment assessment = new Assessment(123, "xact", "Client Assessment", organisation, AssessmentStatus.Completed, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        Integer assessmentId = 123;
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(123);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setAssessmentStatus(AssessmentStatus.Active);
        expectedAssessment.setUpdatedAt(updated);
        expectedAssessment.setOrganisation(organisation);
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, expectedAssessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        expectedAssessment.setAssessmentUsers(assessmentUsers);
        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(assessmentService.reopenAssessment(assessment)).thenReturn(expectedAssessment);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        HttpResponse<AssessmentResponse> actualAssessment = assessmentController.reopenAssessment(assessmentId, authentication);

        assertEquals(expectedAssessment.getAssessmentId(), Objects.requireNonNull(actualAssessment.body()).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), Objects.requireNonNull(actualAssessment.body()).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus().name(), Objects.requireNonNull(actualAssessment.body()).getAssessmentStatus().name());
        verify(assessmentService).reopenAssessment(assessment);
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));


        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendation("some text");


        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendation(topicLevelRecommendationRequest.getRecommendation());

        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendation("some text");
        topicLevelRecommendationRequest.setRecommendationId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        topicLevelRecommendation.setRecommendation(topicLevelRecommendationRequest.getRecommendation());


        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentTopicRecommendationWithNewRecommendationId() {
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendation("");
        topicLevelRecommendationRequest.setRecommendationId(1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        topicLevelRecommendation.setRecommendation(topicLevelRecommendationRequest.getRecommendation());

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setImpact("HIGH");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationImpact(LOW);

        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setEffort("HIGH");
        topicLevelRecommendationRequest.setImpact("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setRecommendationEffort(MEDIUM);

        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        assertEquals("HIGH", topicLevelRecommendation.getRecommendationEffort().toString());
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest();
        topicLevelRecommendationRequest.setRecommendationId(1);
        topicLevelRecommendationRequest.setDeliveryHorizon("some text");
        topicLevelRecommendationRequest.setImpact("");
        topicLevelRecommendationRequest.setEffort("");

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        topicLevelRecommendation.setDeliveryHorizon("");


        HttpResponse<TopicLevelRecommendationResponse> actualResponse = assessmentController.saveTopicRecommendation(assessmentId, topicId, topicLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        assertEquals("some text", topicLevelRecommendation.getDeliveryHorizon());
        verify(topicAndParameterLevelAssessmentService).saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    @Test
    void testDeleteTopicRecommendationWithRecommendationId() {
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(topicId);
        assessmentTopic.setTopicName("Topic Name");

        when(topicService.getTopic(topicId)).thenReturn(Optional.of(assessmentTopic));

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some recommendation");

        when(topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(topicLevelRecommendation));

        Integer recommendationId = 1;

        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationId(recommendationId);
        topicLevelRecommendation.setRecommendation("some dummy recommendation");
        topicLevelRecommendation.setDeliveryHorizon("some text");

        HttpResponse<TopicLevelRecommendationRequest> actualResponse = assessmentController.deleteRecommendation(assessmentId, topicId, recommendationId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(topicAndParameterLevelAssessmentService).deleteRecommendation(recommendationId);
    }

    @Test
    void testUpdateAssessmentParameterRecommendationTextWithOutRecommendationId() {
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Topic Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));


        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentParameterRecommendationTextWithRecommendationId() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("");
        parameterLevelRecommendationRequest.setImpact("");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());

        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentParameterRecommendationWithNewRecommendationId() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("");
        parameterLevelRecommendationRequest.setImpact("");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());

        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        when(topicAndParameterLevelAssessmentService.checkParameterRecommendationId(parameterLevelRecommendation.getRecommendationId())).thenReturn(true);

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }


    @Test
    void testUpdateAssessmentParameterRecommendationImpact() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.valueOf(parameterLevelRecommendationRequest.getImpact()));

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentParameterRecommendationEffort() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("LOW");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.valueOf(parameterLevelRecommendationRequest.getEffort()));

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    @Test
    void testUpdateAssessmentParameterRecommendationDeliveryHorizon() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("");
        parameterLevelRecommendationRequest.setImpact("");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some text");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
        parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());

        HttpResponse<ParameterLevelRecommendationResponse> actualResponse = assessmentController.saveParameterRecommendation(assessmentId, parameterId, parameterLevelRecommendationRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    @Test
    void testDeleteParameterRecommendationWithRecommendationId() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        Integer recommendationId = 1;
        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(recommendationId);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setEffort("HIGH");
        parameterLevelRecommendationRequest.setImpact("LOW");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some text");

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendation.getRecommendationId())).thenReturn(Optional.of(parameterLevelRecommendation));

        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
        parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());


        HttpResponse<ParameterLevelRecommendationRequest> actualResponse = assessmentController.deleteParameterRecommendation(assessmentId, parameterId, recommendationId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(topicAndParameterLevelAssessmentService).deleteParameterRecommendation(recommendationId);
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

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

        HttpResponse actualResponse = assessmentController.saveTopicRating(assessmentId, topicId, "1", authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(topicAndParameterLevelAssessmentService).saveRatingAndRecommendation(topicLevelAssessment);

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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

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

        HttpResponse actualResponse = assessmentController.saveParameterRating(assessmentId, parameterId, "2", authentication);

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
        Assessment assessment = new Assessment(1, "Assessment", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        Set<AssessmentUser> assessmentUserSet = new HashSet<>(Set.of());
        UserId userId1 = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser1 = new AssessmentUser(userId1, AssessmentRole.Owner);
        assessmentUserSet.add(assessmentUser1);

        UserId userId2 = new UserId("new@thoughtworks.com", assessment);
        AssessmentUser assessmentUser2 = new AssessmentUser(userId2, AssessmentRole.Facilitator);
        assessmentUserSet.add(assessmentUser2);

        when(assessmentService.getAssessmentUsers(assessmentRequest, user, assessment)).thenReturn(assessmentUserSet);
        doNothing().when(assessmentService).updateAssessment(assessment, assessmentUserSet);
        doNothing().when(assessmentService).updateAssessment(assessment);

        HttpResponse actualResponse = assessmentController.updateAssessment(assessment.getAssessmentId(), assessmentRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());


    }


    @Test
    void shouldSaveSelectedModulesByUser() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest1 = new ModuleRequest();
        moduleRequest1.setModuleId(1);
        ModuleRequest moduleRequest2 = new ModuleRequest();
        moduleRequest2.setModuleId(2);
        moduleRequests.add(moduleRequest1);
        moduleRequests.add(moduleRequest2);

        doNothing().when(assessmentService).saveAssessmentModules(moduleRequests, assessment);

        HttpResponse actualResponse = assessmentController.saveModules(assessment.getAssessmentId(), moduleRequests, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldSaveUpdatedModulesByUser() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest1 = new ModuleRequest();
        moduleRequest1.setModuleId(1);
        ModuleRequest moduleRequest2 = new ModuleRequest();
        moduleRequest2.setModuleId(2);
        moduleRequests.add(moduleRequest1);
        moduleRequests.add(moduleRequest2);

        doNothing().when(assessmentService).saveAssessmentModules(moduleRequests, assessment);
        doNothing().when(assessmentService).updateAssessmentModules(moduleRequests, assessment);

        HttpResponse actualResponse = assessmentController.updateModules(assessment.getAssessmentId(), moduleRequests, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldDeleteAssessmentIfEditable() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        AssessmentUser assessmentUser = new AssessmentUser(new UserId(userEmail, assessment), AssessmentRole.Owner);
        assessmentUsers.add(assessmentUser);
        assessment.setAssessmentUsers(assessmentUsers);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        doNothing().when(assessmentService).softDeleteAssessment(assessment);

        assertDoesNotThrow(() -> assessmentController.deleteAssessment(assessment.getAssessmentId(), authentication));

        verify(assessmentService).softDeleteAssessment(assessment);
    }

    @Test
    void shouldNotDeleteAssessmentIfNotEditable() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Completed, created, updated);

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        when(assessmentService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        doNothing().when(assessmentService).softDeleteAssessment(assessment);

        assertDoesNotThrow(() -> assessmentController.deleteAssessment(assessment.getAssessmentId(), authentication));

        verify(assessmentService, times(0)).softDeleteAssessment(assessment);
    }

    @Test
    void testSaveUserQuestions() {
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

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        String questionText = "new question ?";
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);


        when(userQuestionService.saveUserQuestion(assessment, assessmentParameter.getParameterId(), questionText)).thenReturn(userQuestion);
        when(userQuestionService.searchUserQuestion(1)).thenReturn(Optional.of(userQuestion));

        HttpResponse<UserQuestionResponse> actualResponse = assessmentController.saveUserQuestion(assessmentId, assessmentParameter.getParameterId(),questionText, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testUpdateUserQuestions() {
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

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);


        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        String questionText = "new question ?";
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion(questionText);
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);

        UserQuestion updatedUserQuestion = new UserQuestion();
        updatedUserQuestion.setQuestionId(1);
        updatedUserQuestion.setQuestion("new?");
        updatedUserQuestion.setParameter(assessmentParameter);
        updatedUserQuestion.setAssessment(assessment);

        when(userQuestionService.searchUserQuestion(1)).thenReturn(Optional.of(userQuestion));
        when(userQuestionService.updateUserQuestion(1,"new?")).thenReturn(userQuestion);

        HttpResponse<UserQuestion> actualResponse = assessmentController.updateUserQuestion(assessmentId, userQuestion.getQuestionId(), updatedUserQuestion.getQuestion(), authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testUpdateAnswerOfDefaultQuestion(){
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

        Assessment assessment = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionText("Question");

        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer();
        answer.setAnswerId(answerId);
        answer.setAnswerNote("Answer");

        when(answerService.getAnswer(answerId)).thenReturn(Optional.of(answer));
        when(answerService.saveAnswer(answer)).thenReturn(answer);

        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest();
        updateAnswerRequest.setQuestionId(1);
        updateAnswerRequest.setAnswer("answer");
        updateAnswerRequest.setType(AnswerType.DEFAULT);
        HttpResponse actualResponse = assessmentController.updateAnswer(assessmentId,questionId,updateAnswerRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void testUpdateAnswerOfAdditionalQuestion() {
        Integer assessmentId = 1;
        Integer parameterId = 1;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));
        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        userQuestion.setQuestion("question");

        when(userQuestionService.searchUserQuestion(1)).thenReturn(Optional.of(userQuestion));

        UpdateAnswerRequest updateAnswerRequest = new UpdateAnswerRequest();
        updateAnswerRequest.setQuestionId(1);
        updateAnswerRequest.setAnswer("answer");
        updateAnswerRequest.setType(AnswerType.ADDITIONAL);
        HttpResponse actualResponse = assessmentController.updateAnswer(assessmentId,1,updateAnswerRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(),actualResponse.getStatus());

    }


    @Test
    void testDeleteUserQuestionWithQuestionId() {
        Integer assessmentId = 1;
        Integer parameterId = 1;

        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);


        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        assessmentParameter.setParameterName("Parameter Name");

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        userQuestion.setQuestion("question");

        when(userQuestionService.searchUserQuestion(1)).thenReturn(Optional.of(userQuestion));

        HttpResponse actualResponse = assessmentController.deleteUserQuestion(assessmentId,1, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(userQuestionService).deleteUserQuestion(1);
    }

    @Test
    void getAssessmentMasterData() {
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(assessmentMasterDataService.getAllCategories()).thenReturn(allCategories);

        HttpResponse<UserAssessmentResponse> userAssessmentResponseHttpResponse = assessmentController.getCategories(assessment.getAssessmentId());

        List<AssessmentCategoryDto> assessmentCategoryDto=userAssessmentResponseHttpResponse.body().getAssessmentCategories();
        AssessmentCategoryDto firstAssessmentCategory = assessmentCategoryDto.get(0);
        assertEquals(firstAssessmentCategory.getCategoryId(), category.getCategoryId());
        assertEquals(firstAssessmentCategory.getCategoryName(), category.getCategoryName());
    }
}





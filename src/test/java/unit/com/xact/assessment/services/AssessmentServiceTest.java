/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.config.FeedbackNotificationConfig;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.TopicAndParameterLevelAssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {
    private UsersAssessmentsService usersAssessmentsService;
    private AssessmentRepository assessmentRepository;
    private AssessmentMasterDataService assessmentMasterDataService;
    private AssessmentService assessmentService;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private FeedbackNotificationConfig feedbackNotificationConfig;

    private static final ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        assessmentMasterDataService = mock(AssessmentMasterDataService.class);
        topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
        feedbackNotificationConfig = mock(FeedbackNotificationConfig.class);
        assessmentService = new AssessmentService(assessmentRepository, usersAssessmentsService, assessmentMasterDataService, topicAndParameterLevelAssessmentService, feedbackNotificationConfig);
    }

    @Test
    void shouldAddUsersToAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("assessment1");
        assessmentRequest.setTeamSize(1);
        assessmentRequest.setOrganisationName("org1");
        assessmentRequest.setIndustry("IT");
        assessmentRequest.setDomain("IT");
        List<UserDto> users = new ArrayList<>();
        UserDto user = new UserDto("test@gmail.com", UserRole.Owner);
        users.add(user);
        assessmentRequest.setUsers(users);

        User loggedinUser = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@email.com");
        loggedinUser.setUserInfo(userInfo);


        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(123);
        assessment.setAssessmentStatus(Active);
        assessment.setAssessmentName("assessment1");
        Organisation organisation = new Organisation();
        organisation.setOrganisationName("org1");
        organisation.setDomain("IT");
        organisation.setIndustry("IT");
        organisation.setSize(1);
        assessment.setOrganisation(organisation);


        UserId userId1 = new UserId("test@email.com", assessment);
        AssessmentUser assessmentUser1 = new AssessmentUser(userId1, AssessmentRole.Owner);
        assessmentUsers.add(assessmentUser1);


        when(assessmentRepository.save(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.createAssessment(assessmentRequest, loggedinUser);

        assertEquals(assessment.getAssessmentName(), actualAssessment.getAssessmentName());
        assertEquals(assessment.getAssessmentStatus(), actualAssessment.getAssessmentStatus());
    }

    @Test
    void getAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@email.com");
        loggedinUser.setUserInfo(userInfo);
        Assessment mockAssessment = new Assessment();
        mockAssessment.setAssessmentId(assessmentId);
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(loggedinUser.getUserEmail(), mockAssessment);
        assessmentUser.setUserId(userId);

        when(usersAssessmentsService.getAssessment(assessmentId, loggedinUser)).thenReturn(mockAssessment);

        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedinUser);

        assertEquals(assessment, mockAssessment);
    }

    @Test
    void finishAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@email.com");
        loggedinUser.setUserInfo(userInfo);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentName("assessmentName");
        assessment.setAssessmentStatus(Active);
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.finishAssessment(assessment);

        assertEquals(AssessmentStatus.Completed, actualAssessment.getAssessmentStatus());
    }

    @Test
    void reopenAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@email.com");
        loggedinUser.setUserInfo(userInfo);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(Active);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.reopenAssessment(assessment);

        assertEquals(Active, actualAssessment.getAssessmentStatus());
    }

    @Test
    void shouldReturnAssessmentUsersListWithParticularAssessmentId() {
        Set<AssessmentUser> assessmentUsers = new HashSet<>();

        Integer assessmentId = 1;

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUser assessmentUsers1 = new AssessmentUser(userId1, AssessmentRole.Facilitator);
        assessmentUsers.add(assessmentUsers1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUser assessmentUsers2 = new AssessmentUser(userId2, AssessmentRole.Facilitator);
        assessmentUsers.add(assessmentUsers2);


        when(usersAssessmentsService.getAssessmentFacilitators(assessment)).thenReturn(assessmentUsers);
        Set<AssessmentUser> actualResponse = assessmentService.getAssessmentFacilitators(assessment);
        assertEquals(assessmentUsers, actualResponse);

    }

    @Test
    void shouldUpdateAssessment() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setAssessmentStatus(Active);

        when(assessmentRepository.save(assessment)).thenReturn(assessment);

        assessment.setAssessmentName("New Assessment");

        when(assessmentRepository.update(assessment)).thenReturn(assessment);
        Set<AssessmentUser> assessmentUserSet = new HashSet<>(Set.of());
        Set<AssessmentUser> assessmentUserSet1 = new HashSet<>(Set.of());
        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUser assessmentUser1 = new AssessmentUser(userId1, AssessmentRole.Facilitator);
        assessmentUserSet.add(assessmentUser1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUser assessmentUser2 = new AssessmentUser(userId2, AssessmentRole.Facilitator);
        assessmentUserSet.add(assessmentUser1);
        assessmentUserSet1.add(assessmentUser2);

        doNothing().when(usersAssessmentsService).updateUsersInAssessment(assessmentUserSet, assessment.getAssessmentId());
        UserId addedUser = new UserId("newUser@gmail.com", assessment);
        AssessmentUser newUser = new AssessmentUser(addedUser, AssessmentRole.Facilitator);
        assessmentUserSet.add(newUser);
        assessmentUserSet1.add(newUser);

        List<AssessmentUser> assessmentUsers = new ArrayList<>();
        assessmentUsers.add(assessmentUser2);
        assessmentUsers.add(assessmentUser1);

        doNothing().when(usersAssessmentsService).updateUsersInAssessment(assessmentUserSet, assessment.getAssessmentId());

        assessmentService.getNewlyAddedUser(assessmentUserSet1, assessmentUserSet);
        assessmentUserSet.remove(assessmentUser1);
        assessmentService.getDeletedUser(assessmentUserSet1, assessmentUserSet);

        assessmentService.updateAssessmentAndUsers(assessment, assessmentUserSet);

        User user = new User();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("hello@email.com");
        user.setUserInfo(userInfo);
        when(usersAssessmentsService.getAssessment(assessment.getAssessmentId(), user)).thenReturn(assessment);

        Assessment expectedAssessment = assessmentService.getAssessment(1, user);

        assertEquals(expectedAssessment.getAssessmentName(), assessment.getAssessmentName());

    }

    @Test
    void shouldGetTheTotalAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 1);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, Active, created1, updated1);
        Assessment assessment2 = new Assessment(2, "Name", "Client Assessment", organisation, AssessmentStatus.Completed, created2, updated2);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        when(assessmentRepository.totalAssessments(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate))).thenReturn(assessments);

        List<Assessment> actualAssessments = assessmentService.getTotalAssessments(startDate, endDate);

        assertEquals(2, actualAssessments.size());

    }

    @Test
    void shouldGetTheAdminAssessmentData() throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createdAt1 = "2022-07-13";
        String updatedAt1 = "2022-09-24";
        String createdAt2 = "2022-06-01";
        String updatedAt2 = "2022-06-11";

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment2 = new Assessment(1, "Name", "Client Assessment", organisation, Completed, simpleDateFormat.parse(createdAt1), simpleDateFormat.parse(updatedAt1));
        Assessment assessment1 = new Assessment(2, "Name", "Client Assessment", organisation, Active, simpleDateFormat.parse(createdAt2), simpleDateFormat.parse(updatedAt2));

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";


        when(assessmentRepository.totalAssessments(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate))).thenReturn(assessments);

        assertEquals(2, assessmentService.getAdminAssessmentsData(startDate, endDate).size());


    }


    @Test
    void softDeleteAssessment() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        assessment.setDeleted(true);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);
        assessmentService.softDeleteAssessment(assessment);
        verify(assessmentRepository).update(assessment);
    }

    @Test
    void shouldSaveUserQuestion() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");


        Organisation organisation = new Organisation();
        organisation.setOrganisationId(1);
        organisation.setIndustry("new");
        organisation.setOrganisationName("org");
        organisation.setDomain("domain");

        assessment.setOrganisation(organisation);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

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
        when(usersAssessmentsService.saveUserQuestion(assessment, assessmentParameter.getParameterId(), userQuestion.getQuestion())).thenReturn(userQuestion);
        assessmentService.saveUserQuestion(assessment, assessmentParameter.getParameterId(), userQuestion.getQuestion());
        verify(usersAssessmentsService).saveUserQuestion(assessment, assessmentParameter.getParameterId(), userQuestion.getQuestion());
    }

    @Test
    void shouldSaveUserAnswer() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");


        Organisation organisation = new Organisation();
        organisation.setOrganisationId(1);
        organisation.setIndustry("new");
        organisation.setOrganisationName("org");
        organisation.setDomain("domain");

        assessment.setOrganisation(organisation);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

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
        userQuestion.setAnswer("answer Text?");
        UpdateAnswerRequest answerRequest = new UpdateAnswerRequest(1,AnswerType.ADDITIONAL,"answer Text?");

        doNothing().when(usersAssessmentsService).saveUserAnswer(userQuestion.getQuestionId(), userQuestion.getAnswer());

        assessmentService.saveAnswer(userQuestion.getQuestionId(), answerRequest,assessment);

        verify(usersAssessmentsService).saveUserAnswer(userQuestion.getQuestionId(), userQuestion.getAnswer());

    }

    @Test
    void shouldBeAbleToUpdateUserQuestionText() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");
        userQuestion.setQuestion("updated question Text?");


        doNothing().when(usersAssessmentsService).updateUserQuestion(userQuestion.getQuestionId(), userQuestion.getQuestion());
        assessmentService.updateUserQuestion(userQuestion.getQuestionId(), userQuestion.getQuestion());

        verify(usersAssessmentsService).updateUserQuestion(userQuestion.getQuestionId(), userQuestion.getQuestion());
    }

    @Test
    void shouldDeleteUserQuestion() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("example");
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("name");

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question Text?");
        userQuestion.setParameter(assessmentParameter);
        userQuestion.setAssessment(assessment);
        userQuestion.setAnswer("answer");

        doNothing().when(usersAssessmentsService).deleteUserQuestion(userQuestion.getQuestionId());
        assessmentService.deleteUserQuestion(userQuestion.getQuestionId());

        verify(usersAssessmentsService).deleteUserQuestion(userQuestion.getQuestionId());

    }

    @Test
    void shouldSaveModulesSelectedByUser() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest = new ModuleRequest();
        moduleRequest.setModuleId(1);
        moduleRequests.add(moduleRequest);

        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        doNothing().when(usersAssessmentsService).saveAssessmentModules(moduleRequests, assessment);

        assessmentService.saveAssessmentModules(moduleRequests, assessment);

        verify(usersAssessmentsService).saveAssessmentModules(moduleRequests, assessment);

    }

    @Test
    void shouldUpdateModulesSelectedByUser() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest = new ModuleRequest();
        moduleRequest.setModuleId(1);
        moduleRequests.add(moduleRequest);

        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        doNothing().when(usersAssessmentsService).updateAssessmentModules(moduleRequests, assessment);

        assessmentService.updateAssessmentModules(moduleRequests, assessment);

        verify(usersAssessmentsService).updateAssessmentModules(moduleRequests, assessment);
    }


    @Test
    void shouldGetFacilitatorsSet() {
        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        Set<AssessmentUser> assessmentUser1 = new HashSet<>();
        assessmentUser1.add(assessmentUser);

        when(usersAssessmentsService.getAssessmentFacilitators(assessment)).thenReturn(assessmentUser1);
        assessmentService.getAssessmentFacilitators(assessment);

        verify(usersAssessmentsService).getAssessmentFacilitators(assessment);
    }

    @Test
    void shouldReturnListOfCompletedAssessmentsBefore30Days() {
        assessmentService.getFinishedAssessments();

        verify(assessmentRepository).findByCompletedStatus(any(Date.class));
    }

    @Test
    void shouldReturnTopicRecommendation() {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationText("text");
        RecommendationRequest recommendationRequest = new RecommendationRequest();
        Assessment assessment = new Assessment();

        when(topicAndParameterLevelAssessmentService.saveTopicRecommendation(recommendationRequest, assessment, 1)).thenReturn(topicLevelRecommendation);

        TopicLevelRecommendation topicLevelRecommendation1 = assessmentService.saveTopicRecommendation(recommendationRequest, assessment, 1);

        assertEquals("text", topicLevelRecommendation1.getRecommendationText());
    }

    @Test
    void shouldReturnParameterLevelRecommendation() {
        RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationText("text");
        parameterLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.HIGH);
        parameterLevelRecommendationRequest.setDeliveryHorizon(RecommendationDeliveryHorizon.LATER);
        Assessment assessment = new Assessment();

        ParameterLevelRecommendation parameterLevelRecommendation = modelMapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);

        when(assessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, 1)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation parameterLevelRecommendation1 = assessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, 1);

        assertEquals("text", parameterLevelRecommendation1.getRecommendationText());
    }

    @Test
    void shouldReturnUserQuestionWhenQuestionIdIsGiven() {
        UserQuestion userQuestion = new UserQuestion(1,new Assessment(),new AssessmentParameter(),"","",new Date(),new Date(),false);

        when(usersAssessmentsService.searchUserQuestion(1)).thenReturn(Optional.of(userQuestion));

        Optional<UserQuestion> actualResponse = assessmentService.searchUserQuestion(1);

        verify(usersAssessmentsService).searchUserQuestion(1);

        assertEquals(Optional.of(userQuestion),actualResponse);
    }

    @Test
    void shouldReturnAssessmentResponse() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        AssessmentUser assessmentUser = new AssessmentUser(new UserId("",assessment),AssessmentRole.Owner);
        assessment.setAssessmentUsers(Collections.singleton(assessmentUser));
        assessment.setOrganisation(new Organisation(1,"","","",1));
        Answer answer = new Answer(new AnswerId(assessment,new Question()),"",new Date(),new Date(),5);
        UserQuestion userQuestion = new UserQuestion(1,new Assessment(),new AssessmentParameter(),"","",new Date(),new Date(),false);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        AssessmentParameter assessmentParameter1 = new AssessmentParameter();
        assessmentParameter1.setParameterId(2);
        TopicLevelRating topicLevelRating = new TopicLevelRating(2,new Date(),new Date(),new TopicLevelId(assessment,assessmentTopic));
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation(assessmentTopic);
        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation(assessmentTopic1);
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        topicLevelRecommendationList.add(topicLevelRecommendation);
        topicLevelRecommendationList.add(topicLevelRecommendation1);
        ParameterLevelRating parameterLevelRating = new ParameterLevelRating(1,new Date(),new Date(),new ParameterLevelId(assessment,assessmentParameter));
        List<ParameterLevelRating> parameterLevelRatingList = new ArrayList<>();
        parameterLevelRatingList.add(parameterLevelRating);
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation(assessmentParameter);
        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation(assessmentParameter1);
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);
        parameterLevelRecommendationList.add(parameterLevelRecommendation);

        when(topicAndParameterLevelAssessmentService.getAnswers(1)).thenReturn(Collections.singletonList(answer));
        when(usersAssessmentsService.getUserQuestions(1)).thenReturn(Collections.singletonList(userQuestion));
        when(topicAndParameterLevelAssessmentService.getTopicRatings(1)).thenReturn(Collections.singletonList(topicLevelRating));
        when(topicAndParameterLevelAssessmentService.getTopicRecommendations(1)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getParameterRatings(1)).thenReturn(parameterLevelRatingList);
        when(topicAndParameterLevelAssessmentService.getParameterRecommendations(1)).thenReturn(parameterLevelRecommendationList);

        AssessmentResponse assessmentResponse = assessmentService.getAssessmentResponse(assessment);

        assertEquals(2,assessmentResponse.getParameterRatingAndRecommendation().size());
    }

    @Test
    void shouldSetValuesToAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest("name","purpose","description","name","domain","industry",2,new ArrayList<>());
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setOrganisation(new Organisation());

        Assessment assessment1 = assessmentService.setAssessment(assessment,assessmentRequest);

        assertEquals("name",assessment1.getAssessmentName());
    }

    @Test
    void shouldReturnAssessmentUsers() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        AssessmentUser assessmentUser = new AssessmentUser(new UserId("abc@thoughtworks.com",assessment),AssessmentRole.Owner);
        AssessmentUser assessmentUser1 = new AssessmentUser(new UserId("cde@thoughtworks.com",assessment),AssessmentRole.Facilitator);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        List<UserDto> assessmentUserList = new ArrayList<>();
        assessmentUserList.add(new UserDto("abc@thoughtworks.com",UserRole.Owner));
        assessmentUserList.add(new UserDto("cde@thoughtworks.com",UserRole.Facilitator));
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        AssessmentRequest assessmentRequest = new AssessmentRequest("name","purpose","description","name","domain","industry",2,assessmentUserList);
        User user = new User("1",new UserInfo("abc@thoughtworks.com","Abc","abc",""),"");


        assessment.setAssessmentUsers(new HashSet<>());

        Set<AssessmentUser> assessmentUsers1 = assessmentService.getAssessmentUsers(assessmentRequest,user,assessment);

        assertEquals(2,assessmentUsers1.size());
    }
}

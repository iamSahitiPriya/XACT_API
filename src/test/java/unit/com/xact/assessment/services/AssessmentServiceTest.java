/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.services.*;
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
    private AccessControlService accessControlService;

    private static final ModelMapper modelMapper = new ModelMapper();


    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        assessmentMasterDataService = mock(AssessmentMasterDataService.class);
        topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
        accessControlService = mock(AccessControlService.class);
        assessmentService = new AssessmentService(assessmentRepository, usersAssessmentsService, accessControlService, assessmentMasterDataService, topicAndParameterLevelAssessmentService);
    }

    @Test
    void shouldAddUsersToAssessment() {
        String email = "abc@thoughtworks.com";
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

//    @Test
//    void shouldReturnAssessmentUsersListWithParticularAssessmentId() {
//        List<AssessmentUser> assessmentUsersList = new ArrayList<>();
//
//        Integer assessmentId = 1;
//
//        Assessment assessment = new Assessment();
//        assessment.setAssessmentId(assessmentId);
//        assessment.setAssessmentStatus(AssessmentStatus.Completed);
//
//        UserId userId1 = new UserId("hello@gmail.com", assessment);
//        AssessmentUser assessmentUsers1 = new AssessmentUser(userId1, AssessmentRole.Facilitator);
//        assessmentUsersList.add(assessmentUsers1);
//
//        UserId userId2 = new UserId("new@gmail.com", assessment);
//        AssessmentUser assessmentUsers2 = new AssessmentUser(userId2, AssessmentRole.Facilitator);
//        assessmentUsersList.add(assessmentUsers2);
//
//        List<String> expectedAssessmentUsersList = new ArrayList<>();
//        for (AssessmentUser eachUser : assessmentUsersList) {
//            expectedAssessmentUsersList.add(eachUser.getUserId().getUserEmail());
//        }
//        when(usersAssessmentsService.getAssessmentFacilitators(assessmentId)).thenReturn(expectedAssessmentUsersList);
//        List<String> actualResponse = assessmentService.getAssessmentFacilitators(assessmentId);
//        assertEquals(expectedAssessmentUsersList, actualResponse);
//
//    }

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

        doNothing().when(usersAssessmentsService).saveUserAnswer(userQuestion.getQuestionId(), userQuestion.getAnswer());
        assessmentService.saveUserAnswer(userQuestion.getQuestionId(), userQuestion.getAnswer());

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

        when(topicAndParameterLevelAssessmentService.saveTopicRecommendation(recommendationRequest,assessment,1)).thenReturn(topicLevelRecommendation);

        TopicLevelRecommendation topicLevelRecommendation1 = assessmentService.saveTopicRecommendation(recommendationRequest,assessment,1);

        assertEquals("text", topicLevelRecommendation1.getRecommendationText());
    }

    @Test
    void shouldReturnParameterLevelRecommendation() {
        RecommendationRequest parameterLevelRecommendationRequest=new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationText("text");
        parameterLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.HIGH);
        parameterLevelRecommendationRequest.setDeliveryHorizon(RecommendationDeliveryHorizon.LATER);

        Assessment assessment = new Assessment();


        ParameterLevelRecommendation parameterLevelRecommendation=modelMapper.map(parameterLevelRecommendationRequest,ParameterLevelRecommendation.class);

        when(assessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment,1)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation parameterLevelRecommendation1=assessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment,1);

        assertEquals("text",parameterLevelRecommendation1.getRecommendationText());
    }
    @Test
    void shouldGetInactiveAssessments() {
        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        List<Assessment> assessmentList = new ArrayList<>();
        assessmentList.add(assessment);

        when(assessmentRepository.findInactiveAssessments(any(Date.class))).thenReturn(assessmentList);
        List<Assessment> inactiveAssessments = assessmentService.findInactiveAssessments(15);

        verify(assessmentRepository).findInactiveAssessments(any(Date.class));
        assertEquals(1,inactiveAssessments.size());


    }
}

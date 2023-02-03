/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ModuleRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UserAssessmentModuleRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.ModuleService;
import com.xact.assessment.services.UserQuestionService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsersAssessmentsServiceTest {

    private UsersAssessmentsRepository usersAssessmentsRepository;
    private UsersAssessmentsService usersAssessmentsService;

    private UserAssessmentModuleRepository userAssessmentModuleRepository;

    private UserQuestionService userQuestionService;
    private ModuleService moduleService;


    @BeforeEach
    public void beforeEach() {
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        userAssessmentModuleRepository = mock(UserAssessmentModuleRepository.class);
        userQuestionService = mock(UserQuestionService.class);
        moduleService = mock(ModuleService.class);
        usersAssessmentsService = new UsersAssessmentsService(usersAssessmentsRepository, userAssessmentModuleRepository, userAssessmentModuleService, userQuestionService, moduleService);
    }

    @Test
    void shouldFetchAllAssessmentDetails() {

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);
        String userEmail = "hello@thoughtworks.com";

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(Collections.singletonList(assessmentUser));
        List<Assessment> actualAssessments = usersAssessmentsService.findAssessments(userEmail);

        assertEquals(Collections.singletonList(assessment), actualAssessments);

        verify(usersAssessmentsRepository).findByUserEmail(userEmail);
    }

    @Test
    void shouldSaveAssessmentUsersDetails() {

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        Set<AssessmentUser> users = new HashSet<>();
        users.add(assessmentUser);

        when(usersAssessmentsRepository.saveAll(users)).thenReturn(users);
        usersAssessmentsService.createUsersInAssessment(users);

        verify(usersAssessmentsRepository).saveAll(users);
    }

    @Test
    void shouldUpdateUserInAssessment() {
        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);

        List<AssessmentUser> assessmentUser1 = new ArrayList<>();
        assessmentUser1.add(assessmentUser);

        doNothing().when(usersAssessmentsRepository).deleteUsersByAssessmentId(assessment.getAssessmentId());
        usersAssessmentsService.updateUsersInAssessment(assessmentUsers,assessment.getAssessmentId());

        verify(usersAssessmentsRepository).deleteUsersByAssessmentId(assessment.getAssessmentId());

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

        AssessmentModule assessmentModule1 = new AssessmentModule();
        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        when(moduleService.getModule(moduleRequest.getModuleId())).thenReturn(assessmentModule1);

        AssessmentModule assessmentModule = moduleService.getModule(moduleRequest.getModuleId());
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        when(userAssessmentModuleRepository.save(userAssessmentModule)).thenReturn(userAssessmentModule);

        usersAssessmentsService.saveAssessmentModules(moduleRequests, assessment);

        verify(userAssessmentModuleRepository).save(userAssessmentModule);

    }


    @Test
    void shouldUpdateUserSelectedModules() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest = new ModuleRequest();
        moduleRequest.setModuleId(2);
        moduleRequests.add(moduleRequest);

        AssessmentModule assessmentModule1 = new AssessmentModule();
        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        when(moduleService.getModule(moduleRequest.getModuleId())).thenReturn(assessmentModule1);

        AssessmentModule assessmentModule = moduleService.getModule(moduleRequest.getModuleId());
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        when(userAssessmentModuleRepository.save(userAssessmentModule)).thenReturn(userAssessmentModule);

        usersAssessmentsService.updateAssessmentModules(moduleRequests, assessment);

        verify(userAssessmentModuleRepository).save(userAssessmentModule);
    }

    @Test
    void shouldDeleteUserSelectedModule() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        List<ModuleRequest> moduleRequests = new ArrayList<>();
        ModuleRequest moduleRequest = new ModuleRequest();
        moduleRequest.setModuleId(2);
        moduleRequests.add(moduleRequest);

        AssessmentModule assessmentModule1 = new AssessmentModule();
        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        when(moduleService.getModule(moduleRequest.getModuleId())).thenReturn(assessmentModule1);

        AssessmentModule assessmentModule = moduleService.getModule(moduleRequest.getModuleId());
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        when(userAssessmentModuleRepository.save(userAssessmentModule)).thenReturn(userAssessmentModule);
        doNothing().when(userAssessmentModuleRepository).deleteByModule(assessment.getAssessmentId());
        usersAssessmentsService.updateAssessmentModules(moduleRequests, assessment);

        verify(userAssessmentModuleRepository).deleteByModule(assessment.getAssessmentId());
    }

    @Test
    void shouldBeAbleToSaveUserQuestionAtTimeOfCreation() {
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

        when(userQuestionService.saveUserQuestion(assessment, assessmentParameter.getParameterId(), "question Text?")).thenReturn(userQuestion);
        usersAssessmentsService.saveUserQuestion(assessment, assessmentParameter.getParameterId(), "question Text?");

        verify(userQuestionService).saveUserQuestion(assessment, assessmentParameter.getParameterId(), "question Text?");

    }
    @Test
    void shouldBeAbleToUpdateUserAnswerText() {
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

        userQuestion.setAnswer("answer Text?");
        when(userQuestionService.saveUserAnswer(userQuestion.getQuestionId(),"answer Text")).thenReturn(userQuestion);

        usersAssessmentsService.saveUserAnswer(userQuestion.getQuestionId(),"answer Text");
        verify(userQuestionService).saveUserAnswer(userQuestion.getQuestionId(),"answer Text");

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
        when(userQuestionService.updateUserQuestion(userQuestion.getQuestionId(),"updated question Text?")).thenReturn(userQuestion);
        usersAssessmentsService.updateUserQuestion(userQuestion.getQuestionId(),"updated question Text?");

        verify(userQuestionService).updateUserQuestion(userQuestion.getQuestionId(),"updated question Text?");

    }
    @Test
    void shouldDeleteUserAddedQuestionAndAnswer() {
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

        doNothing().when(userQuestionService).deleteUserQuestion(userQuestion.getQuestionId());
        usersAssessmentsService.deleteUserQuestion(userQuestion.getQuestionId());

        verify(userQuestionService).deleteUserQuestion(userQuestion.getQuestionId());

    }

    @Test
    void shouldGetFacilitators() {
        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        List<AssessmentUser> assessmentUser1 = new ArrayList<>();
        assessmentUser1.add(assessmentUser);

        when(usersAssessmentsRepository.findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator)).thenReturn(assessmentUser1);

        usersAssessmentsService.getAssessmentFacilitators(assessment.getAssessmentId());

        verify(usersAssessmentsRepository).findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator);


    }
    @Test
    void shouldGetFacilitatorsSet() {
        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUser assessmentUser = new AssessmentUser(userId, AssessmentRole.Owner);

        List<AssessmentUser> assessmentUser1 = new ArrayList<>();
        assessmentUser1.add(assessmentUser);

        when(usersAssessmentsRepository.findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator)).thenReturn(assessmentUser1);

        usersAssessmentsService.getAssessmentFacilitatorsSet(assessment);

        verify(usersAssessmentsRepository).findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator);


    }
}

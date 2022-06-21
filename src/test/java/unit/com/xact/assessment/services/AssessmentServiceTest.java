/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.AssessmentsRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {
    private UsersAssessmentsService usersAssessmentsService;
    private AssessmentService assessmentService;
    private AssessmentRepository assessmentRepository;
    private UsersAssessmentsRepository usersAssessmentsRepository;
    private AssessmentsRepository assessmentsRepository;

    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        assessmentsRepository = mock(AssessmentsRepository.class);
        assessmentService = new AssessmentService(usersAssessmentsService, assessmentRepository, usersAssessmentsRepository, assessmentsRepository);
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
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);


        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        Assessment assessment = new Assessment();
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        assessment.setAssessmentName("assessment1");
        Organisation organisation = new Organisation();
        organisation.setOrganisationName("org1");
        organisation.setDomain("IT");
        organisation.setIndustry("IT");
        organisation.setSize(1);
        assessment.setOrganisation(organisation);


        UserId userId1 = new UserId("test@email.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Owner);
        assessmentUsers.add(assessmentUsers1);


        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        doNothing().when(usersAssessmentsService).createUsersInAssessment(assessmentUsers);


        Assessment actualAssessment = assessmentService.createAssessment(assessmentRequest, loggedinUser);

        assertEquals(assessment.getAssessmentName(), actualAssessment.getAssessmentName());
        assertEquals(assessment.getAssessmentStatus(), actualAssessment.getAssessmentStatus());
    }

    @Test
    void getAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment mockAssessment = new Assessment();
        mockAssessment.setAssessmentId(assessmentId);
        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(loggedinUser.getUserEmail(), mockAssessment);
        assessmentUser.setUserId(userId);

        when(usersAssessmentsRepository.findByUserEmail(loggedinUser.getUserEmail(), assessmentId)).thenReturn(assessmentUser);

        Assessment assessment = assessmentService.getAssessment(assessmentId, loggedinUser);

        assertEquals(assessment, mockAssessment);
    }

    @Test
    void finishAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Assessment actualAsessment = assessmentService.finishAssessment(assessment);

        assertEquals(actualAsessment.getAssessmentStatus(), AssessmentStatus.Completed);
    }

    @Test
    void reopenAssessment() {
        Integer assessmentId = 123;
        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        Assessment actualAsessment = assessmentService.reopenAssessment(assessment);

        assertEquals(actualAsessment.getAssessmentStatus(), AssessmentStatus.Active);
    }


}

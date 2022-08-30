/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.repositories.AssessmentRepository;
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
    private AccessControlRepository accessControlRepository;

    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        accessControlRepository = mock(AccessControlRepository.class);
        assessmentService = new AssessmentService(usersAssessmentsService, assessmentRepository, usersAssessmentsRepository, accessControlRepository);
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
        assessment.setAssessmentId(123);
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
        assessment.setAssessmentName("assessmentName");
        assessment.setAssessmentStatus(AssessmentStatus.Active);
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.finishAssessment(assessment);

        assertEquals(AssessmentStatus.Completed,actualAssessment.getAssessmentStatus());
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
        assessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setAssessmentId(assessmentId);
        expectedAssessment.setAssessmentName("assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        when(assessmentRepository.update(assessment)).thenReturn(assessment);

        Assessment actualAssessment = assessmentService.reopenAssessment(assessment);

        assertEquals( AssessmentStatus.Active,actualAssessment.getAssessmentStatus());
    }

    @Test
    void shouldReturnAssessmentUsersListWithParticularAssessmentId() {
        List<AssessmentUsers> assessmentUsersList = new ArrayList<>();

        Integer assessmentId = 1;

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Facilitator);
        assessmentUsersList.add(assessmentUsers1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, AssessmentRole.Facilitator);
        assessmentUsersList.add(assessmentUsers2);

        List<String> expectedAssessmentUsersList = new ArrayList<>();
        for (AssessmentUsers eachUser : assessmentUsersList) {
            expectedAssessmentUsersList.add(eachUser.getUserId().getUserEmail());
        }
        when(usersAssessmentsRepository.findUserByAssessmentId(assessmentId, AssessmentRole.Facilitator)).thenReturn(assessmentUsersList);
        List<String> actualResponse = assessmentService.getAssessmentUsers(assessmentId);
        assertEquals(expectedAssessmentUsersList, actualResponse);

    }

    @Test
    void shouldUpdateAssessment() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        when(assessmentRepository.save(assessment)).thenReturn(assessment);

        assessment.setAssessmentName("New Assessment");

        when(assessmentRepository.update(assessment)).thenReturn(assessment);
        Set<AssessmentUsers> assessmentUsersSet = new HashSet<>(Set.of());
        UserId userId1 = new UserId("hello@gmail.com", assessment);
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, AssessmentRole.Facilitator);
        assessmentUsersSet.add(assessmentUsers1);

        UserId userId2 = new UserId("new@gmail.com", assessment);
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, AssessmentRole.Facilitator);
        assessmentUsersSet.add(assessmentUsers2);

        doNothing().when(usersAssessmentsService).updateUsersInAssessment(assessmentUsersSet, assessment.getAssessmentId());

        assessmentService.updateAssessment(assessment, assessmentUsersSet);

        User user = new User();
        Profile profile = new Profile();
        profile.setEmail("hello@email.com");
        user.setProfile(profile);
        when(usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), 1)).thenReturn(assessmentUsers1);

        Assessment expectedAssessment = assessmentService.getAssessment(1, user);

        assertEquals(expectedAssessment.getAssessmentName(), assessment.getAssessmentName());

    }


}
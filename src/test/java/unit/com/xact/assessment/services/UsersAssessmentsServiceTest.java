/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsersAssessmentsServiceTest {

    private UsersAssessmentsRepository usersAssessmentsRepository;
    private UsersAssessmentsService usersAssessmentsService;


    @BeforeEach
    public void beforeEach() {
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        usersAssessmentsService = new UsersAssessmentsService(usersAssessmentsRepository, userAssessmentModuleRepository, userQuestionService, moduleService);
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

        List<AssessmentUser> assessmentUser1 = new ArrayList<>();
        assessmentUser1.add(assessmentUser);

        doNothing().when(usersAssessmentsRepository).deleteUsersByAssessmentId(assessment.getAssessmentId());

        usersAssessmentsRepository.deleteUsersByAssessmentId(assessment.getAssessmentId());
        verify(usersAssessmentsRepository).deleteUsersByAssessmentId(assessment.getAssessmentId());

    }
}

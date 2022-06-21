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
        usersAssessmentsService = new UsersAssessmentsService(usersAssessmentsRepository);
    }

    @Test
    void shouldFetchAllAssessmentDetails() {

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);
        String userEmail = "hello@thoughtworks.com";

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUsers assessmentUsers = new AssessmentUsers(userId, AssessmentRole.Owner);

        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(Collections.singletonList(assessmentUsers));
        List<Assessment> actualAssessments = usersAssessmentsService.findAssessments(userEmail);

        assertEquals(Collections.singletonList(assessment), actualAssessments);

        verify(usersAssessmentsRepository).findByUserEmail(userEmail);
    }

    @Test
    void shouldSaveAssessmentUsersDetails() {

        Date created = new Date(22 - 10 - 2022);
        Date updated = new Date(22 - 10 - 2022);

        Organisation organisation = new Organisation(1, "Thoughtworks", "IT", "Consultant", 10);
        Assessment assessment = new Assessment(1, "xact", organisation, AssessmentStatus.Active, created, updated);
        UserId userId = new UserId("hello@thoughtworks.com", assessment);
        AssessmentUsers assessmentUsers = new AssessmentUsers(userId, AssessmentRole.Owner);

        Set<AssessmentUsers> users = new HashSet<>();
        users.add(assessmentUsers);

        when(usersAssessmentsRepository.saveAll(users)).thenReturn(users);
        usersAssessmentsService.createUsersInAssessment(users);

        verify(usersAssessmentsRepository).saveAll(users);
    }


}

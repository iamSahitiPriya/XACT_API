package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        AssessmentUsers assessmentUsers = new AssessmentUsers(userId, "hello", "world", AssessmentRole.Owner);

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
        AssessmentUsers assessmentUsers = new AssessmentUsers(userId, "hello", "world", AssessmentRole.Owner);

        List<AssessmentUsers> users = Collections.singletonList(assessmentUsers);

        when(usersAssessmentsRepository.saveAll(users)).thenReturn(users);
        List<AssessmentUsers> actualAssessmentUsers = usersAssessmentsService.createUsersInAssessment(users);

        assertEquals(users, actualAssessmentUsers);

        verify(usersAssessmentsRepository).saveAll(users);
    }


}

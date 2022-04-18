//package unit.com.xact.services;
//
//import com.xact.assessment.models.Assessment;
//import com.xact.assessment.models.AssessmentUsers;
//import com.xact.assessment.models.Organisation;
//import com.xact.assessment.models.UserId;
//import com.xact.assessment.repositories.UsersAssessmentsRepository;
//import com.xact.assessment.services.UsersAssessmentsService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UsersAssessmentsServiceTest {
//
//    private UsersAssessmentsRepository usersAssessmentsRepository;
//    private UsersAssessmentsService usersAssessmentsService;
//
//
//    @BeforeEach
//    public void beforeEach() {
//        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
//        usersAssessmentsService = new UsersAssessmentsService(usersAssessmentsRepository);
//    }
//
//    @Test
//    public void shouldFetchAllAssessmentDetails() {
//
//        Date created = new Date(22 - 10 - 2022);
//        Date updated = new Date(22 - 10 - 2022);
//        String userEmail = "hello@thoughtworks.com";
//
//        Organisation organisation = new Organisation(1L, "Thoughtworks", "IT", "Consultant", 10);
//        Assessment assessment = new Assessment(1L, "xact", organisation, "TWI", "ACTIVE", created, updated);
//        UserId userId = new UserId("hello@thoughtworks.com", assessment);
//        AssessmentUsers assessmentUsers = new AssessmentUsers(userId, "hello", "world", "Owner");
//
//        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(Collections.singletonList(assessmentUsers));
//        List<Assessment> actualAssessments = usersAssessmentsService.findAssessments(userEmail);
//
//        assertEquals(Collections.singletonList(assessment),actualAssessments);
//
//        verify(usersAssessmentsRepository).findByUserEmail(userEmail);
//    }
//}

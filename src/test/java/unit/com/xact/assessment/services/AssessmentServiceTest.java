package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import unit.com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AssessmentServiceTest {
    private UsersAssessmentsService usersAssessmentsService;
    private AssessmentService assessmentService;
    private AssessmentRepository assessmentRepository;


    @BeforeEach
    public void beforeEach() {
        usersAssessmentsService = mock(UsersAssessmentsService.class);
        assessmentRepository = mock(AssessmentRepository.class);
        assessmentService = new AssessmentService(usersAssessmentsService, assessmentRepository);
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
        UserDto user = new UserDto("test@email.com", "first-name", "last-name", UserRole.Owner);
        users.add(user);
        assessmentRequest.setUsers(users);

        User loggedinUser = new User();
        Profile profile = new Profile();
        profile.setEmail("test@email.com");
        loggedinUser.setProfile(profile);


        List<AssessmentUsers> assessmentUsers = new ArrayList<>();
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
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, "first-name", "last-name", AssessmentRole.Owner);
        assessmentUsers.add(assessmentUsers1);


        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        when(usersAssessmentsService.createUsersInAssessment(assessmentUsers)).thenReturn(assessmentUsers);

        Assessment actualAssessment = assessmentService.createAssessment(assessmentRequest, loggedinUser);

        assertEquals(assessment.getAssessmentName(), actualAssessment.getAssessmentName());
        assertEquals(assessment.getAssessmentStatus(), actualAssessment.getAssessmentStatus());
    }

}

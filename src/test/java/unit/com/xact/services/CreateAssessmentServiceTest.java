package unit.com.xact.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentsRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.CreateAssessmentService;
import com.xact.assessment.services.OrganisationService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateAssessmentServiceTest {
    private AssessmentsRepository assessmentsRepository;
    private OrganisationService organisationService;
    private UsersAssessmentsRepository usersAssessmentsRepository;
    private UsersAssessmentsService usersAssessmentsService;
    private CreateAssessmentService createAssessmentService;


    @BeforeEach
    public void beforeEach() {
        assessmentsRepository = mock(AssessmentsRepository.class);
        usersAssessmentsRepository = mock(UsersAssessmentsRepository.class);
        usersAssessmentsService = new UsersAssessmentsService(usersAssessmentsRepository);
        organisationService = mock(OrganisationService.class);
        createAssessmentService = new CreateAssessmentService(assessmentsRepository, organisationService, usersAssessmentsService);
    }

    @Test
    public void shouldCreateNewAssessment() {

        List<UserDto> assessmentUsers = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();


        UserDto userDto1 = new UserDto("test@email.com", "first-name", "last-name", UserRole.Facilitator);
        assessmentUsers.add(userDto1);

        UserDto userDto2 = new UserDto("test2@email.com", "first2-name", "last2-name", UserRole.Facilitator);
        assessmentUsers.add(userDto2);
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("test-assessment");
        assessmentRequest.setOrganisationName("test-organisation");
        assessmentRequest.setDomain("test-domain");
        assessmentRequest.setIndustry("test-industry");
        assessmentRequest.setTeamSize(10);
        assessmentRequest.setUsers(assessmentUsers);

        Assessment actualCreatedAssessment = createAssessmentService.createAssessment(assessmentRequest, new User("test@email.com", "first-name", "last-name"));

        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.ACTIVE);

        when(assessmentsRepository.save(assessment)).thenReturn(assessment);

        assertEquals(userDto1.getRole(), UserRole.Owner);
        assertEquals(actualCreatedAssessment.getAssessmentName(), assessment.getAssessmentName());
        assertEquals(actualCreatedAssessment.getAssessmentStatus(), assessment.getAssessmentStatus());
        assertEquals(actualCreatedAssessment.getOrganisation().getOrganisationName(), assessment.getOrganisation().getOrganisationName());

        verify(assessmentsRepository).save(actualCreatedAssessment);

    }

    @Test
    void shouldAddUsersToAssessment() {
        List<AssessmentUsers> assessmentUsers = new ArrayList<>();


        UserId userId1 = new UserId("test@email.com", new Assessment());
        AssessmentUsers assessmentUsers1 = new AssessmentUsers(userId1, "first-name", "last-name", AssessmentRole.Facilitator);
        assessmentUsers.add(assessmentUsers1);

        UserId userId2 = new UserId("test-2@email.com", new Assessment());
        AssessmentUsers assessmentUsers2 = new AssessmentUsers(userId2, "first2-name", "last2-name", AssessmentRole.Facilitator);
        assessmentUsers.add(assessmentUsers2);


        when(usersAssessmentsRepository.saveAll(assessmentUsers)).thenReturn(assessmentUsers);

        List<AssessmentUsers> actualAssessmentUsers = usersAssessmentsService.createUsersInAssessment(assessmentUsers);

        assertEquals(assessmentUsers, actualAssessmentUsers);

        verify(usersAssessmentsRepository).saveAll(assessmentUsers);


    }

}

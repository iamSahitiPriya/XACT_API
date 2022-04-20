package unit.com.xact.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentStatus;
import com.xact.assessment.models.Organisation;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AuthService;
import com.xact.assessment.services.CreateAssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentControllerTest {


    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UsersAssessmentsService usersAssessmentsService = Mockito.mock(UsersAssessmentsService.class);
    private final AuthService authService = Mockito.mock(AuthService.class);
    private final CreateAssessmentService createAssessmentService = Mockito.mock(CreateAssessmentService.class);
    private final AssessmentController assessmentController = new AssessmentController(usersAssessmentsService, authService, createAssessmentService);

    @Test
    public void testGetAssessment() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        user.setEmail(userEmail);
        Organisation organisation = new Organisation(2L, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(1L, "xact", organisation, AssessmentStatus.ACTIVE, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
        when(authService.getLoggedInUser(authentication)).thenReturn(user);
        AssessmentResponse expectedAssessment = new AssessmentResponse();
        expectedAssessment.setAssessmentId(1L);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.ACTIVE);
        expectedAssessment.setUpdatedAt(updated);
        HttpResponse<List<AssessmentResponse>> actualAssessments = assessmentController.getAssessments(authentication);

        assertEquals(expectedAssessment.getAssessmentId(), actualAssessments.body().get(0).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessments.body().get(0).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessments.body().get(0).getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), actualAssessments.body().get(0).getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessments.body().get(0).getUpdatedAt());
        verify(usersAssessmentsService).findAssessments(userEmail);
    }

    @Test
    public void testCreateAssessment() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        assessmentRequest.setAssessmentName("Dummy assessment");
        assessmentRequest.setDomain("Dummy domain");
        assessmentRequest.setIndustry("Dummy Industry");
        assessmentRequest.setOrganisationName("Dummy Org");
        assessmentRequest.setTeamSize(10);
        UserDto userDto = new UserDto();
        userDto.setEmail("dummy@thoughtworks.com");
        userDto.setFirstName("Dummy FN");
        userDto.setLastName("Dummy LN");
        userDto.setRole(UserRole.Owner);
        List<UserDto> users = Collections.singletonList(userDto);
        assessmentRequest.setUsers(users);
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        user.setEmail(userEmail);
        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2L, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(1L, "xact", organisation, AssessmentStatus.ACTIVE, created, updated);


        when(authService.getLoggedInUser(authentication)).thenReturn(user);
        when(createAssessmentService.createAssessment(assessmentRequest, user)).thenReturn(assessment);

        HttpResponse<AssessmentResponse> actualAssessments = assessmentController.createAssessment(assessmentRequest, authentication);

        System.out.println(actualAssessments.body().getOrganisationName());
        assertNotNull(actualAssessments.body().getAssessmentId());
        assertEquals(assessment.getAssessmentName(), actualAssessments.body().getAssessmentName());
        assertEquals(assessment.getAssessmentStatus().name(), actualAssessments.body().getAssessmentStatus().name());
        assertEquals(assessment.getOrganisation().getOrganisationName(), actualAssessments.body().getOrganisationName());
    }
}

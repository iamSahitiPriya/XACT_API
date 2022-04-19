package unit.com.xact.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.dtos.AssessmentResponseDto;
import com.xact.assessment.dtos.AssessmentStatusDto;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentStatus;
import com.xact.assessment.models.Organisation;
import com.xact.assessment.services.UsersAssessmentsService;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentControllerTest {


    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UsersAssessmentsService usersAssessmentsService = Mockito.mock(UsersAssessmentsService.class);
    private final AssessmentController assessmentController = new AssessmentController(usersAssessmentsService);

    @Test
    public void testGetAssessmentOpen() {

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        String userEmail = "hello@thoughtworks.com";
        Organisation organisation = new Organisation(2L, "abc", "hello", "ABC", 4);
        Assessment assessment = new Assessment(1L, "xact", organisation, "Project for xact", AssessmentStatus.ACTIVE, created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
        when(authentication.getAttributes()).thenReturn(authMap);
        AssessmentResponseDto expectedAssessment = new AssessmentResponseDto();
        expectedAssessment.setAssessmentId(1L);
        expectedAssessment.setAssessmentName("xact");
        expectedAssessment.setOrganisationName("abc");
        expectedAssessment.setAssessmentStatus(AssessmentStatusDto.ACTIVE);
        expectedAssessment.setUpdatedAt(updated);
        List<AssessmentResponseDto> actualAssessments = assessmentController.getAssessments(authentication);


        assertEquals(expectedAssessment.getAssessmentId(), actualAssessments.get(0).getAssessmentId());
        assertEquals(expectedAssessment.getAssessmentName(), actualAssessments.get(0).getAssessmentName());
        assertEquals(expectedAssessment.getAssessmentStatus(), actualAssessments.get(0).getAssessmentStatus());
        assertEquals(expectedAssessment.getOrganisationName(), actualAssessments.get(0).getOrganisationName());
        assertEquals(expectedAssessment.getUpdatedAt(), actualAssessments.get(0).getUpdatedAt());
        verify(usersAssessmentsService).findAssessments(userEmail);
    }
}

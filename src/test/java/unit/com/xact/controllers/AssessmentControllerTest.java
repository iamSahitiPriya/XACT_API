package unit.com.xact.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.models.Assessment;
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
        Assessment assessment = new Assessment(1L, "xact", organisation, "Project for xact", "ACTIVE", created, updated);
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", userEmail);
        when(usersAssessmentsService.findAssessments(userEmail)).thenReturn(Collections.singletonList(assessment));
        when(authentication.getAttributes()).thenReturn(authMap);

        List<Assessment> actualAssessments = assessmentController.getAssessments(authentication);


        assertEquals(Collections.singletonList(assessment), actualAssessments);
        verify(usersAssessmentsService).findAssessments(userEmail);
    }
}

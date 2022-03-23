package unit.com.xact.controllers;

import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.AssessmentService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class AssessmentControllerTest {


    private Authentication authentication = Mockito.mock(Authentication.class);
    private AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private AssessmentController assessmentController = new AssessmentController(assessmentService);


    @Test
    public void testGetAssessmentSecure() {
        Map<String, Object> authAttributes = new HashMap<>();
        authAttributes.put("name","dummy-user");
        authAttributes.put("email","dummy-user@tw.com");
        when(authentication.getAttributes()).thenReturn(authAttributes);

        HttpResponse<Assessment> assessmentResponse = assessmentController.getAssessmentSecure("assessment-id-123",authentication);

        assertNotNull(assessmentResponse);
    }

    @Test
    public void testGetAssessmentOpen() {
        Assessment assessment = new Assessment();
        assessment.setName("Assessment name");
        when(assessmentService.getAssessmentById("assessment-id-123")).thenReturn(assessment);

        HttpResponse<Assessment> assessmentResponse = assessmentController.getAssessmentOpen("assessment-id-123");

        assertEquals(assessmentResponse.body().getName(),assessment.getName());
    }
}

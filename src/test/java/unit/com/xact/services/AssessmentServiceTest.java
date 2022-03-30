package unit.com.xact.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.AssessmentService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AssessmentServiceTest {

    AssessmentService assessmentService = new AssessmentService();

    @Test
    void getAssessmentById() {
        Assessment expectedAssessment = new Assessment();
        expectedAssessment.setName("Created an anonymous endpoint assessment-id-123");

        Assessment assessment = assessmentService.getAssessmentById("assessment-id-123");

        assertThat(assessment.getName(), is(expectedAssessment.getName()));
    }
}

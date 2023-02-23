package unit.com.xact.assessment.models;

import com.xact.assessment.models.*;
import org.junit.jupiter.api.Test;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static org.junit.jupiter.api.Assertions.assertEquals;

 class ParameterLevelRecommendationTest {
    ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
    @Test
    void hasNullRecommendation() {
        ParameterLevelRecommendation parameterLevelRecommendation= new ParameterLevelRecommendation();
        assertEquals(false, parameterLevelRecommendation.hasRecommendation());
    }


    @Test
    void hasRecommendationText()
    {
        ParameterLevelRecommendation parameterLevelRecommendation= new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        parameterLevelRecommendation.setAssessment(assessment);
        AssessmentParameter assessmentParameter=new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendation("some text");
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }

}

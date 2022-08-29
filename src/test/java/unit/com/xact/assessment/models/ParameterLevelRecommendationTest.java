package unit.com.xact.assessment.models;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.ParameterLevelRecommendation;
import com.xact.assessment.models.RecommendationImpact;
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
    void hasNullRecommendationEffort(){
        assertEquals(false, parameterLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasNullRecommendationImpact(){
        assertEquals(false, parameterLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasNullRecommendationDeliveryHorizon(){
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
    @Test
    void hasRecommendationImpact()
    {
        ParameterLevelRecommendation parameterLevelRecommendation= new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        parameterLevelRecommendation.setAssessment(assessment);
        AssessmentParameter assessmentParameter=new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }
    @Test
    void hasRecommendationEffort()
    {
        ParameterLevelRecommendation parameterLevelRecommendation= new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        parameterLevelRecommendation.setAssessment(assessment);
        AssessmentParameter assessmentParameter=new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }
    @Test
    void hasRecommendationDeliveryHorizon()
    {
        ParameterLevelRecommendation parameterLevelRecommendation= new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        parameterLevelRecommendation.setAssessment(assessment);
        AssessmentParameter assessmentParameter=new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setDeliveryHorizon("some text");
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }
    @Test
    void hasRecommendation()
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
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setDeliveryHorizon("some");
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }
}

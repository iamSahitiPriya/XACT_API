/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.ParameterLevelRecommendation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class ParameterLevelRecommendationTest {
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
        parameterLevelRecommendation.setRecommendationText("some text");
        assertEquals(true,parameterLevelRecommendation.hasRecommendation());
    }

}

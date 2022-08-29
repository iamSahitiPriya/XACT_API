package unit.com.xact.assessment.models;

import com.xact.assessment.models.ParameterLevelRecommendation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterLevelRecommendationTest {
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

}

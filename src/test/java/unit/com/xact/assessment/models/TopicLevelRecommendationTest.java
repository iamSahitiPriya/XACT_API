package unit.com.xact.assessment.models;


import com.xact.assessment.models.TopicLevelRecommendation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TopicLevelRecommendationTest {

    TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
    @Test
    void hasNullRecommendation() {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        assertEquals(false, topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasNullRecommendationEffort(){
        assertEquals(false, topicLevelRecommendation.hasRecommendation());
    }


}

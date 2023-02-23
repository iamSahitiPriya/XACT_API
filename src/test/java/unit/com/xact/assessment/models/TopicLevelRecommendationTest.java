package unit.com.xact.assessment.models;


import com.xact.assessment.models.*;
import org.junit.jupiter.api.Test;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicLevelRecommendationTest {

    @Test
    void hasNullRecommendation() {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        assertEquals(false, topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendationText() {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendation("some text");
        assertEquals(true, topicLevelRecommendation.hasRecommendation());
    }


}

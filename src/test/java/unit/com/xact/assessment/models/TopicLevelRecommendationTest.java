package unit.com.xact.assessment.models;


import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.models.RecommendationImpact;
import com.xact.assessment.models.TopicLevelRecommendation;
import org.junit.jupiter.api.Test;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TopicLevelRecommendationTest {

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

    @Test
    void hasNullRecommendationImpact(){
        assertEquals(false, topicLevelRecommendation.hasRecommendation());
    }
    @Test
    void hasNullRecommendationDeliveryHorizon(){
        assertEquals(false, topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendationEffort()
    {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        assertEquals(true,topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendationImpact()
    {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        assertEquals(true,topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendationText()
    {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setRecommendation("some text");
        assertEquals(true,topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendationDeliveryHoriozon()
    {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setDeliveryHorizon("some text");
        assertEquals(true,topicLevelRecommendation.hasRecommendation());
    }

    @Test
    void hasRecommendation()
    {
        TopicLevelRecommendation topicLevelRecommendation= new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        Assessment assessment=new Assessment();
        assessment.setAssessmentId(1);
        topicLevelRecommendation.setAssessment(assessment);
        AssessmentTopic assessmentTopic=new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        topicLevelRecommendation.setTopic(assessmentTopic);
        topicLevelRecommendation.setDeliveryHorizon("some text");
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("text");
        topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        assertEquals(true,topicLevelRecommendation.hasRecommendation());
    }


}

package unit.com.xact.assessment.models;

import com.xact.assessment.models.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssessmentCategoryTest {
    @Test
    void shouldReturnCategoryAverageScore(){
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("Category One");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module Name");
        assessmentModule.setCategory(assessmentCategory);

        Set<AssessmentTopic> assessmentTopicList = new HashSet<>();
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic One");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopicList.add(assessmentTopic);

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        assessmentTopicReference.setTopic(assessmentTopic);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setTopicName("Topic Two");
        assessmentTopic1.setModule(assessmentModule);

        assessmentTopicReference.setReferenceId(1);
        assessmentTopicReference.setReference("Topic Reference");
        assessmentTopicReference.setRating(Rating.ONE);

        assessmentTopic.setReferences(Collections.singleton(assessmentTopicReference));
        assessmentTopicList.add(assessmentTopic1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter One");
        assessmentParameter.setTopic(assessmentTopic1);

        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference();
        assessmentParameterReference.setParameter(assessmentParameter);
        assessmentParameterReference.setReferenceId(1);
        assessmentParameterReference.setReference("Parameter Reference");
        assessmentParameterReference.setRating(Rating.FIVE);

        assessmentTopic1.setParameters(Collections.singleton(assessmentParameter));

        assessmentParameter.setReferences(Collections.singleton(assessmentParameterReference));

        assessmentModule.setTopics(assessmentTopicList);
        assessmentCategory.setModules(Collections.singleton(assessmentModule));


        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment);
        topicLevelId.setTopic(assessmentTopic);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        ParameterLevelId parameterLevelId = new ParameterLevelId(assessment,assessmentParameter);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);
        parameterLevelAssessment.setRating(5);

        double actualAverageOfModules = assessmentCategory.getCategoryAverage(Collections.singletonList(topicLevelAssessment), Collections.singletonList(parameterLevelAssessment));
        double expectedAverage = 3.0;
        assertEquals(expectedAverage,actualAverageOfModules);
    }

}

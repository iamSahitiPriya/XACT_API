/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AssessmentCategoryTest {
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
        assessmentTopic.setRating(1);
        assessmentTopicList.add(assessmentTopic);

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        assessmentTopicReference.setTopic(assessmentTopic);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setTopicName("Topic Two");
        assessmentTopic1.setModule(assessmentModule);

        assessmentTopicReference.setReferenceId(1);
        assessmentTopicReference.setReference("Topic Reference");
        assessmentTopicReference.setRating(RatingLevel.ONE);

        assessmentTopic.setReferences(Collections.singleton(assessmentTopicReference));
        assessmentTopicList.add(assessmentTopic1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter One");
        assessmentParameter.setTopic(assessmentTopic1);
        assessmentParameter.setRating(5);

        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference();
        assessmentParameterReference.setParameter(assessmentParameter);
        assessmentParameterReference.setReferenceId(1);
        assessmentParameterReference.setReference("Parameter Reference");
        assessmentParameterReference.setRating(RatingLevel.FIVE);

        assessmentTopic1.setParameters(Collections.singleton(assessmentParameter));

        assessmentParameter.setReferences(Collections.singleton(assessmentParameterReference));

        assessmentModule.setTopics(assessmentTopicList);
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        double actualAverageOfModules = assessmentCategory.getCategoryAverage();
        double expectedAverage = 3.0;
        assertEquals(expectedAverage,actualAverageOfModules);
    }

    @Test
    void getModulesWhenModulesNotExist() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assertNull(assessmentCategory.getModules());

    }


}

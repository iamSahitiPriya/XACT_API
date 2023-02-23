/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import com.xact.assessment.services.TopicLevelRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.xact.assessment.models.RecommendationDeliveryHorizon.LATER;
import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.mockito.Mockito.*;
class TopicLevelRecommendationServiceTest {
    private TopicLevelRecommendationRepository topicLevelRecommendationRepository;
    private TopicLevelRecommendationService topicLevelRecommendationService;

    @BeforeEach
    public void beforeEach(){
        topicLevelRecommendationRepository = mock(TopicLevelRecommendationRepository.class);
        topicLevelRecommendationService = new TopicLevelRecommendationService(topicLevelRecommendationRepository);
    }

    @Test
    void shouldSaveRecommendation() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentDescription("description");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategory1.setActive(true);
        AssessmentCategory assessmentCategory2 = new AssessmentCategory("Second Category",true,"2");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("First Module");
        assessmentModule.setCategory(assessmentCategory1);
        assessmentModule.setActive(true);

        AssessmentModule assessmentModule1 = new AssessmentModule("Second Module",assessmentCategory2,true,"");

        assessmentCategory1.setModules(Set.of(assessmentModule));
        assessmentCategory2.setModules(Set.of(assessmentModule1));

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);


        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon(LATER);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);
        topicLevelRecommendationService.save(topicLevelRecommendation);

        verify(topicLevelRecommendationRepository).save(topicLevelRecommendation);

    }
    @Test
    void shouldDeleteRecommendation() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentDescription("description");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategory1.setActive(true);
        AssessmentCategory assessmentCategory2 = new AssessmentCategory("Second Category",true,"2");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("First Module");
        assessmentModule.setCategory(assessmentCategory1);
        assessmentModule.setActive(true);

        AssessmentModule assessmentModule1 = new AssessmentModule("Second Module",assessmentCategory2,true,"");

        assessmentCategory1.setModules(Set.of(assessmentModule));
        assessmentCategory2.setModules(Set.of(assessmentModule1));

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);


        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendation("some recommendation");
        topicLevelRecommendation.setDeliveryHorizon(LATER);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);

        doNothing().when(topicLevelRecommendationRepository).delete(topicLevelRecommendation);
        topicLevelRecommendationService.delete(topicLevelRecommendation);

        verify(topicLevelRecommendationRepository).delete(topicLevelRecommendation);

    }
}

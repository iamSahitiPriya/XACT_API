/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import com.xact.assessment.services.TopicLevelRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.NOW;
import static com.xact.assessment.dtos.RecommendationImpact.LOW;
import static org.mockito.Mockito.*;
class TopicLevelRecommendationServiceTest {
    private TopicLevelRecommendationRepository topicLevelRecommendationRepository;
    private TopicLevelRecommendationService topicLevelRecommendationService;
    private static final ModelMapper modelMapper = new ModelMapper();

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

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(new AssessmentModule());
        assessmentTopic.setActive(true);

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest(null,"text",LOW, RecommendationEffort.LOW,NOW);
        TopicLevelRecommendation topicLevelRecommendation = modelMapper.map(topicLevelRecommendationRequest,TopicLevelRecommendation.class);
        when(topicLevelRecommendationRepository.save(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);

        topicLevelRecommendationService.saveTopicRecommendation(topicLevelRecommendationRequest,assessment,assessmentTopic);

        verify(topicLevelRecommendationRepository).save(any(TopicLevelRecommendation.class));

    }
    @Test
    void shouldUpdateRecommendation() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentDescription("description");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(new AssessmentModule());
        assessmentTopic.setActive(true);

        TopicLevelRecommendationRequest topicLevelRecommendationRequest = new TopicLevelRecommendationRequest(1,"text",LOW, RecommendationEffort.LOW,NOW);
        TopicLevelRecommendation topicLevelRecommendation = modelMapper.map(topicLevelRecommendationRequest,TopicLevelRecommendation.class);
        when(topicLevelRecommendationRepository.findById(1)).thenReturn(Optional.ofNullable(topicLevelRecommendation));
        when(topicLevelRecommendationRepository.update(topicLevelRecommendation)).thenReturn(topicLevelRecommendation);

        topicLevelRecommendationService.updateTopicRecommendation(topicLevelRecommendationRequest);

        verify(topicLevelRecommendationRepository).update(any(TopicLevelRecommendation.class));
    }


    @Test
    void getRecommendationsByTopicAndAssessment() {
        int assessmentId = 1;
        int topicId = 1;
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation(1,new Assessment(),new AssessmentTopic(),"text",LOW,RecommendationEffort.LOW,NOW,new Date(),new Date());
        when(topicLevelRecommendationService.findByAssessmentAndTopic(assessmentId,topicId)).thenReturn(Collections.singletonList(topicLevelRecommendation));

        topicLevelRecommendationService.findByAssessmentAndTopic(assessmentId,topicId);

        verify(topicLevelRecommendationRepository).findByAssessmentAndTopic(assessmentId,topicId);
    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.models.TopicLevelRecommendation;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicLevelRecommendationService {
    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    public TopicLevelRecommendationService(TopicLevelRecommendationRepository topicLevelRecommendationRepository) {
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
    }




    public Optional<TopicLevelRecommendation> findById(Integer recommendationId) {
        return topicLevelRecommendationRepository.findById(recommendationId);
    }

    public List<TopicLevelRecommendation> findByAssessment(Integer assessmentId) {
        return topicLevelRecommendationRepository.findByAssessment(assessmentId);
    }

    public void deleteById(Integer recommendationId) {
        topicLevelRecommendationRepository.deleteById(recommendationId);
    }

    public boolean existsById(Integer recommendationId) {
        return topicLevelRecommendationRepository.existsById(recommendationId);
    }

    public TopicLevelRecommendation updateTopicRecommendation(RecommendationRequest recommendationRequest) {
        TopicLevelRecommendation topicLevelRecommendation = findById(recommendationRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
        topicLevelRecommendation.setRecommendationId(recommendationRequest.getRecommendationId());
        setTopicLevelRecommendation(topicLevelRecommendation, recommendationRequest);
        return updateTopicRecommendation(topicLevelRecommendation);
    }

    private void setTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation, RecommendationRequest recommendationRequest) {
        topicLevelRecommendation.setRecommendationText(recommendationRequest.getRecommendationText());
        topicLevelRecommendation.setRecommendationImpact(recommendationRequest.getImpact());
        topicLevelRecommendation.setRecommendationEffort(recommendationRequest.getEffort());
        topicLevelRecommendation.setDeliveryHorizon(recommendationRequest.getDeliveryHorizon());
    }

    public TopicLevelRecommendation saveTopicRecommendation(RecommendationRequest recommendationRequest, Assessment assessment, AssessmentTopic assessmentTopic) {
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        setTopicLevelRecommendation(topicLevelRecommendation, recommendationRequest);
        return saveTopicRecommendation(topicLevelRecommendation);
    }

    private TopicLevelRecommendation saveTopicRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        if (topicLevelRecommendation.hasRecommendation()) {
            topicLevelRecommendationRepository.save(topicLevelRecommendation);
        }
        return topicLevelRecommendation;
    }

    private TopicLevelRecommendation updateTopicRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        if (topicLevelRecommendation.getRecommendationId() != null) {
                topicLevelRecommendationRepository.update(topicLevelRecommendation);
        }
        return topicLevelRecommendation;
    }
}



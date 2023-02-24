package com.xact.assessment.services;

import com.xact.assessment.dtos.TopicLevelRecommendationRequest;
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

    public void delete(TopicLevelRecommendation topicLevelRecommendation) {
        topicLevelRecommendationRepository.delete(topicLevelRecommendation);
    }

    public List<TopicLevelRecommendation> findByAssessmentAndTopic(Integer assessmentId, Integer topicId) {
        return topicLevelRecommendationRepository.findByAssessmentAndTopic(assessmentId, topicId);
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

    public TopicLevelRecommendation updateTopicRecommendation(TopicLevelRecommendationRequest topicLevelRecommendationRequest) {
        TopicLevelRecommendation topicLevelRecommendation =findById(topicLevelRecommendationRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
        topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
        setTopicLevelRecommendation(topicLevelRecommendation, topicLevelRecommendationRequest);
        return saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    private void setTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation, TopicLevelRecommendationRequest topicLevelRecommendationRequest) {
        topicLevelRecommendation.setRecommendation(topicLevelRecommendationRequest.getRecommendation());
        topicLevelRecommendation.setRecommendationImpact(topicLevelRecommendationRequest.getImpact());
        topicLevelRecommendation.setRecommendationEffort(topicLevelRecommendationRequest.getEffort());
        topicLevelRecommendation.setDeliveryHorizon(topicLevelRecommendationRequest.getDeliveryHorizon());
    }

    public TopicLevelRecommendation saveTopicRecommendation(TopicLevelRecommendationRequest topicLevelRecommendationRequest, Assessment assessment, AssessmentTopic assessmentTopic) {
        TopicLevelRecommendation topicLevelRecommendation=new TopicLevelRecommendation();
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        setTopicLevelRecommendation(topicLevelRecommendation, topicLevelRecommendationRequest);
        return saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    private TopicLevelRecommendation saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        if (topicLevelRecommendation.getRecommendationId() != null) {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationRepository.update(topicLevelRecommendation);
            } else {
                topicLevelRecommendationRepository.delete(topicLevelRecommendation);
            }
        } else {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationRepository.save(topicLevelRecommendation);
            }
        }
        return topicLevelRecommendation;
    }
}

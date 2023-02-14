package com.xact.assessment.services;

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

    public TopicLevelRecommendation save(TopicLevelRecommendation topicLevelRecommendation) {
        return topicLevelRecommendationRepository.save(topicLevelRecommendation);
    }

    public void delete(TopicLevelRecommendation topicLevelRecommendation) {
        topicLevelRecommendationRepository.delete(topicLevelRecommendation);
    }

    public TopicLevelRecommendation update(TopicLevelRecommendation topicLevelRecommendation) {
        return topicLevelRecommendationRepository.update(topicLevelRecommendation);
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
}

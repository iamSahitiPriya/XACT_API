/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicService {
    private final AssessmentTopicRepository assessmentTopicRepository;
    private final AssessmentTopicReferenceService assessmentTopicReferenceService;

    private final TopicLevelRatingService topicLevelRatingService;
    private final TopicLevelRecommendationService topicLevelRecommendationService;

    public TopicService(AssessmentTopicRepository assessmentTopicRepository, AssessmentTopicReferenceService assessmentTopicReferenceService, TopicLevelRatingService topicLevelRatingService, TopicLevelRecommendationService topicLevelRecommendationService) {
        this.assessmentTopicRepository = assessmentTopicRepository;
        this.assessmentTopicReferenceService = assessmentTopicReferenceService;
        this.topicLevelRatingService = topicLevelRatingService;
        this.topicLevelRecommendationService = topicLevelRecommendationService;
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return assessmentTopicRepository.findById(topicId);
    }

    public void createTopic(AssessmentTopic topic) {
        assessmentTopicRepository.save(topic);
    }

    public void updateTopic(AssessmentTopic topic) {
        assessmentTopicRepository.update(topic);
    }

    public AssessmentTopic getTopicById(Integer topicId) {
        return assessmentTopicRepository.findByTopicId(topicId);
    }

    public TopicLevelRating saveRatingAndRecommendation(TopicLevelRating topicLevelRating) {

        if (topicLevelRatingService.existsByID(topicLevelRating)) {
            if (topicLevelRating.getRating() == null) {
                topicLevelRatingService.delete(topicLevelRating);
            } else {
                topicLevelRatingService.update(topicLevelRating);
            }
        } else {
            if (topicLevelRating.getRating() != null)

                topicLevelRatingService.save(topicLevelRating);
        }
        return topicLevelRating;
    }


    public List<TopicLevelRating> getTopicAssessmentData(Integer assessmentId) {
        return topicLevelRatingService.findByAssessment(assessmentId);
    }


    public Optional<TopicLevelRating> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelRatingService.findById(topicLevelId);
    }


    public TopicLevelRecommendation saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        if (topicLevelRecommendation.getRecommendationId() != null) {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationService.update(topicLevelRecommendation);
            } else {
                topicLevelRecommendationService.delete(topicLevelRecommendation);
            }
        } else {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationService.save(topicLevelRecommendation);
            }
        }
        return topicLevelRecommendation;
    }


    public List<TopicLevelRecommendation> getTopicAssessmentRecommendationData(Integer assessmentId, Integer topicId) {
        return topicLevelRecommendationService.findByAssessmentAndTopic(assessmentId, topicId);
    }


    public Optional<TopicLevelRecommendation> searchTopicRecommendation(Integer recommendationId) {
        return topicLevelRecommendationService.findById(recommendationId);
    }


    public void deleteRecommendation(Integer recommendationId) {
        topicLevelRecommendationService.deleteById(recommendationId);
    }


    public boolean checkTopicRecommendationId(Integer recommendationId) {
        return topicLevelRecommendationService.existsById(recommendationId);
    }


    public List<TopicLevelRecommendation> getAssessmentTopicRecommendationData(Integer assessmentId) {
        return topicLevelRecommendationService.findByAssessment(assessmentId);
    }


    public String getTopicRecommendationById(Integer identifier) {
        return topicLevelRecommendationService.findById(identifier).orElseThrow().getRecommendation();
    }

    public void saveTopicReference(AssessmentTopicReference assessmentTopicReference) {
        assessmentTopicReferenceService.save(assessmentTopicReference);
    }


    public AssessmentTopicReference updateTopicReference(AssessmentTopicReference assessmentTopicReference) {
        return assessmentTopicReferenceService.update(assessmentTopicReference);
    }


    public AssessmentTopicReference getAssessmentTopicReference(Integer referenceId) {
        return assessmentTopicReferenceService.findById(referenceId);
    }


    public void deleteTopicReference(Integer referenceId) {
        assessmentTopicReferenceService.deleteById(referenceId);
    }


}

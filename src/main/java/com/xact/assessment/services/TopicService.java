/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentTopicReferenceRepository;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicService {
    private final AssessmentTopicRepository assessmentTopicRepository;
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;

    private final TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    public TopicService(AssessmentTopicRepository assessmentTopicRepository, AssessmentTopicReferenceRepository assessmentTopicReferenceRepository, TopicLevelAssessmentRepository topicLevelAssessmentRepository, TopicLevelRecommendationRepository topicLevelRecommendationRepository) {
        this.assessmentTopicRepository = assessmentTopicRepository;
        this.assessmentTopicReferenceRepository = assessmentTopicReferenceRepository;
        this.topicLevelAssessmentRepository = topicLevelAssessmentRepository;
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
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

    public TopicLevelAssessment saveRatingAndRecommendation(TopicLevelAssessment topicLevelAssessment) {

        if (topicLevelAssessmentRepository.existsById(topicLevelAssessment.getTopicLevelId())) {
            if (topicLevelAssessment.getRating() == null) {
                topicLevelAssessmentRepository.delete(topicLevelAssessment);
            } else {
                topicLevelAssessmentRepository.update(topicLevelAssessment);
            }
        } else {
            if (topicLevelAssessment.getRating() != null)

                topicLevelAssessmentRepository.save(topicLevelAssessment);
        }
        return topicLevelAssessment;
    }

    public List<TopicLevelAssessment> getTopicAssessmentData(Integer assessmentId) {
        return topicLevelAssessmentRepository.findByAssessment(assessmentId);
    }

    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelAssessmentRepository.findById(topicLevelId);
    }

    public TopicLevelRecommendation saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
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

    public List<TopicLevelRecommendation> getTopicAssessmentRecommendationData(Integer assessmentId, Integer topicId) {
        return topicLevelRecommendationRepository.findByAssessmentAndTopic(assessmentId, topicId);
    }

    public Optional<TopicLevelRecommendation> searchTopicRecommendation(Integer recommendationId) {
        return topicLevelRecommendationRepository.findById(recommendationId);
    }


    public void deleteRecommendation(Integer recommendationId) {
        topicLevelRecommendationRepository.deleteById(recommendationId);
    }


    public boolean checkTopicRecommendationId(Integer recommendationId) {
        return topicLevelRecommendationRepository.existsById(recommendationId);
    }


    public List<TopicLevelRecommendation> getAssessmentTopicRecommendationData(Integer assessmentId) {
        return topicLevelRecommendationRepository.findByAssessment(assessmentId);
    }

    public String getTopicRecommendationById(Integer identifier) {
        return topicLevelRecommendationRepository.findById(identifier).orElseThrow().getRecommendation();
    }
    public void saveTopicReference(AssessmentTopicReference assessmentTopicReference) {
        assessmentTopicReferenceRepository.save(assessmentTopicReference);
    }
    public AssessmentTopicReference updateTopicReference(AssessmentTopicReference assessmentTopicReference) {
        return assessmentTopicReferenceRepository.update(assessmentTopicReference);
    }
    public AssessmentTopicReference getAssessmentTopicReference(Integer referenceId) {
        return assessmentTopicReferenceRepository.findById(referenceId).orElseThrow();
    }
    public void deleteTopicReference(Integer referenceId) {
        assessmentTopicReferenceRepository.deleteById(referenceId);
    }


}

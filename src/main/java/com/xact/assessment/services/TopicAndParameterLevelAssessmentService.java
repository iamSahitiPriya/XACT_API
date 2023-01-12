/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicAndParameterLevelAssessmentService {

    private final TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private final ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;

    private final ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;

    public TopicAndParameterLevelAssessmentService(TopicLevelAssessmentRepository topicLevelAssessmentRepository, ParameterLevelAssessmentRepository parameterLevelAssessmentRepository, TopicLevelRecommendationRepository topicLevelRecommendationRepository, ParameterLevelRecommendationRepository parameterLevelRecommendationRepository) {
        this.topicLevelAssessmentRepository = topicLevelAssessmentRepository;
        this.parameterLevelAssessmentRepository = parameterLevelAssessmentRepository;
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
        this.parameterLevelRecommendationRepository = parameterLevelRecommendationRepository;
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


    public ParameterLevelAssessment saveRatingAndRecommendation(ParameterLevelAssessment parameterLevelAssessment) {

        if (parameterLevelAssessmentRepository.existsById(parameterLevelAssessment.getParameterLevelId())) {
            if (parameterLevelAssessment.getRating() == null) {

                parameterLevelAssessmentRepository.delete(parameterLevelAssessment);
            } else {
                parameterLevelAssessmentRepository.update(parameterLevelAssessment);
            }
        } else {
            if (parameterLevelAssessment.getRating() != null) {

                parameterLevelAssessmentRepository.save(parameterLevelAssessment);
            }
        }
        return parameterLevelAssessment;
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


    public ParameterLevelRecommendation saveParameterLevelRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        if (parameterLevelRecommendation.getRecommendationId() != null) {
            if (parameterLevelRecommendation.hasRecommendation()) {
                parameterLevelRecommendationRepository.update(parameterLevelRecommendation);
            } else {
                parameterLevelRecommendationRepository.delete(parameterLevelRecommendation);
            }
        } else {
            if (parameterLevelRecommendation.hasRecommendation()) {
                parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
            }
        }
        return parameterLevelRecommendation;
    }

    public List<ParameterLevelAssessment> getParameterAssessmentData(Integer assessmentId) {
        return parameterLevelAssessmentRepository.findByAssessment(assessmentId);
    }

    public List<TopicLevelAssessment> getTopicAssessmentData(Integer assessmentId) {
        return topicLevelAssessmentRepository.findByAssessment(assessmentId);
    }

    public List<TopicLevelRecommendation> getTopicAssessmentRecommendationData(Integer assessmentId, Integer topicId) {
        return topicLevelRecommendationRepository.findByAssessmentAndTopic(assessmentId, topicId);
    }

    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterLevelAssessmentRepository.findById(parameterLevelId);
    }


    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelAssessmentRepository.findById(topicLevelId);
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


    public List<ParameterLevelRecommendation> getAssessmentParameterRecommendationData(Integer assessmentId) {
        return parameterLevelRecommendationRepository.findByAssessment(assessmentId);
    }

    public List<ParameterLevelRecommendation> getParameterAssessmentRecommendationData(Integer assessmentId, Integer parameterId) {
        return parameterLevelRecommendationRepository.findByAssessmentAndParameter(assessmentId, parameterId);
    }

    public Optional<ParameterLevelRecommendation> searchParameterRecommendation(Integer recommendationId) {
        return parameterLevelRecommendationRepository.findById(recommendationId);
    }

    public boolean checkParameterRecommendationId(Integer recommendationId) {
        return parameterLevelRecommendationRepository.existsById(recommendationId);
    }

    public void deleteParameterRecommendation(Integer recommendationId) {
        parameterLevelRecommendationRepository.deleteById(recommendationId);
    }
}

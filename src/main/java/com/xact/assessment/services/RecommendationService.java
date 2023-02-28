/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.ParameterLevelRecommendation;
import com.xact.assessment.models.Recommendation;
import com.xact.assessment.models.TopicLevelRecommendation;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;

@Singleton
public class RecommendationService {

    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;
    private final ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    private final ParameterLevelRecommendationService parameterLevelRecommendationService;
    private final TopicLevelRecommendationService topicLevelRecommendationService;

    public RecommendationService(TopicLevelRecommendationRepository topicLevelRecommendationRepository, ParameterLevelRecommendationRepository parameterLevelRecommendationRepository, ParameterLevelRecommendationService parameterLevelRecommendationService, TopicLevelRecommendationService topicLevelRecommendationService) {
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
        this.parameterLevelRecommendationRepository = parameterLevelRecommendationRepository;
        this.parameterLevelRecommendationService = parameterLevelRecommendationService;
        this.topicLevelRecommendationService = topicLevelRecommendationService;
    }


    public Recommendation updateRecommendation(RecommendationRequest recommendationRequest, Recommendation recommendation) {
        if (recommendation instanceof TopicLevelRecommendation) {
            recommendation = topicLevelRecommendationService.findById(recommendationRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
            recommendation.setRecommendationId(recommendationRequest.getRecommendationId());
            setRecommendation(recommendation, recommendationRequest);
            return topicLevelRecommendationRepository.update((TopicLevelRecommendation) recommendation);
        } else {
            recommendation = parameterLevelRecommendationService.findById(recommendation.getRecommendationId()).orElse(new ParameterLevelRecommendation());
            recommendation.setRecommendationId(recommendationRequest.getRecommendationId());
            setRecommendation(recommendation, recommendationRequest);
            return parameterLevelRecommendationRepository.update((ParameterLevelRecommendation) recommendation);

        }
    }

    private void setRecommendation(Recommendation recommendation, RecommendationRequest recommendationRequest) {
        recommendation.setRecommendation(recommendationRequest.getRecommendation());
        recommendation.setRecommendationEffort(recommendationRequest.getEffort());
        recommendation.setRecommendationImpact(recommendationRequest.getImpact());
        recommendation.setDeliveryHorizon(recommendationRequest.getDeliveryHorizon());
    }
}

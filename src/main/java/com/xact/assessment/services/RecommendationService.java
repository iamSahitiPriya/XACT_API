/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

@Singleton
public class RecommendationService {

    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;
    private final ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    private final ParameterService parameterService;
    private final TopicService topicService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public RecommendationService(TopicLevelRecommendationRepository topicLevelRecommendationRepository, ParameterLevelRecommendationRepository parameterLevelRecommendationRepository, ParameterService parameterService, TopicService topicService) {
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
        this.parameterLevelRecommendationRepository = parameterLevelRecommendationRepository;
        this.parameterService = parameterService;
        this.topicService = topicService;
    }


    public Recommendation updateRecommendation(RecommendationRequest recommendationRequest, Recommendation recommendation) {
        if (recommendation instanceof TopicLevelRecommendation) {
            recommendation = topicLevelRecommendationRepository.findById(recommendationRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
            recommendation.setRecommendationId(recommendationRequest.getRecommendationId());
            setRecommendation(recommendation, recommendationRequest);
            return topicLevelRecommendationRepository.update((TopicLevelRecommendation) recommendation);
        } else {
            recommendation = parameterLevelRecommendationRepository.findById(recommendation.getRecommendationId()).orElse(new ParameterLevelRecommendation());
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

    public Recommendation saveRecommendation(RecommendationRequest recommendationRequest, Assessment assessment, Integer id, Recommendation recommendation) {
        if (recommendation instanceof TopicLevelRecommendation) {
            setRecommendation(recommendation, recommendationRequest);
            TopicLevelRecommendation topicLevelRecommendation = modelMapper.map(recommendation, TopicLevelRecommendation.class);
            topicLevelRecommendation.setAssessment(assessment);
            topicLevelRecommendation.setTopic(topicService.getTopicById(id));
            return topicLevelRecommendationRepository.save((TopicLevelRecommendation) recommendation);
        } else {
            setRecommendation(recommendation, recommendationRequest);
            ParameterLevelRecommendation parameterLevelRecommendation = modelMapper.map(recommendation, ParameterLevelRecommendation.class);
            parameterLevelRecommendation.setAssessment(assessment);
            parameterLevelRecommendation.setParameter(parameterService.getParameter(id).orElse(new AssessmentParameter()));
            return parameterLevelRecommendationRepository.save((ParameterLevelRecommendation) recommendation);
        }
    }
}

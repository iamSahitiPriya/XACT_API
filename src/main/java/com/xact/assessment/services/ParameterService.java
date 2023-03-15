/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class ParameterService {
    private final AssessmentParameterRepository assessmentParameterRepository;
    private final ParameterLevelRatingService parameterLevelRatingService;
    private final ParameterLevelRecommendationService parameterLevelRecommendationService;
    private final AssessmentParameterReferenceService assessmentParameterReferenceService;


    public ParameterService(AssessmentParameterRepository assessmentParameterRepository, ParameterLevelRatingService parameterLevelRatingService, ParameterLevelRecommendationService parameterLevelRecommendationService, AssessmentParameterReferenceService assessmentParameterReferenceService) {
        this.assessmentParameterRepository = assessmentParameterRepository;
        this.parameterLevelRatingService = parameterLevelRatingService;
        this.parameterLevelRecommendationService = parameterLevelRecommendationService;
        this.assessmentParameterReferenceService = assessmentParameterReferenceService;
    }

    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return assessmentParameterRepository.findById(parameterId);
    }

    public void createParameter(AssessmentParameter parameter) {
        assessmentParameterRepository.save(parameter);
    }

    public void updateParameter(AssessmentParameter assessmentParameter) {
        assessmentParameterRepository.update(assessmentParameter);
    }

    public List<AssessmentParameter> getAllParameters() {
        return (List<AssessmentParameter>) assessmentParameterRepository.findAll();
    }


    public ParameterLevelRating saveParameterRating(ParameterLevelRating parameterLevelRating) {

        if (parameterLevelRatingService.existsById(parameterLevelRating)) {
            if (parameterLevelRating.getRating() == null) {

                parameterLevelRatingService.delete(parameterLevelRating);
            } else {
                parameterLevelRatingService.update(parameterLevelRating);
            }
        } else {
            if (parameterLevelRating.getRating() != null) {

                parameterLevelRatingService.save(parameterLevelRating);
            }
        }
        return parameterLevelRating;
    }


    public List<ParameterLevelRating> getParameterLevelRatings(Integer assessmentId) {
        return parameterLevelRatingService.findByAssessment(assessmentId);
    }


    public Optional<ParameterLevelRating> searchParameterRating(ParameterLevelId parameterLevelId) {
        return parameterLevelRatingService.findById(parameterLevelId);
    }


    public void deleteParameterRecommendation(Integer recommendationId) {
        parameterLevelRecommendationService.deleteById(recommendationId);
    }

    public String getParameterRecommendationById(Integer identifier) {
        return parameterLevelRecommendationService.findById(identifier).orElseThrow().getRecommendationText();
    }

    public void saveParameterReference(AssessmentParameterReference assessmentParameterReference) {
        assessmentParameterReferenceService.saveParameterReference(assessmentParameterReference);
    }

    public void updateParameterReference(AssessmentParameterReference assessmentParameterReference) {
        assessmentParameterReferenceService.updateParameterReference(assessmentParameterReference);
    }

    public AssessmentParameterReference getAssessmentParameterReference(Integer referenceId) {
        return assessmentParameterReferenceService.getAssessmentParameterReference(referenceId);
    }

    public void deleteParameterReference(Integer referenceId) {
        assessmentParameterReferenceService.deleteParameterReference(referenceId);
    }


    public List<ParameterLevelRecommendation> getParameterRecommendations(Integer assessmentId) {
        return parameterLevelRecommendationService.findByAssessment(assessmentId);
    }

    public ParameterLevelRecommendation updateParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest) {
        return parameterLevelRecommendationService.updateParameterRecommendation(parameterLevelRecommendationRequest);
    }

    public ParameterLevelRecommendation saveParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest, Assessment assessment, Integer parameterId) {
        AssessmentParameter assessmentParameter = getParameter(parameterId).orElseThrow();
        return parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, assessmentParameter);
    }
}

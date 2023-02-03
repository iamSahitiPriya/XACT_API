/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
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

    public List<AssessmentParameter> getParameters() {
        return assessmentParameterRepository.listOrderByUpdatedAtDesc();
    }

    public ParameterLevelRating saveRatingAndRecommendation(ParameterLevelRating parameterLevelRating) {

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


    public List<ParameterLevelRating> getParameterAssessmentData(Integer assessmentId) {
        return parameterLevelRatingService.findByAssessment(assessmentId);
    }


    public Optional<ParameterLevelRating> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterLevelRatingService.findById(parameterLevelId);
    }


    public ParameterLevelRecommendation saveParameterLevelRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        return parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    public List<ParameterLevelRecommendation> getAssessmentParameterRecommendationData(Integer assessmentId) {
        return parameterLevelRecommendationService.findByAssessment(assessmentId);
    }

    public List<ParameterLevelRecommendation> getParameterAssessmentRecommendationData(Integer assessmentId, Integer parameterId) {
        return parameterLevelRecommendationService.findByAssessmentAndParameter(assessmentId, parameterId);
    }

    public Optional<ParameterLevelRecommendation> searchParameterRecommendation(Integer recommendationId) {
        return parameterLevelRecommendationService.findById(recommendationId);
    }

    public boolean checkParameterRecommendationId(Integer recommendationId) {
        return parameterLevelRecommendationService.existsById(recommendationId);
    }

    public void deleteParameterRecommendation(Integer recommendationId) {
        parameterLevelRecommendationService.deleteById(recommendationId);
    }

    public String getParameterRecommendationById(Integer identifier) {
        return parameterLevelRecommendationService.findById(identifier).orElseThrow().getRecommendation();
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


}

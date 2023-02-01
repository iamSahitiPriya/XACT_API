/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class ParameterService {
    private final AssessmentParameterRepository assessmentParameterRepository;
    private final ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private final ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    private final AssessmentParameterReferenceRepository assessmentParameterRRepository;




    public ParameterService(AssessmentParameterRepository assessmentParameterRepository, ParameterLevelAssessmentRepository parameterLevelAssessmentRepository, ParameterLevelRecommendationRepository parameterLevelRecommendationRepository, AssessmentParameterReferenceRepository assessmentParameterRRepository) {
        this.assessmentParameterRepository = assessmentParameterRepository;
        this.parameterLevelAssessmentRepository = parameterLevelAssessmentRepository;
        this.parameterLevelRecommendationRepository = parameterLevelRecommendationRepository;
        this.assessmentParameterRRepository = assessmentParameterRRepository;
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
    public List<ParameterLevelAssessment> getParameterAssessmentData(Integer assessmentId) {
        return parameterLevelAssessmentRepository.findByAssessment(assessmentId);
    }
    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterLevelAssessmentRepository.findById(parameterLevelId);
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
    public  String getParameterRecommendationById(Integer identifier) {
        return parameterLevelRecommendationRepository.findById(identifier).orElseThrow().getRecommendation();
    }
    public void saveParameterReference(AssessmentParameterReference assessmentParameterReference) {
        assessmentParameterRRepository.save(assessmentParameterReference);
    }
    public void updateParameterReference(AssessmentParameterReference assessmentParameterReference) {
        assessmentParameterRRepository.update(assessmentParameterReference);
    }

    public AssessmentParameterReference getAssessmentParameterReference(Integer referenceId) {
        return assessmentParameterRRepository.findById(referenceId).orElseThrow();
    }
    public void deleteParameterReference(Integer referenceId) {
        assessmentParameterRRepository.deleteById(referenceId);
    }


}

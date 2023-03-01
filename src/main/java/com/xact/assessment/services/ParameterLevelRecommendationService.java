package com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterLevelRecommendationRequest;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.ParameterLevelRecommendation;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class ParameterLevelRecommendationService {
    private final ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;

    public ParameterLevelRecommendationService(ParameterLevelRecommendationRepository parameterLevelRecommendationRepository) {
        this.parameterLevelRecommendationRepository = parameterLevelRecommendationRepository;
    }


    public List<ParameterLevelRecommendation> findByAssessment(Integer assessmentId) {
        return parameterLevelRecommendationRepository.findByAssessment(assessmentId);
    }

    public List<ParameterLevelRecommendation> findByAssessmentAndParameter(Integer assessmentId, Integer parameterId) {
        return parameterLevelRecommendationRepository.findByAssessmentAndParameter(assessmentId, parameterId);
    }

    public Optional<ParameterLevelRecommendation> findById(Integer recommendationId) {
        return parameterLevelRecommendationRepository.findById(recommendationId);
    }


    public void deleteById(Integer recommendationId) {
        parameterLevelRecommendationRepository.deleteById(recommendationId);
    }

    public ParameterLevelRecommendation updateParameterLevelRecommendation(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest) {
        ParameterLevelRecommendation parameterLevelRecommendation = findById(parameterLevelRecommendationRequest.getRecommendationId()).orElse(new ParameterLevelRecommendation());
        parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
        setParameterLevelRecommendation(parameterLevelRecommendation, parameterLevelRecommendationRequest);
        return updateParameterRecommendation(parameterLevelRecommendation);
    }

    private ParameterLevelRecommendation updateParameterRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        if (parameterLevelRecommendation.getRecommendationId() != null) {
                parameterLevelRecommendationRepository.update(parameterLevelRecommendation);
        }
        return parameterLevelRecommendation;
    }

    private void setParameterLevelRecommendation(ParameterLevelRecommendation parameterLevelRecommendation, ParameterLevelRecommendationRequest parameterLevelRecommendationRequest) {
        parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
        parameterLevelRecommendation.setRecommendationImpact(parameterLevelRecommendationRequest.getImpact());
        parameterLevelRecommendation.setRecommendationEffort(parameterLevelRecommendationRequest.getEffort());
        parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());
    }

    public ParameterLevelRecommendation saveParameterLevelRecommendation(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, Assessment assessment, AssessmentParameter assessmentParameter) {
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        setParameterLevelRecommendation(parameterLevelRecommendation, parameterLevelRecommendationRequest);
        return saveParameterRecommendation(parameterLevelRecommendation);
    }

    private ParameterLevelRecommendation saveParameterRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        if (parameterLevelRecommendation.hasRecommendation()) {
            parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        }
        return parameterLevelRecommendation;
    }


}


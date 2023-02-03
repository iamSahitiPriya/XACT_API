package com.xact.assessment.services;

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

    public List<ParameterLevelRecommendation> findByAssessment(Integer assessmentId) {
        return parameterLevelRecommendationRepository.findByAssessment(assessmentId);
    }

    public List<ParameterLevelRecommendation> findByAssessmentAndParameter(Integer assessmentId, Integer parameterId) {
        return parameterLevelRecommendationRepository.findByAssessmentAndParameter(assessmentId, parameterId);
    }

    public Optional<ParameterLevelRecommendation> findById(Integer recommendationId) {
        return parameterLevelRecommendationRepository.findById(recommendationId);
    }

    public boolean existsById(Integer recommendationId) {
        return parameterLevelRecommendationRepository.existsById(recommendationId);
    }

    public void deleteById(Integer recommendationId) {
        parameterLevelRecommendationRepository.deleteById(recommendationId);
    }

}

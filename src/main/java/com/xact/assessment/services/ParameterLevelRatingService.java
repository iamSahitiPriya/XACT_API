/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.ParameterLevelId;
import com.xact.assessment.models.ParameterLevelRating;
import com.xact.assessment.repositories.ParameterLevelRatingRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class ParameterLevelRatingService {
    private final ParameterLevelRatingRepository parameterLevelRatingRepository;

    public ParameterLevelRatingService(ParameterLevelRatingRepository parameterLevelRatingRepository) {
        this.parameterLevelRatingRepository = parameterLevelRatingRepository;
    }

    public ParameterLevelRating save(ParameterLevelRating parameterLevelRating) {
        return parameterLevelRatingRepository.save(parameterLevelRating);
    }

    public ParameterLevelRating update(ParameterLevelRating parameterLevelRating) {
        return parameterLevelRatingRepository.update(parameterLevelRating);
    }

    public void delete(ParameterLevelRating parameterLevelRating) {
        parameterLevelRatingRepository.delete(parameterLevelRating);
    }

    public List<ParameterLevelRating> findByAssessment(Integer assessmentId) {
        return parameterLevelRatingRepository.findByAssessment(assessmentId);
    }

    public Optional<ParameterLevelRating> findById(ParameterLevelId parameterLevelId) {
        return parameterLevelRatingRepository.findById(parameterLevelId);
    }

    public boolean existsById(ParameterLevelRating parameterLevelRating) {
        return parameterLevelRatingRepository.existsById(parameterLevelRating.getParameterLevelId());
    }
}

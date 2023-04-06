/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameterReference;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
import jakarta.inject.Singleton;

@Singleton
public class AssessmentParameterReferenceService {
    private final AssessmentParameterReferenceRepository assessmentParameterRRepository;

    public AssessmentParameterReferenceService(AssessmentParameterReferenceRepository assessmentParameterRRepository) {
        this.assessmentParameterRRepository = assessmentParameterRRepository;
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

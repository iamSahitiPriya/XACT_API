/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentQuestionReference;
import com.xact.assessment.repositories.AssessmentQuestionReferenceRepository;
import jakarta.inject.Singleton;

@Singleton
public class AssessmentQuestionReferenceService {

    private final AssessmentQuestionReferenceRepository assessmentQuestionReferenceRepository;

    public AssessmentQuestionReferenceService(AssessmentQuestionReferenceRepository assessmentQuestionReferenceRepository) {
        this.assessmentQuestionReferenceRepository = assessmentQuestionReferenceRepository;
    }

    public void saveQuestionReference(AssessmentQuestionReference assessmentQuestionReference) {
        assessmentQuestionReferenceRepository.save(assessmentQuestionReference);
    }

    public AssessmentQuestionReference getReferenceById(Integer referenceId) {
       return  assessmentQuestionReferenceRepository.findById(referenceId).orElseThrow();
    }

    public void delete(Integer referenceId) {
        assessmentQuestionReferenceRepository.deleteById(referenceId);
    }

    public void updateQuestionReference(AssessmentQuestionReference assessmentQuestionReference) {
        assessmentQuestionReferenceRepository.update(assessmentQuestionReference);
    }

    public void deleteByQuestionId(Integer questionId) {
        assessmentQuestionReferenceRepository.deleteByQuestionId(questionId);
    }
}

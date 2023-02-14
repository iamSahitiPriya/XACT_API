package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentTopicReference;
import com.xact.assessment.repositories.AssessmentTopicReferenceRepository;
import jakarta.inject.Singleton;

@Singleton
public class AssessmentTopicReferenceService {
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;

    public AssessmentTopicReferenceService(AssessmentTopicReferenceRepository assessmentTopicReferenceRepository) {
        this.assessmentTopicReferenceRepository = assessmentTopicReferenceRepository;
    }
    public AssessmentTopicReference save(AssessmentTopicReference assessmentTopicReference) {
        return assessmentTopicReferenceRepository.save(assessmentTopicReference);
    }
    public AssessmentTopicReference update(AssessmentTopicReference assessmentTopicReference) {
        return assessmentTopicReferenceRepository.update(assessmentTopicReference);
    }
    public AssessmentTopicReference findById(Integer referenceId) {
        return assessmentTopicReferenceRepository.findById(referenceId).orElseThrow();
    }
    public void deleteById(Integer referenceId) {
        assessmentTopicReferenceRepository.deleteById(referenceId);
    }

}

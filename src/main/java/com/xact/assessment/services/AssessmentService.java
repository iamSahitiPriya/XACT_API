package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import jakarta.inject.Singleton;

@Singleton
public class AssessmentService {
    public Assessment getAssessmentById(String assessmentID) {
        Assessment assessment = new Assessment();
        assessment.setName("Created an anonymous endpoint");
        return assessment;
    }
}

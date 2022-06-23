package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class ParameterService {
    private final AssessmentParameterRepository assessmentParameterRepository;

    public ParameterService(AssessmentParameterRepository assessmentParameterRepository) {
        this.assessmentParameterRepository = assessmentParameterRepository;
    }
    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return  assessmentParameterRepository.findById(parameterId);
    }
}

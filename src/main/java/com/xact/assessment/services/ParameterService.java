/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class ParameterService {
    private final AssessmentParameterRepository assessmentParameterRepository;

    public ParameterService(AssessmentParameterRepository assessmentParameterRepository) {
        this.assessmentParameterRepository = assessmentParameterRepository;
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

}

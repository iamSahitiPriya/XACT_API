/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentModuleId;
import com.xact.assessment.models.UserAssessmentModule;
import com.xact.assessment.repositories.UserAssessmentModuleRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class UserAssessmentModuleService {
    private final UserAssessmentModuleRepository userAssessmentModuleRepository;

    public UserAssessmentModuleService(UserAssessmentModuleRepository userAssessmentModuleRepository) {
        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
    }

    public UserAssessmentModule save(UserAssessmentModule userAssessmentModule) {
        return userAssessmentModuleRepository.save(userAssessmentModule);
    }

    public void deleteByModule(Assessment assessment) {
        userAssessmentModuleRepository.deleteByModule(assessment.getAssessmentId());
    }

    public List<AssessmentModule> findModuleByAssessment(Integer assessmentId) {
        return userAssessmentModuleRepository.findModuleByAssessment(assessmentId);
    }

    public boolean existsById(AssessmentModuleId assessmentModuleId) {
        return userAssessmentModuleRepository.existsById(assessmentModuleId);
    }

}

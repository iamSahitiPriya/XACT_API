/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentCategoryRequest;
import com.xact.assessment.dtos.AssessmentModuleRequest;
import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentModule;
import jakarta.inject.Singleton;

import java.text.ParseException;
import java.util.List;


@Singleton
public class AdminService {
    private final ModuleContributorService moduleContributorService;

    private final AssessmentMasterDataService assessmentMasterDataService;

    private final AssessmentService assessmentService;


    public AdminService(ModuleContributorService moduleContributorService, AssessmentMasterDataService assessmentMasterDataService, AssessmentService assessmentService) {
        this.moduleContributorService = moduleContributorService;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.assessmentService = assessmentService;
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        moduleContributorService.saveContributors(moduleId,contributors);
    }

    public AssessmentCategory createAssessmentCategory(AssessmentCategoryRequest assessmentCategory) {
        return assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
    }

    public AssessmentModule createAssessmentModule(AssessmentModuleRequest assessmentModule) {
        return assessmentMasterDataService.createAssessmentModule(assessmentModule);
    }

    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        return assessmentMasterDataService.updateCategory(assessmentCategory,assessmentCategoryRequest);
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        return assessmentMasterDataService.updateModule(moduleId,assessmentModuleRequest);
    }

    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        return assessmentService.getTotalAssessments(startDate, endDate);
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }
}

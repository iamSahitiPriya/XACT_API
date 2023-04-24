/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
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

    public AssessmentTopic createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        return assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
    }

    public AssessmentParameter createAssessmentParameter(AssessmentParameterRequest assessmentParameterRequest) {
        return  assessmentMasterDataService.createAssessmentParameter(assessmentParameterRequest);
    }


    public AssessmentTopicReference createAssessmentTopicReference(TopicReferencesRequest topicReferencesRequest) {
        return assessmentMasterDataService.createAssessmentTopicReference(topicReferencesRequest);
    }

    public AssessmentParameterReference createAssessmentParameterReference(ParameterReferencesRequest parameterReferencesRequests) {
        return assessmentMasterDataService.createAssessmentParameterReference(parameterReferencesRequests);
    }

    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        return assessmentMasterDataService.updateCategory(assessmentCategory,assessmentCategoryRequest);
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        return assessmentMasterDataService.updateModule(moduleId,assessmentModuleRequest);
    }

    public AssessmentTopic updateTopic(Integer topicId, AssessmentTopicRequest assessmentTopicRequest) {
        return assessmentMasterDataService.updateTopic(topicId,assessmentTopicRequest);
    }

    public AssessmentParameter updateParameter(Integer parameterId, AssessmentParameterRequest assessmentParameterRequest) {
        return assessmentMasterDataService.updateParameter(parameterId,assessmentParameterRequest);
    }

    public Question updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        return assessmentMasterDataService.updateQuestion(questionId, questionRequest);
    }

    public AssessmentTopicReference updateTopicReference(Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        return assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
    }

    public AssessmentParameterReference updateParameterReference(Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        return assessmentMasterDataService.updateParameterReference(referenceId, parameterReferencesRequest);
    }

    public void deleteTopicReference(Integer referenceId) {
      assessmentMasterDataService.deleteTopicReference(referenceId);
    }

    public void deleteParameterReference(Integer referenceId) {
        assessmentMasterDataService.deleteParameterReference(referenceId);
    }

    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        return assessmentService.getTotalAssessments(startDate, endDate);
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }
}

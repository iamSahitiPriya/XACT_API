/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleContributorRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class ModuleContributorService {
    private final QuestionService questionService;
    private final  ParameterService parameterService;
    private final ModuleContributorRepository moduleContributorRepository;
    private final ModuleService moduleService;


    public ModuleContributorService(QuestionService questionService, ParameterService parameterService, ModuleContributorRepository moduleContributorRepository, ModuleService moduleService) {
        this.questionService = questionService;
        this.parameterService = parameterService;
        this.moduleService = moduleService;
        this.moduleContributorRepository = moduleContributorRepository;
    }

    public List<AssessmentModule> getModulesByRole(String userEmail, ContributorRole contributorRole) {
        return moduleContributorRepository.findByRole(userEmail, contributorRole);
    }

    public Set<ModuleContributor> getContributorsByEmail(String userEmail) {
        return moduleContributorRepository.findContributorsByEmail(userEmail);
    }

    public Optional<ContributorRole> getRole(Integer moduleId, String userEmail) {
        return moduleContributorRepository.findRole(moduleId, userEmail);
    }


    public ContributorResponse getContributorResponse(ContributorRole role, String userEmail) {
        List<AssessmentModule> assessmentModules = getModulesByRole(userEmail, role);
        return questionService.getContributorResponse(role,assessmentModules);
    }

    public Question createAssessmentQuestion(String userEmail, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        Optional<ContributorRole> contributorRole = getRole(question.getParameter().getTopic().getModule().getModuleId(), userEmail);
        return questionService.createQuestion(contributorRole, question);
    }

    public QuestionStatusUpdateResponse updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        return  questionService.updateContributorQuestionsStatus(status, questionStatusUpdateRequest, contributorRole);
    }

    public void deleteQuestion(Integer questionId, String userEmail) {
        Question question = questionService.getQuestionById(questionId);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        questionService.deleteQuestion(question, contributorRole);
    }

    public Question updateContributorQuestion(Integer questionId, String questionText, String userEmail) {
        Question question = questionService.getQuestionById(questionId);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = getRole(moduleId, userEmail);
        return questionService.updateContributorQuestion(question, questionText, contributorRole);
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        List<ModuleContributor> moduleContributors = new ArrayList<>();
        for (ContributorDto contributor : contributors) {
            ModuleContributor moduleContributor = getModuleContributor(moduleId, contributor);
            moduleContributors.add(moduleContributor);
        }
        moduleContributorRepository.deleteByModuleId(moduleId);
        moduleContributorRepository.saveAll(moduleContributors);
    }
    private ModuleContributor getModuleContributor(Integer moduleId, ContributorDto contributorDto) {
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(moduleService.getModule(moduleId));
        contributorId.setUserEmail(contributorDto.getUserEmail());
        ModuleContributor moduleContributor = new ModuleContributor();
        moduleContributor.setContributorId(contributorId);
        moduleContributor.setContributorRole(contributorDto.getRole());
        return moduleContributor;
    }
}

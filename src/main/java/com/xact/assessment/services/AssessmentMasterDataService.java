/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AssessmentMasterDataService {

    ModuleRepository modulesRepository;
    CategoryRepository categoryRepository;
    AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;
    private final ParameterService parameterService;
    private final TopicService topicService;
    private final QuestionService questionService;
    AssessmentParameterReferenceRepository assessmentParameterRRepository;


    public AssessmentMasterDataService(CategoryRepository categoryRepository, ModuleRepository modulesRepository, QuestionService questionService, AssessmentTopicReferenceRepository assessmentTopicReferenceRepository, ParameterService parameterService, TopicService topicService, AssessmentParameterReferenceRepository assessmentParameterRRepository) {
        this.categoryRepository = categoryRepository;
        this.modulesRepository = modulesRepository;
        this.questionService = questionService;
        this.assessmentTopicReferenceRepository = assessmentTopicReferenceRepository;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.assessmentParameterRRepository = assessmentParameterRRepository;

    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void createAssessmentCategory(AssessmentCategoryRequest assessmentCategoryRequest) {
        AssessmentCategory assessmentCategory = new AssessmentCategory(assessmentCategoryRequest.getCategoryId(), assessmentCategoryRequest.getCategoryName());
        assessmentCategory.setActive(assessmentCategoryRequest.isActive());
        categoryRepository.save(assessmentCategory);
    }

    public void createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleId(), assessmentModuleRequest.getModuleName(), assessmentCategory);
        assessmentModule.setActive(assessmentModuleRequest.isActive());
        modulesRepository.save(assessmentModule);
    }

    public void createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = modulesRepository.findByModuleId(assessmentTopicRequest.getModule());
        AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicId(), assessmentTopicRequest.getTopicName(), assessmentModule);
        assessmentTopic.setAssessmentLevel(AssessmentLevel.valueOf("Topic"));
        topicService.createTopic(assessmentTopic);
    }


    public void createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(assessmentParameter.getParameterId(), assessmentParameter.getParameterName(), assessmentTopic);
        parameterService.createParameter(assessmentParameter1);

    }

    public void createAssessmentQuestions(QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionId(), questionRequest.getQuestionText(), assessmentParameter);
        questionService.createQuestion(question);
    }

    public void createAssessmentTopicReferences(TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());
        assessmentTopicReferenceRepository.save(assessmentTopicReference);

    }

    public void createAssessmentParameterReferences(ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());
        assessmentParameterRRepository.save(assessmentParameterReference);

    }
}

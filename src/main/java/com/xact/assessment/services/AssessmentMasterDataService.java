/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
import com.xact.assessment.repositories.AssessmentTopicReferenceRepository;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AssessmentMasterDataService {

    CategoryRepository categoryRepository;
    AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;
    private final ParameterService parameterService;
    private final TopicService topicService;
    private final QuestionService questionService;
    AssessmentParameterReferenceRepository assessmentParameterRRepository;
    private final ModuleService moduleService;


    public AssessmentMasterDataService(CategoryRepository categoryRepository, ModuleService moduleService, QuestionService questionService, AssessmentTopicReferenceRepository assessmentTopicReferenceRepository, ParameterService parameterService, TopicService topicService, AssessmentParameterReferenceRepository assessmentParameterRRepository) {
        this.categoryRepository = categoryRepository;
        this.moduleService = moduleService;
        this.questionService = questionService;
        this.assessmentTopicReferenceRepository = assessmentTopicReferenceRepository;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.assessmentParameterRRepository = assessmentParameterRRepository;

    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }

    public void createAssessmentCategory(AssessmentCategoryRequest assessmentCategoryRequest) {
        AssessmentCategory assessmentCategory = new AssessmentCategory(assessmentCategoryRequest.getCategoryName(), assessmentCategoryRequest.isActive(), assessmentCategoryRequest.getComments());
        categoryRepository.save(assessmentCategory);
    }

    public void createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), assessmentCategory, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
        moduleService.createModule(assessmentModule);
    }

    public void createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicName(), assessmentModule, assessmentTopicRequest.isActive(), assessmentTopicRequest.getComments());
        topicService.createTopic(assessmentTopic);
    }


    public void createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(assessmentParameter.getParameterName(), assessmentTopic, assessmentParameter.isActive(), assessmentParameter.getComments());
        parameterService.createParameter(assessmentParameter1);

    }

    public void createAssessmentQuestions(QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
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

    public void updateCategory(AssessmentCategory assessmentCategory) {
        categoryRepository.update(assessmentCategory);
    }

    public void updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(moduleId);
        assessmentModule.setModuleName(assessmentModuleRequest.getModuleName());

        assessmentModule.setCategory(getCategory(assessmentModuleRequest.getCategory()));
        assessmentModule.setActive(assessmentModuleRequest.isActive());
        moduleService.updateModule(assessmentModule);
    }

    public void updateTopic(Integer topicId, AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setTopicName(assessmentTopicRequest.getTopicName());

        topicService.updateTopic(assessmentTopic);
    }

    public void updateParameter(Integer parameterId, AssessmentParameterRequest assessmentParameterRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameterRequest.getTopic()).orElseThrow();
        assessmentParameter.setParameterName(assessmentParameterRequest.getParameterName());
        assessmentParameter.setTopic(assessmentTopic);

        parameterService.updateParameter(assessmentParameter);
    }

    public void updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = questionService.getQuestion(questionId).orElseThrow();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setParameter(assessmentParameter);

        questionService.updateQuestion(question);


    }

    public void updateTopicReference(Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = assessmentTopicReferenceRepository.findById(referenceId).orElseThrow();
        assessmentTopicReference.setReference(topicReferencesRequest.getReference());
        assessmentTopicReference.setTopic(assessmentTopic);
        assessmentTopicReference.setRating(topicReferencesRequest.getRating());

        assessmentTopicReferenceRepository.update(assessmentTopicReference);
    }

    public void updateParameterReferences(Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = assessmentParameterRRepository.findById(referenceId).orElseThrow();
        assessmentParameterReference.setParameter(assessmentParameter);
        assessmentParameterReference.setReference(parameterReferencesRequest.getReference());
        assessmentParameterReference.setRating(parameterReferencesRequest.getRating());

        assessmentParameterRRepository.update(assessmentParameterReference);
    }
}

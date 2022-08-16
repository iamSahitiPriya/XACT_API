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
        List<AssessmentCategory> categories = getAllCategories();
        categories.sort((a, b) -> a.getCategoryId() - b.getCategoryId());
        Integer categoryId = categories.size() > 1 ? ((categories.get(categories.size() - 1).getCategoryId()) + 1) : 1;
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryId, assessmentCategoryRequest.getCategoryName(), assessmentCategoryRequest.isActive(), assessmentCategoryRequest.getComments());
        categoryRepository.save(assessmentCategory);
    }

    public void createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        List<AssessmentModule> modules = moduleService.getAllModules();
        modules.sort((a, b) -> a.getModuleId() - b.getModuleId());
        Integer moduleId = modules.size() > 1 ? (modules.get(modules.size() - 1).getModuleId()) + 1 : 1;
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        AssessmentModule assessmentModule = new AssessmentModule(moduleId, assessmentModuleRequest.getModuleName(), assessmentCategory, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
        moduleService.createModule(assessmentModule);
    }

    public void createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        List<AssessmentTopic> topics = topicService.getAllTopics();
        topics.sort((a, b) -> a.getTopicId() - b.getTopicId());
        Integer topicId = topics.size() > 1 ? (topics.get(topics.size() - 1).getTopicId()) + 1 : 1;
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicId, assessmentTopicRequest.getTopicName(), assessmentModule, assessmentTopicRequest.isActive(), assessmentTopicRequest.getComments());
        topicService.createTopic(assessmentTopic);
    }


    public void createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        List<AssessmentParameter> parameters = parameterService.getAllParameters();
        parameters.sort((a, b) -> a.getParameterId() - b.getParameterId());
        Integer parameterId = parameters.size() > 1 ? (parameters.get(parameters.size() - 1).getParameterId()) + 1 : 1;
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(parameterId, assessmentParameter.getParameterName(), assessmentTopic, assessmentParameter.isActive(), assessmentParameter.getComments());
        parameterService.createParameter(assessmentParameter1);

    }

    public void createAssessmentQuestions(QuestionRequest questionRequest) {
        List<Question> questions = questionService.getAllQuestion();
        Integer questionId = questions.size() > 1 ? (questions.get(questions.size() - 1).getQuestionId()) + 1 : 1;
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionId, questionRequest.getQuestionText(), assessmentParameter);
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

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
import com.xact.assessment.repositories.AssessmentTopicReferenceRepository;
import com.xact.assessment.repositories.CategoryRepository;
import com.xact.assessment.repositories.UserAssessmentModuleRepository;
import jakarta.inject.Singleton;

import java.util.*;

@Singleton
public class AssessmentMasterDataService {

    private final CategoryRepository categoryRepository;
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;
    private final ParameterService parameterService;
    private final TopicService topicService;
    private final QuestionService questionService;

    private final UserAssessmentModuleRepository userAssessmentModuleRepository;
    private final AssessmentParameterReferenceRepository assessmentParameterRRepository;
    private final ModuleService moduleService;
    private static final String DUPLICATE_RECORDS_ARE_NOT_ALLOWED = "Duplicate records are not allowed";


    public AssessmentMasterDataService(CategoryRepository categoryRepository, ModuleService moduleService, QuestionService questionService, AssessmentTopicReferenceRepository assessmentTopicReferenceRepository, ParameterService parameterService, TopicService topicService, UserAssessmentModuleRepository userAssessmentModuleRepository, AssessmentParameterReferenceRepository assessmentParameterRRepository) {
        this.categoryRepository = categoryRepository;
        this.moduleService = moduleService;
        this.questionService = questionService;
        this.assessmentTopicReferenceRepository = assessmentTopicReferenceRepository;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
        this.assessmentParameterRRepository = assessmentParameterRRepository;

    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public AssessmentCategory getCategory(Integer categoryId) {
        return categoryRepository.findCategoryById(categoryId);
    }

    public List<AssessmentCategory> getUserAssessmentCategories(Integer assessmentId) {
        List<AssessmentCategory> categories = new ArrayList<>();
        Set<AssessmentCategory> categorySet = new HashSet<>();
        List<AssessmentModule> assessmentModules = userAssessmentModuleRepository.findModuleByAssessment(assessmentId);
        if (!assessmentModules.isEmpty()) {
            for (AssessmentModule assessmentModule : assessmentModules) {
                AssessmentCategory category = assessmentModule.getCategory();
                assessmentModule.setTopics(assessmentModule.getActiveTopics());
                categorySet.add(category);

            }
            for (AssessmentCategory category : categorySet) {
                Set<AssessmentModule> assessmentModuleSet = new HashSet<>();
                category.getModules().forEach(module -> {
                    if (assessmentModules.contains(module)) {
                        assessmentModuleSet.add(module);
                    }
                });
                category.setModules(assessmentModuleSet);
                categories.add(category);
            }
        }
        return categories;
    }

    public AssessmentCategory createAssessmentCategory(AssessmentCategoryRequest assessmentCategoryRequest) {
        if (!checkIfCategoryUnique(assessmentCategoryRequest.getCategoryName())) {
            AssessmentCategory assessmentCategory = new AssessmentCategory(assessmentCategoryRequest.getCategoryName(), assessmentCategoryRequest.isActive(), assessmentCategoryRequest.getComments());
            categoryRepository.save(assessmentCategory);
            return assessmentCategory;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    private boolean checkIfCategoryUnique(String categoryName) {
        List<String> categories = categoryRepository.getAllCategories();
        List<String> result = categories.stream()
                .map(String::toLowerCase).map(option -> option.replaceAll("\\s", ""))
                .toList();
        return result.contains(categoryName.toLowerCase().replaceAll("\\s", ""));
    }

    public AssessmentModule createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        if (!checkIfModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory)) {
            AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), assessmentCategory, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
            moduleService.createModule(assessmentModule);
            return assessmentModule;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }
    }

    public AssessmentTopic createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicName(), assessmentModule, assessmentTopicRequest.isActive(), assessmentTopicRequest.getComments());
        topicService.createTopic(assessmentTopic);
        return assessmentTopic;
    }


    public AssessmentParameter createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        AssessmentParameter assessmentParameter1 = AssessmentParameter.builder().parameterName(assessmentParameter.getParameterName()).topic(assessmentTopic)
                .isActive(assessmentParameter.isActive()).comments(assessmentParameter.getComments()).build();


        parameterService.createParameter(assessmentParameter1);
        return assessmentParameter1;
    }

    public void createAssessmentQuestions(QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        questionService.createQuestion(question);
    }

    public AssessmentTopicReference createAssessmentTopicReferences(TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());
        assessmentTopicReferenceRepository.save(assessmentTopicReference);
        return assessmentTopicReference;
    }

    public void createAssessmentParameterReferences(ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());
        assessmentParameterRRepository.save(assessmentParameterReference);

    }

    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        if (assessmentCategory.getCategoryName().equals(assessmentCategoryRequest.getCategoryName())) {
            assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
            assessmentCategory.setActive(assessmentCategoryRequest.isActive());
            assessmentCategory.setComments(assessmentCategoryRequest.getComments());
            categoryRepository.update(assessmentCategory);
        } else {
            if (!checkIfCategoryUnique(assessmentCategoryRequest.getCategoryName())) {
                assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
                assessmentCategory.setActive(assessmentCategoryRequest.isActive());
                assessmentCategory.setComments(assessmentCategoryRequest.getComments());
                categoryRepository.update(assessmentCategory);
            } else {
                throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
            }
        }
        return assessmentCategory;
    }

    private boolean checkIfModuleUnique(String moduleName, AssessmentCategory assessmentCategory) {
        List<String> modules = moduleService.getModuleNames(assessmentCategory.getCategoryId());
        List<String> result = modules.stream().map(String::toLowerCase).map(option -> option.replaceAll("\\s", "")).toList();
        return result.contains(moduleName.toLowerCase().replaceAll("\\s", ""));
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(moduleId);
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        if (assessmentModule.getModuleName().equals(assessmentModuleRequest.getModuleName()) || !checkIfModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory)) {
            assessmentModule.setModuleName(assessmentModuleRequest.getModuleName());
            assessmentModule.setCategory(assessmentCategory);
            assessmentModule.setActive(assessmentModuleRequest.isActive());
            assessmentModule.setComments(assessmentModuleRequest.getComments());
            moduleService.updateModule(assessmentModule);
            return assessmentModule;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    public AssessmentTopic updateTopic(Integer topicId, AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setTopicName(assessmentTopicRequest.getTopicName());
        assessmentTopic.setActive(assessmentTopicRequest.isActive());
        assessmentTopic.setComments(assessmentTopicRequest.getComments());
        assessmentTopic.setUpdatedAt(new Date());
        topicService.updateTopic(assessmentTopic);
        return assessmentTopic;
    }

    public AssessmentParameter updateParameter(Integer parameterId, AssessmentParameterRequest assessmentParameterRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameterRequest.getTopic()).orElseThrow();
        assessmentParameter.setParameterName(assessmentParameterRequest.getParameterName());
        assessmentParameter.setTopic(assessmentTopic);
        assessmentParameter.setActive(assessmentParameterRequest.isActive());
        assessmentParameter.setComments(assessmentParameterRequest.getComments());
        parameterService.updateParameter(assessmentParameter);
        return assessmentParameter;
    }

    public void updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = questionService.getQuestion(questionId).orElseThrow();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setParameter(assessmentParameter);

        questionService.updateQuestion(question);


    }

    public AssessmentTopicReference updateTopicReference(Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = assessmentTopicReferenceRepository.findById(referenceId).orElseThrow();
        assessmentTopicReference.setReference(topicReferencesRequest.getReference());
        assessmentTopicReference.setTopic(assessmentTopic);
        assessmentTopicReference.setRating(topicReferencesRequest.getRating());

        return assessmentTopicReferenceRepository.update(assessmentTopicReference);
    }

    public void updateParameterReferences(Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = assessmentParameterRRepository.findById(referenceId).orElseThrow();
        assessmentParameterReference.setParameter(assessmentParameter);
        assessmentParameterReference.setReference(parameterReferencesRequest.getReference());
        assessmentParameterReference.setRating(parameterReferencesRequest.getRating());

        assessmentParameterRRepository.update(assessmentParameterReference);
    }

    public List<AssessmentCategory> getCategories() {
        return categoryRepository.findCategories();
    }

    public void deleteTopicReference(Integer referenceId) {
        assessmentTopicReferenceRepository.deleteById(referenceId);
    }
}


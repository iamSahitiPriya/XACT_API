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
        if (isCategoryUnique(assessmentCategoryRequest.getCategoryName())) {
            AssessmentCategory assessmentCategory = new AssessmentCategory(assessmentCategoryRequest.getCategoryName(), assessmentCategoryRequest.isActive(), assessmentCategoryRequest.getComments());
            categoryRepository.save(assessmentCategory);
            return assessmentCategory;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    private boolean isCategoryUnique(String categoryName) {
        List<String> categories = categoryRepository.getAllCategories();
        return isUnique(categories, categoryName);
    }

    public AssessmentModule createAssessmentModule(AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        if (isModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory)) {
            AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), assessmentCategory, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
            moduleService.createModule(assessmentModule);
            return assessmentModule;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }
    }

    public AssessmentTopic createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(assessmentTopicRequest.getModule());
        if (isTopicUnique(assessmentTopicRequest.getTopicName(), assessmentModule)) {
            AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicName(), assessmentModule, assessmentTopicRequest.isActive(), assessmentTopicRequest.getComments());
            topicService.createTopic(assessmentTopic);
            return assessmentTopic;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }
    }

    private boolean isTopicUnique(String topicName, AssessmentModule assessmentModule) {
        List<String> topics = assessmentModule.getTopics().stream().map(AssessmentTopic::getTopicName).toList();
        return isUnique(topics, topicName);
    }


    public AssessmentParameter createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameter.getTopic()).orElseThrow();
        if (isParameterUnique(assessmentParameter.getParameterName(), assessmentTopic)) {
            AssessmentParameter assessmentParameter1 = AssessmentParameter.builder().parameterName(assessmentParameter.getParameterName()).topic(assessmentTopic)
                    .isActive(assessmentParameter.isActive()).comments(assessmentParameter.getComments()).build();

            parameterService.createParameter(assessmentParameter1);
            return assessmentParameter1;
        } else {
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
        }

    }

    private boolean isParameterUnique(String parameterName, AssessmentTopic assessmentTopic) {
        List<String> parameters = assessmentTopic.getParameters().stream().map(AssessmentParameter::getParameterName).toList();
        return isUnique(parameters, parameterName);
    }

    public Question createAssessmentQuestions(QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        questionService.createQuestion(question);
        return question;
    }

    public AssessmentTopicReference createAssessmentTopicReferences(TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        if (isTopicRatingUnique(assessmentTopic.getReferences(), topicReferencesRequest.getRating()) && isTopicReferenceUnique(assessmentTopic.getReferences(), topicReferencesRequest.getReference())) {
            AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());
            assessmentTopicReferenceRepository.save(assessmentTopicReference);
            return assessmentTopicReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private boolean isTopicReferenceUnique(Set<AssessmentTopicReference> references, String reference) {
        if (references != null) {
            List<String> topicReferences = references.stream().map(AssessmentTopicReference::getReference).toList();
            return isUnique(topicReferences, reference);
        } else return true;
    }

    private boolean isTopicRatingUnique(Set<AssessmentTopicReference> references, Rating rating) {
        if (references != null) {
            List<Rating> ratings = references.stream().map(AssessmentTopicReference::getRating).toList();
            return !ratings.contains(rating);
        } else return true;
    }

    public AssessmentParameterReference createAssessmentParameterReferences(ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        if (isParameterRatingUnique(assessmentParameter.getReferences(), parameterReferencesRequest.getRating()) && isParameterReferenceUnique(assessmentParameter.getReferences(), parameterReferencesRequest.getReference())) {
            AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());
            assessmentParameterRRepository.save(assessmentParameterReference);
            return assessmentParameterReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private boolean isParameterReferenceUnique(Set<AssessmentParameterReference> references, String reference) {
        if (references != null) {
            List<String> parameterReferences = references.stream().map(AssessmentParameterReference::getReference).toList();
            return isUnique(parameterReferences, reference);
        } else return true;
    }

    private boolean isParameterRatingUnique(Set<AssessmentParameterReference> references, Rating rating) {
        if (references != null) {
            List<Rating> ratings = references.stream().map(AssessmentParameterReference::getRating).toList();
            return !ratings.contains(rating);
        } else return true;
    }

    public AssessmentCategory updateCategory(AssessmentCategory assessmentCategory, AssessmentCategoryRequest assessmentCategoryRequest) {
        if (removeWhiteSpaces(assessmentCategory.getCategoryName()).equals(removeWhiteSpaces(assessmentCategoryRequest.getCategoryName())) || isCategoryUnique(assessmentCategoryRequest.getCategoryName())) {
            assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
            assessmentCategory.setActive(assessmentCategoryRequest.isActive());
            assessmentCategory.setComments(assessmentCategoryRequest.getComments());
            categoryRepository.update(assessmentCategory);
            return assessmentCategory;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private boolean isModuleUnique(String moduleName, AssessmentCategory assessmentCategory) {
        List<String> modules = assessmentCategory.getModules().stream().map(AssessmentModule::getModuleName).toList();
        return isUnique(modules, moduleName);
    }

    private boolean isUnique(List<String> actualList, String name) {
        List<String> result = actualList.stream()
                .map(String::toLowerCase).map(option -> option.replaceAll("\\s", ""))
                .toList();
        return !(result.contains(name.toLowerCase().replaceAll("\\s", "")));
    }

    public AssessmentModule updateModule(Integer moduleId, AssessmentModuleRequest assessmentModuleRequest) {
        AssessmentModule assessmentModule = moduleService.getModule(moduleId);
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModuleRequest.getCategory());
        if (removeWhiteSpaces(assessmentModule.getModuleName()).equals(removeWhiteSpaces(assessmentModuleRequest.getModuleName())) || isModuleUnique(assessmentModuleRequest.getModuleName(), assessmentCategory)) {
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
        if (removeWhiteSpaces(assessmentTopic.getTopicName()).equals(removeWhiteSpaces(assessmentTopicRequest.getTopicName())) || isTopicUnique(assessmentTopicRequest.getTopicName(), assessmentModule)) {
            assessmentTopic.setModule(assessmentModule);
            assessmentTopic.setTopicName(assessmentTopicRequest.getTopicName());
            assessmentTopic.setActive(assessmentTopicRequest.isActive());
            assessmentTopic.setComments(assessmentTopicRequest.getComments());
            assessmentTopic.setUpdatedAt(new Date());
            topicService.updateTopic(assessmentTopic);
            return assessmentTopic;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public AssessmentParameter updateParameter(Integer parameterId, AssessmentParameterRequest assessmentParameterRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
        AssessmentTopic assessmentTopic = topicService.getTopic(assessmentParameterRequest.getTopic()).orElseThrow();
        if (removeWhiteSpaces(assessmentParameter.getParameterName()).equals(removeWhiteSpaces(assessmentParameterRequest.getParameterName())) || isParameterUnique(assessmentParameter.getParameterName(), assessmentTopic)) {
            assessmentParameter.setParameterName(assessmentParameterRequest.getParameterName());
            assessmentParameter.setTopic(assessmentTopic);
            assessmentParameter.setActive(assessmentParameterRequest.isActive());
            assessmentParameter.setComments(assessmentParameterRequest.getComments());
            parameterService.updateParameter(assessmentParameter);
            return assessmentParameter;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public Question updateQuestion(Integer questionId, QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(questionRequest.getParameter()).orElseThrow();
        Question question = questionService.getQuestion(questionId).orElseThrow();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setParameter(assessmentParameter);

        questionService.updateQuestion(question);
        return question;


    }

    public AssessmentTopicReference updateTopicReference(Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        AssessmentTopic assessmentTopic = topicService.getTopic(topicReferencesRequest.getTopic()).orElseThrow();
        AssessmentTopicReference assessmentTopicReference = assessmentTopicReferenceRepository.findById(referenceId).orElseThrow();
        Set<AssessmentTopicReference> assessmentTopicReferences = new HashSet<>(assessmentTopic.getReferences().stream().filter(reference -> !Objects.equals(reference.getReferenceId(), assessmentTopicReference.getReferenceId())).toList());
        if (isTopicRatingUnique(assessmentTopicReferences, topicReferencesRequest.getRating()) && isTopicReferenceUnique(assessmentTopicReferences, topicReferencesRequest.getReference())) {
            assessmentTopicReference.setReference(topicReferencesRequest.getReference());
            assessmentTopicReference.setTopic(assessmentTopic);
            assessmentTopicReference.setRating(topicReferencesRequest.getRating());
            return assessmentTopicReferenceRepository.update(assessmentTopicReference);
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    public AssessmentParameterReference updateParameterReferences(Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterReferencesRequest.getParameter()).orElseThrow();
        AssessmentParameterReference assessmentParameterReference = assessmentParameterRRepository.findById(referenceId).orElseThrow();
        Set<AssessmentParameterReference> assessmentParameterReferences = new HashSet<>(assessmentParameter.getReferences().stream().filter(reference -> !Objects.equals(reference.getReferenceId(), assessmentParameterReference.getReferenceId())).toList());
        if (isParameterRatingUnique(assessmentParameterReferences, parameterReferencesRequest.getRating()) && isParameterReferenceUnique(assessmentParameterReferences, parameterReferencesRequest.getReference())) {
            assessmentParameterReference.setParameter(assessmentParameter);
            assessmentParameterReference.setReference(parameterReferencesRequest.getReference());
            assessmentParameterReference.setRating(parameterReferencesRequest.getRating());

            assessmentParameterRRepository.update(assessmentParameterReference);
            return assessmentParameterReference;
        } else
            throw new DuplicateRecordException(DUPLICATE_RECORDS_ARE_NOT_ALLOWED);
    }

    private String removeWhiteSpaces(String name) {
        return name.toLowerCase().replaceAll("\\s", "");
    }

    public List<AssessmentCategory> getCategories() {
        return categoryRepository.findCategories();
    }

    public void deleteTopicReference(Integer referenceId) {
        assessmentTopicReferenceRepository.deleteById(referenceId);
    }

    public void deleteParameterReference(Integer referenceId) {
        assessmentParameterRRepository.deleteById(referenceId);
    }
    public Long getAssessedCategory(Integer assessmentId){
        List<Long> assessedCategories = new ArrayList<>();
        for (Long category : categoryRepository.getAssessedCategoryByTopic(assessmentId)) {
            if (!categoryRepository.getAssessedCategoryByParameter(assessmentId).contains(category)) {
                assessedCategories.add(category);
            }
        }
        return (long) assessedCategories.size();
    }
}


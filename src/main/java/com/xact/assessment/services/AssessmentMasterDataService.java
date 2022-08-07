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
    AssessmentTopicRepository assessmentTopicRepository;
    AssessmentParameterRepository assessmentParameterRepository;
    QuestionRepository questionRepository;


    public AssessmentMasterDataService(CategoryRepository categoryRepository, ModuleRepository modulesRepository, AssessmentTopicRepository assessmentTopicRepository, AssessmentParameterRepository assessmentParameterRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.modulesRepository = modulesRepository;
        this.assessmentTopicRepository = assessmentTopicRepository;
        this.assessmentParameterRepository = assessmentParameterRepository;
        this.questionRepository = questionRepository;
    }

    public List<AssessmentCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void createAssessmentMasterData(AssessmentCategoryRequest assessmentCategory) {
        AssessmentCategory assessmentCategory1 = new AssessmentCategory(assessmentCategory.getCategoryId(), assessmentCategory.getCategoryName());
        assessmentCategory1.setActive(assessmentCategory.isActive());
        categoryRepository.save(assessmentCategory1);
    }

    public void createAssessmentModule(AssessmentModuleRequest assessmentModule) {
        AssessmentCategory assessmentCategory = categoryRepository.findCategoryById(assessmentModule.getCategory());
        AssessmentModule assessmentModule1 = new AssessmentModule(assessmentModule.getModuleId(), assessmentModule.getModuleName(), assessmentCategory);
        assessmentModule1.setActive(assessmentModule.isActive());
        modulesRepository.save(assessmentModule1);
    }

    public void createAssessmentTopics(AssessmentTopicRequest assessmentTopicRequest) {
        AssessmentModule assessmentModule = modulesRepository.findByModuleId(assessmentTopicRequest.getModule());
        AssessmentTopic assessmentTopic = new AssessmentTopic(assessmentTopicRequest.getTopicId(), assessmentTopicRequest.getTopicName(), assessmentModule);
        assessmentTopic.setAssessmentLevel(AssessmentLevel.valueOf("Topic"));
        assessmentTopicRepository.save(assessmentTopic);
    }


    public void createAssessmentParameter(AssessmentParameterRequest assessmentParameter) {
        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(assessmentParameter.getTopic());
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(assessmentParameter.getParameterId(),assessmentParameter.getParameterName(),assessmentTopic);
        assessmentParameterRepository.save(assessmentParameter1);

    }

    public void createAssessmentQuestions(QuestionRequest questionRequest) {
        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(questionRequest.getParameter());
        Question question = new Question(questionRequest.getQuestionId(),questionRequest.getQuestionText(),assessmentParameter);
        System.out.println(question.getQuestionId() + question.getQuestionText() + question.getParameter());
        questionRepository.save(question);
    }

}

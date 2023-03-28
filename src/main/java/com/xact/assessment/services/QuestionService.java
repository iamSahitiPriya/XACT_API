/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

import static com.xact.assessment.dtos.ContributorQuestionStatus.*;

@Singleton
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final UserQuestionService userQuestionService;

    private final ModuleContributorService moduleContributorService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public QuestionService(QuestionRepository questionRepository, UserQuestionService userQuestionService, ModuleContributorService moduleContributorService) {
        this.questionRepository = questionRepository;
        this.userQuestionService = userQuestionService;
        this.moduleContributorService = moduleContributorService;
    }

    public Optional<Question> getQuestion(Integer questionId) {
        return questionRepository.findById(questionId);
    }

    public void createQuestion(Question question) {
        questionRepository.save(question);
    }

    public void updateQuestion(Question question) {
        questionRepository.update(question);
    }

    public List<Question> getAllQuestion() {
        return (List<Question>) questionRepository.findAll();
    }

    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return getQuestion(questionId).orElseThrow().getParameter().getTopic();
    }

    public void save(List<UserQuestion> userQuestionList) {
        for (UserQuestion userQuestion : userQuestionList) {
            if (!userQuestion.isContributionStatus()) {
                Question question = new Question();
                question.setQuestionText(userQuestion.getQuestion());
                question.setParameter(userQuestion.getParameter());
                question.setQuestionStatus(ContributorQuestionStatus.Draft);
                createQuestion(question);
                userQuestion.setContributionStatus(true);
                userQuestionService.updateUserQuestion(userQuestion);
            }
        }
    }

    public ContributorResponse getContributorResponse(ContributorRole contributorRole, String userEmail) {
        List<AssessmentModule> assessmentModules = moduleContributorService.getModuleByRole(userEmail, contributorRole);
        ContributorResponse contributorResponse = new ContributorResponse();
        List<ContributorCategoryData> contributorCategoryDataList = getContributorCategoryDataResponse(contributorRole, assessmentModules);
        contributorResponse.setCategories(contributorCategoryDataList);
        return contributorResponse;
    }

    private List<ContributorCategoryData> getContributorCategoryDataResponse(ContributorRole contributorRole, List<AssessmentModule> assessmentModules) {
        List<ContributorCategoryData> contributorCategoryDataList = new ArrayList<>();
        for (AssessmentModule assessmentModule : assessmentModules) {
            ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
            List<Question> contributorQuestions = getContributorQuestions(contributorRole, assessmentModule.getModuleId());
            ContributorModuleData contributorModuleData = getContributorModuleData(contributorQuestions, assessmentModule);
            if (!contributorModuleData.getTopics().isEmpty()) {
                Integer categoryIndex = contributorCategoryDataList.stream().map(ContributorCategoryData::getCategoryName).toList().indexOf(assessmentModule.getCategory());
                if (categoryIndex != -1) {
                    contributorCategoryDataList.get(categoryIndex).getModules().add(contributorModuleData);
                } else {
                    contributorCategoryData.setModules(Collections.singletonList(contributorModuleData));
                    contributorCategoryData.setCategoryName(assessmentModule.getCategory().getCategoryName());
                    contributorCategoryDataList.add(contributorCategoryData);
                }
            }
        }
        return contributorCategoryDataList;
    }

    private List<Question> getContributorQuestions(ContributorRole contributorRole, Integer moduleId) {
        List<Question> questionList;
        if (contributorRole.equals(ContributorRole.Author)) {
            questionList = questionRepository.getAuthorQuestions(moduleId);
        } else {
            questionList = questionRepository.getReviewerQuestions(moduleId);
        }
        return questionList;
    }

    private ContributorModuleData getContributorModuleData(List<Question> questionList, AssessmentModule assessmentModule) {
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName(assessmentModule.getModuleName());
        List<ContributorTopicData> contributorTopicDataList = new ArrayList<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            ContributorTopicData contributorTopicData = getContributorTopicData(questionList, assessmentTopic);
            if (!contributorTopicData.getParameters().isEmpty()) {
                contributorTopicDataList.add(contributorTopicData);
            }
        }
        contributorModuleData.setTopics(contributorTopicDataList);
        return contributorModuleData;
    }

    private ContributorTopicData getContributorTopicData(List<Question> questionList, AssessmentTopic assessmentTopic) {
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicName(assessmentTopic.getTopicName());
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            List<Question> parameterQuestions = getFilteredParameterQuestions(questionList, assessmentParameter);
            if (!parameterQuestions.isEmpty()) {
                ContributorParameterData contributorParameterData = getContributorParameterData(parameterQuestions, assessmentParameter);
                contributorParameterDataList.add(contributorParameterData);
            }
        }
        contributorTopicData.setParameters(contributorParameterDataList);
        return contributorTopicData;
    }

    private ContributorParameterData getContributorParameterData(List<Question> questionList, AssessmentParameter assessmentParameter) {
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterName(assessmentParameter.getParameterName());
        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (Question question : questionList) {
            ContributorQuestionData contributorQuestionData = modelMapper.map(question, ContributorQuestionData.class);
            contributorQuestionDataList.add(contributorQuestionData);
        }
        contributorParameterData.setQuestions(contributorQuestionDataList);
        return contributorParameterData;
    }


    private List<Question> getFilteredParameterQuestions(List<Question> questionList, AssessmentParameter assessmentParameter) {
        return questionList.stream().filter(question ->
                Objects.equals(question.getParameter().getParameterId(), assessmentParameter.getParameterId())
        ).collect(Collectors.toList());
    }


    public void updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        ContributorRole contributorRole = moduleContributorService.getRole(moduleId, userEmail);
        if(contributorRole.isStatusValid(status)){
            updateContributorQuestion(status,questionStatusUpdateRequest);
        }

    }


    public void deleteQuestion(Integer questionId, String userEmail) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author && question.getQuestionStatus() != Published) {
            questionRepository.delete(question);
        }

    }

    public void updateContributorQuestion(Integer questionId, String questionText, String userEmail) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        question.setQuestionText(questionText);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author && question.getQuestionStatus() == Draft) {
            updateQuestion(question);
        }
    }

    private void updateContributorQuestion(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        for (Integer questionId: questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if(question.isNextStateAllowed(status)){
                question.setComments(questionStatusUpdateRequest.getComments());
                question.setQuestionStatus(status);
                updateQuestion(question);
            }
        }
    }
}

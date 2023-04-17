/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.xact.assessment.dtos.ContributorQuestionStatus.*;
import static com.xact.assessment.dtos.ContributorRole.AUTHOR;
import static com.xact.assessment.dtos.ContributorRole.REVIEWER;

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

    public void createQuestion(String userEmail, Question question) {

        Optional<ContributorRole> contributorRole = moduleContributorService.getRole(question.getParameter().getTopic().getModule().getModuleId(), userEmail);
        if (contributorRole.isPresent() && contributorRole.get() == AUTHOR) {
            question.setQuestionStatus(DRAFT);
            saveQuestion(question);
        }
    }


    private void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public Question updateQuestion(Question question) {
        return questionRepository.update(question);
    }

    public List<Question> getAllQuestions() {
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
                question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
                saveQuestion(question);
                userQuestion.setContributionStatus(true);
                userQuestionService.updateUserQuestion(userQuestion);
            }
        }
    }

    public ContributorResponse getContributorResponse(ContributorRole contributorRole, String userEmail) {
        List<AssessmentModule> assessmentModules = moduleContributorService.getModulesByRole(userEmail, contributorRole);
        ContributorResponse contributorResponse = new ContributorResponse();
        List<ContributorModuleData> contributorModuleDataList = getContributorModuleData(contributorRole, assessmentModules);
        contributorResponse.setContributorModuleData(contributorModuleDataList);
        return contributorResponse;
    }


    public QuestionStatusUpdateResponse updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        Optional<ContributorRole> contributorRole = moduleContributorService.getRole(moduleId, userEmail);
        QuestionStatusUpdateResponse questionStatusUpdateResponse = new QuestionStatusUpdateResponse();
        if (contributorRole.isPresent() && contributorRole.get().isStatusValid(status)) {
            questionStatusUpdateResponse = updateContributorQuestion(status, questionStatusUpdateRequest);
        }

        return questionStatusUpdateResponse;
    }


    public void deleteQuestion(Integer questionId, String userEmail) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = moduleContributorService.getRole(moduleId, userEmail);
        if (contributorRole.isPresent() && contributorRole.get() == AUTHOR && question.getQuestionStatus() != PUBLISHED) {
            questionRepository.delete(question);
        }

    }

    public Question updateContributorQuestion(Integer questionId, String questionText, String userEmail) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        question.setQuestionText(questionText);
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        Optional<ContributorRole> contributorRole = moduleContributorService.getRole(moduleId, userEmail);
        if (contributorRole.isPresent() && contributorRole.get() == AUTHOR && isUpdateAllowed(question)) {
            question = updateQuestion(question);
        } else if (contributorRole.isPresent() && contributorRole.get() == REVIEWER) {
            question.setQuestionStatus(PUBLISHED);
            question = updateQuestion(question);
        }
        return question;
    }

    private boolean isUpdateAllowed(Question question) {
        return question.getQuestionStatus() == DRAFT || question.getQuestionStatus() == REQUESTED_FOR_CHANGE;
    }

    private List<ContributorModuleData> getContributorModuleData(ContributorRole contributorRole, List<AssessmentModule> assessmentModules) {
        List<ContributorModuleData> contributorModuleDataList = new ArrayList<>();
        for (AssessmentModule assessmentModule : assessmentModules) {
            List<Question> contributorQuestions = getContributorQuestions(contributorRole, assessmentModule.getModuleId());
            ContributorModuleData contributorModuleData = getContributorModuleData(contributorQuestions, assessmentModule);
            if (!contributorModuleData.getTopics().isEmpty()) {
                contributorModuleDataList.add(contributorModuleData);
            }
        }
        return contributorModuleDataList;
    }


    private List<Question> getContributorQuestions(ContributorRole contributorRole, Integer moduleId) {
        List<Question> questionList;
        if (contributorRole.equals(AUTHOR)) {
            questionList = questionRepository.getAuthorQuestions(moduleId);
        } else {
            questionList = questionRepository.getReviewerQuestions(moduleId);
        }
        return questionList;
    }

    private ContributorModuleData getContributorModuleData(List<Question> questionList, AssessmentModule assessmentModule) {
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleId(assessmentModule.getModuleId());
        contributorModuleData.setModuleName(assessmentModule.getModuleName());
        contributorModuleData.setCategoryName(assessmentModule.getCategory().getCategoryName());
        contributorModuleData.setCategoryId(assessmentModule.getCategory().getCategoryId());
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
        contributorTopicData.setTopicId(assessmentTopic.getTopicId());
        contributorTopicData.setTopicName(assessmentTopic.getTopicName());
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            List<Question> parameterQuestions = getQuestionsByParameter(questionList, assessmentParameter);
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
        contributorParameterData.setParameterId(assessmentParameter.getParameterId());
        contributorParameterData.setParameterName(assessmentParameter.getParameterName());
        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (Question question : questionList) {
            ContributorQuestionData contributorQuestionData = modelMapper.map(question, ContributorQuestionData.class);
            contributorQuestionDataList.add(contributorQuestionData);
            contributorParameterData.setQuestions(contributorQuestionDataList);
        }

        return contributorParameterData;
    }


    private List<Question> getQuestionsByParameter(List<Question> questionList, AssessmentParameter assessmentParameter) {
        return questionList.stream().filter(question ->
                Objects.equals(question.getParameter().getParameterId(), assessmentParameter.getParameterId())
        ).toList();
    }


    private QuestionStatusUpdateResponse updateContributorQuestion(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        QuestionStatusUpdateResponse questionStatusUpdateResponse = new QuestionStatusUpdateResponse();
        questionStatusUpdateResponse.setQuestionId(questionStatusUpdateRequest.getQuestionId());
        for (Integer questionId : questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if (question.isNextStatusAllowed(status)) {
                question.setComments(questionStatusUpdateRequest.getComments());
                question.setQuestionStatus(status);
                updateQuestion(question);
                questionStatusUpdateResponse.setStatus(question.getQuestionStatus());
                questionStatusUpdateResponse.setComments(question.getComments());
            }
        }
        return questionStatusUpdateResponse;
    }
}


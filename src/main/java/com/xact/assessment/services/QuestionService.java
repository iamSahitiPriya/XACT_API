/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import java.util.*;

@Singleton
public class QuestionService {
    private final QuestionRepository questionRepository;

    private final UserQuestionService userQuestionService;

    private final ModuleContributorService moduleContributorService;

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
                question.setQuestionStatus(ContributorQuestionStatus.Idle);
                createQuestion(question);
                userQuestion.setContributionStatus(true);
                userQuestionService.updateUserQuestion(userQuestion);
            }
        }
    }

    public ContributorDataResponse getContributorQuestions(ContributorRole contributorRole, String userEmail) {
        List<AssessmentModule> authorAssessmentModules = moduleContributorService.getModuleByRole(userEmail, contributorRole);

        List<Question> questionList = getContributorQuestions(contributorRole);

        ContributorDataResponse contributorDataResponse = new ContributorDataResponse();
        List<ContributorCategoryData> contributorCategoryDataList = getContributorCategoryData(questionList, authorAssessmentModules);
        contributorDataResponse.setContributorCategoryDataList(contributorCategoryDataList);

        return contributorDataResponse;
    }

    private List<Question> getContributorQuestions(ContributorRole contributorRole) {
        List<Question> questionList;
        if (contributorRole.equals(ContributorRole.Author)) {
            questionList = questionRepository.getAuthorQuestions();
        } else {
            questionList = questionRepository.getReviewerQuestions();
        }
        return questionList;
    }

    private List<ContributorCategoryData> getContributorCategoryData(List<Question> questionList, List<AssessmentModule> authorAssessmentModules) {
        List<ContributorCategoryData> contributorCategoryDataList = new ArrayList<>();
        HashMap<AssessmentCategory, List<AssessmentModule>> categoryListHashMap = getMappedCategoryModules(authorAssessmentModules);
        categoryListHashMap.forEach((key, value) -> {
            ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
            contributorCategoryData.setCategoryName(key.getCategoryName());
            List<ContributorModuleData> contributorModuleDataList = getContributorModuleData(questionList, value);
            contributorCategoryData.setContributorModuleData(contributorModuleDataList);
            contributorCategoryDataList.add(contributorCategoryData);
        });
        return contributorCategoryDataList;
    }

    private HashMap<AssessmentCategory, List<AssessmentModule>> getMappedCategoryModules(List<AssessmentModule> authorAssessmentModules) {
        HashMap<AssessmentCategory, List<AssessmentModule>> categoryListHashMap = new HashMap<>();
        for (AssessmentModule assessmentModule : authorAssessmentModules) {
            if (categoryListHashMap.containsKey(assessmentModule.getCategory())) {
                categoryListHashMap.get(assessmentModule.getCategory()).add(assessmentModule);
            } else {
                categoryListHashMap.put(assessmentModule.getCategory(), Collections.singletonList(assessmentModule));
            }
        }
        return categoryListHashMap;
    }


    private List<ContributorModuleData> getContributorModuleData(List<Question> questionList, List<AssessmentModule> assessmentModules) {
        List<ContributorModuleData> contributorModuleDataList = new ArrayList<>();
        for (AssessmentModule assessmentModule : assessmentModules) {
            ContributorModuleData contributorModuleData = new ContributorModuleData();
            contributorModuleData.setModuleName(assessmentModule.getModuleName());
            setContributorTopicData(questionList, assessmentModule, contributorModuleData);
            contributorModuleDataList.add(contributorModuleData);
        }
        return contributorModuleDataList;
    }

    private void setContributorTopicData(List<Question> questionList, AssessmentModule assessmentModule, ContributorModuleData contributorModuleData) {
        List<ContributorTopicData> contributorTopicDataList = new ArrayList<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            ContributorTopicData contributorTopicData = new ContributorTopicData();
            contributorTopicData.setTopicName(assessmentTopic.getTopicName());
            setContributorParameterData(questionList, assessmentTopic, contributorTopicData);
            contributorTopicDataList.add(contributorTopicData);
            contributorModuleData.setContributorTopicDataList(contributorTopicDataList);
        }
    }

    private void setContributorParameterData(List<Question> questionList, AssessmentTopic assessmentTopic, ContributorTopicData contributorTopicData) {
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            ContributorParameterData contributorParameterData = new ContributorParameterData();
            contributorParameterData.setParameterName(assessmentParameter.getParameterName());
            setContributorQuestionData(questionList, contributorParameterDataList, assessmentParameter, contributorParameterData);
            contributorTopicData.setContributorParameterDataList(contributorParameterDataList);
        }
    }

    private void setContributorQuestionData(List<Question> questionList, List<ContributorParameterData> contributorParameterDataList, AssessmentParameter assessmentParameter, ContributorParameterData contributorParameterData) {

        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (Question question : questionList) {
            if (question.getParameter().equals(assessmentParameter)) {
                ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
                setContributorQuestionData(question, contributorQuestionData);
                contributorQuestionDataList.add(contributorQuestionData);
            }
            contributorParameterData.setContributorQuestionDataList(contributorQuestionDataList);
        }
        contributorParameterDataList.add(contributorParameterData);
    }

    private void setContributorQuestionData(Question question, ContributorQuestionData contributorQuestionData) {
        contributorQuestionData.setQuestionId(question.getQuestionId());
        contributorQuestionData.setQuestion(question.getQuestionText());
        contributorQuestionData.setContributorQuestionStatus(question.getQuestionStatus());
        contributorQuestionData.setComments(question.getComments());
    }

    public void updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author && status == ContributorQuestionStatus.Sent_For_Review) {
            updateAuthorQuestionsStatus(status, questionStatusUpdateRequest);
        } else if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Reviewer && (status == ContributorQuestionStatus.Requested_For_Change || status == ContributorQuestionStatus.Approved || status == ContributorQuestionStatus.Rejected)) {
            updateReviewerQuestionsStatus(status, questionStatusUpdateRequest);
        }

    }

    private void updateReviewerQuestionsStatus(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        for (Integer questionId : questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if (question.getQuestionStatus() == ContributorQuestionStatus.Sent_For_Review) {
                updateQuestionStatus(status, questionStatusUpdateRequest, question);
            }

        }
    }


    private void updateAuthorQuestionsStatus(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        for (Integer questionId : questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if (question.getQuestionStatus() == ContributorQuestionStatus.Idle) {
                updateQuestionStatus(status, questionStatusUpdateRequest, question);
            }
        }
    }

    private void updateQuestionStatus(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Question question) {
        question.setQuestionStatus(status);
        question.setComments(questionStatusUpdateRequest.getComments());
        questionRepository.update(question);
    }

}

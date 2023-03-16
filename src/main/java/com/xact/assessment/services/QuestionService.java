/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.dtos.ContributorRole.Author;

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

    public ContributorDataResponse getContributorQuestions(String userEmail) {
        List<AssessmentModule> authorAssessmentModules = moduleContributorService.getModuleByRole(userEmail,Author);

        List<Question> questionList = (List<Question>) questionRepository.findAll();
        ContributorDataResponse contributorDataResponse = new ContributorDataResponse();
        List<ContributorModuleData> contributorModuleDataList = getContributorModuleData(questionList, authorAssessmentModules);
        contributorDataResponse.setContributorModuleData(contributorModuleDataList);

        return contributorDataResponse;
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
        contributorQuestionData.setAuthorComments(question.getAuthorComments());
        contributorQuestionData.setReviewerComments(question.getReviewerComments());
    }

}

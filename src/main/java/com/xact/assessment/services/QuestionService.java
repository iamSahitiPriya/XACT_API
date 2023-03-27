/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.QuestionRepository;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.stream.Collectors;

import static com.xact.assessment.dtos.ContributorQuestionStatus.*;

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

    public ContributorResponse getContributorResponse(ContributorRole contributorRole, String userEmail) {
        List<AssessmentModule> assessmentModules = moduleContributorService.getModuleByRole(userEmail, contributorRole);
        HashMap<String, List<ContributorModuleData>> categoryListHashMap = createCategoryHashMap(contributorRole, assessmentModules);
        ContributorResponse contributorResponse = generateFromCategoryHashMap(categoryListHashMap);
        return contributorResponse;
    }

    private ContributorResponse generateFromCategoryHashMap(HashMap<String, List<ContributorModuleData>> categoryListHashMap) {
        ContributorResponse contributorResponse = new ContributorResponse();
        List<ContributorCategoryData> contributorCategoryDataList = new ArrayList<>();

        categoryListHashMap.forEach((key, value) -> {
            ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
            contributorCategoryData.setCategoryName(key);
            contributorCategoryData.setModules(value);
            contributorCategoryDataList.add(contributorCategoryData);
        });
        contributorResponse.setCategories(contributorCategoryDataList);
        return contributorResponse;
    }

    private HashMap<String, List<ContributorModuleData>> createCategoryHashMap(ContributorRole contributorRole, List<AssessmentModule> assessmentModules) {
        HashMap<String, List<ContributorModuleData>> categoryListHashMap = new HashMap<>();
        for (AssessmentModule assessmentModule : assessmentModules) {
            List<Question> contributorQuestions = getContributorQuestions(contributorRole, assessmentModule.getModuleId());
            ContributorModuleData contributorModuleData = getContributorModuleData(contributorQuestions, assessmentModule);
            if (contributorModuleData.getModuleName() != null) {
                setCategoryHashMap(categoryListHashMap, assessmentModule, contributorModuleData);
            }
        }
        return categoryListHashMap;
    }

    private void setCategoryHashMap(HashMap<String, List<ContributorModuleData>> categoryListHashMap, AssessmentModule assessmentModule, ContributorModuleData contributorModuleData) {
        if (categoryListHashMap.containsKey(assessmentModule.getCategory().getCategoryName())) {
            List<ContributorModuleData> contributorModuleDataList = categoryListHashMap.get(assessmentModule.getCategory().getCategoryName());
            contributorModuleDataList.add(contributorModuleData);
        } else {
            List<ContributorModuleData> moduleList = new ArrayList<>();
            moduleList.add(contributorModuleData);
            categoryListHashMap.put(assessmentModule.getCategory().getCategoryName(), moduleList);
        }
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
        List<ContributorTopicData> contributorTopicDataList = getContributorTopicData(questionList, assessmentModule);
        setContributorModuleData(assessmentModule, contributorModuleData, contributorTopicDataList);

        return contributorModuleData;
    }

    private void setContributorModuleData(AssessmentModule assessmentModule, ContributorModuleData contributorModuleData, List<ContributorTopicData> contributorTopicDataList) {
        if (!contributorTopicDataList.isEmpty()) {
            contributorModuleData.setModuleName(assessmentModule.getModuleName());
            contributorModuleData.setTopics(contributorTopicDataList);
        }
    }

    private List<ContributorTopicData> getContributorTopicData(List<Question> questionList, AssessmentModule assessmentModule) {
        List<ContributorTopicData> contributorTopicDataList = new ArrayList<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            setContributorTopicData(questionList, contributorTopicDataList, assessmentTopic);
        }
        return contributorTopicDataList;
    }

    private void setContributorTopicData(List<Question> questionList, List<ContributorTopicData> contributorTopicDataList, AssessmentTopic assessmentTopic) {
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        List<ContributorParameterData> contributorParameterDataList = getContributorParameterData(questionList, assessmentTopic, contributorTopicData);
        if (!contributorParameterDataList.isEmpty()) {
            contributorTopicData.setTopicName(assessmentTopic.getTopicName());
            contributorTopicDataList.add(contributorTopicData);
        }
    }

    private List<ContributorParameterData> getContributorParameterData(List<Question> questionList, AssessmentTopic assessmentTopic, ContributorTopicData contributorTopicData) {
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            List<Question> parameterQuestions;
            parameterQuestions = getFilteredParameterQuestions(questionList, assessmentParameter);
            setContributorParameterData(contributorTopicData, contributorParameterDataList, assessmentParameter, parameterQuestions);
        }
        return contributorParameterDataList;
    }

    private List<Question> getFilteredParameterQuestions(List<Question> questionList, AssessmentParameter assessmentParameter) {
        return questionList.stream().filter(question ->
                Objects.equals(question.getParameter().getParameterId(), assessmentParameter.getParameterId())
        ).collect(Collectors.toList());
    }

    private void setContributorParameterData(ContributorTopicData contributorTopicData, List<ContributorParameterData> contributorParameterDataList, AssessmentParameter assessmentParameter, List<Question> parameterQuestions) {
        if (!parameterQuestions.isEmpty()) {
            ContributorParameterData contributorParameterData = new ContributorParameterData();
            contributorParameterData.setParameterName(assessmentParameter.getParameterName());
            setContributorParameterQuestions(parameterQuestions, contributorParameterDataList, assessmentParameter, contributorParameterData);
            contributorTopicData.setParameters(contributorParameterDataList);
        }
    }

    private void setContributorParameterQuestions(List<Question> questionList, List<ContributorParameterData> contributorParameterDataList, AssessmentParameter assessmentParameter, ContributorParameterData contributorParameterData) {

        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (Question question : questionList) {
            ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
            setContributorQuestionData(question, contributorQuestionData);
            contributorQuestionDataList.add(contributorQuestionData);
            contributorParameterData.setQuestions(contributorQuestionDataList);
        }
        contributorParameterDataList.add(contributorParameterData);
    }

    private void setContributorQuestionData(Question question, ContributorQuestionData contributorQuestionData) {
        contributorQuestionData.setQuestionId(question.getQuestionId());
        contributorQuestionData.setQuestion(question.getQuestionText());
        contributorQuestionData.setStatus(question.getQuestionStatus());
        contributorQuestionData.setComments(question.getComments());
    }

    public void updateContributorQuestionsStatus(Integer moduleId, ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, String userEmail) {
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author) {
            updateAuthorQuestionsStatus(questionStatusUpdateRequest);
        } else if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Reviewer && isStatusAllowed(status)) {

            updateReviewerQuestionsStatus(status, questionStatusUpdateRequest);
        }

    }

    private boolean isStatusAllowed(ContributorQuestionStatus status) {
        return status == ContributorQuestionStatus.Requested_For_Change || status == ContributorQuestionStatus.Approved || status == ContributorQuestionStatus.Rejected;
    }

    private void updateReviewerQuestionsStatus(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        for (Integer questionId : questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if (question.getQuestionStatus() == ContributorQuestionStatus.Sent_For_Review) {
                updateQuestionStatus(status, questionStatusUpdateRequest, question);
            }

        }
    }


    private void updateAuthorQuestionsStatus(QuestionStatusUpdateRequest questionStatusUpdateRequest) {
        for (Integer questionId : questionStatusUpdateRequest.getQuestionId()) {
            Question question = questionRepository.findById(questionId).orElseThrow();
            if (question.getQuestionStatus() == ContributorQuestionStatus.Idle) {
                updateQuestionStatus(Sent_For_Review, questionStatusUpdateRequest, question);
            }
        }
    }

    private void updateQuestionStatus(ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Question question) {
        question.setQuestionStatus(status);
        question.setComments(questionStatusUpdateRequest.getComments());
        questionRepository.update(question);
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
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author && question.getQuestionStatus() == Idle) {
            updateQuestion(question);
        }
    }


}

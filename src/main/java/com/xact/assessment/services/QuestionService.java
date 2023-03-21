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

import static com.xact.assessment.dtos.ContributorQuestionStatus.Published;

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

    public ContributorResponse getContributorQuestions(ContributorRole contributorRole, String userEmail) {
        List<AssessmentModule> authorAssessmentModules = moduleContributorService.getModuleByRole(userEmail, contributorRole);
        List<Question> parameterLevelQuestions = new ArrayList<>();
        HashMap<String, List<ContributorModuleData>> categoryListHashMap = new HashMap<>();

        for (AssessmentModule assessmentModule : authorAssessmentModules) {
            parameterLevelQuestions = getContributorQuestions(contributorRole, assessmentModule.getModuleId());
            ContributorModuleData contributorModuleData = getContributorModuleData(parameterLevelQuestions, assessmentModule);

            if (categoryListHashMap.containsKey(assessmentModule.getCategory().getCategoryName())) {
                List<ContributorModuleData> contributorModuleDataList = categoryListHashMap.get(assessmentModule.getCategory().getCategoryName());
                contributorModuleDataList.add(contributorModuleData);

            } else {
                List<ContributorModuleData> moduleList = new ArrayList<>();
                moduleList.add(contributorModuleData);
                categoryListHashMap.put(assessmentModule.getCategory().getCategoryName(), moduleList);
            }

            System.out.println("moduleId" + assessmentModule.getModuleId());
            System.out.println("parameteRQUESTION" + parameterLevelQuestions.size());
//            parameterLevelQuestions.addAll(questionList);
        }
        ContributorResponse contributorResponse = new ContributorResponse();
        List<ContributorCategoryData> contributorCategoryDataList = new ArrayList<>();

        categoryListHashMap.forEach((key, value) -> {
            ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
            contributorCategoryData.setCategoryName(key);
            contributorCategoryData.setModules(value);
            contributorCategoryDataList.add(contributorCategoryData);
        });
        contributorResponse.setCategories(contributorCategoryDataList);
//        List<Question> questionList = getContributorQuestions(contributorRole);

//        ContributorResponse contributorResponse = new ContributorResponse();
//        List<ContributorCategoryData> contributorCategoryDataList = getContributorCategoryData(parameterLevelQuestions, authorAssessmentModules);
//        contributorResponse.setCategories(contributorCategoryDataList);

        return contributorResponse;
    }

    private List<Question> getContributorQuestions(ContributorRole contributorRole, Integer moduleId) {
        List<Question> questionList;
        if (contributorRole.equals(ContributorRole.Author)) {
            questionList = questionRepository.getAuthorQuestions(moduleId);
        } else {
            questionList = questionRepository.getReviewerQuestions();
        }
        return questionList;
    }

//    private List<ContributorCategoryData> getContributorCategoryData(List<Question> questionList, List<AssessmentModule> authorAssessmentModules) {
//        List<ContributorCategoryData> contributorCategoryDataList = new ArrayList<>();
//        HashMap<AssessmentCategory, List<AssessmentModule>> categoryListHashMap = getMappedCategoryModules(authorAssessmentModules);
//        categoryListHashMap.forEach((key, value) -> {
//            ContributorCategoryData contributorCategoryData = new ContributorCategoryData();
//            contributorCategoryData.setCategoryName(key.getCategoryName());
//            List<ContributorModuleData> contributorModuleDataList = getContributorModuleData(questionList, value);
//            contributorCategoryData.setModules(contributorModuleDataList);
//            contributorCategoryDataList.add(contributorCategoryData);
//        });
//        return contributorCategoryDataList;
//    }
//
//    private HashMap<AssessmentCategory, List<AssessmentModule>> getMappedCategoryModules(List<AssessmentModule> authorAssessmentModules) {
//        HashMap<AssessmentCategory, List<AssessmentModule>> categoryListHashMap = new HashMap<>();
//        System.out.println("authorAssessment" + authorAssessmentModules.size());
//        for (AssessmentModule assessmentModule : authorAssessmentModules) {
//            if (categoryListHashMap.containsKey(assessmentModule.getCategory())) {
//                categoryListHashMap.get(assessmentModule.getCategory()).add(assessmentModule);
//            } else {
//                List<AssessmentModule> moduleList = new ArrayList<>();
//                moduleList.add(assessmentModule);
//                categoryListHashMap.put(assessmentModule.getCategory(), moduleList);
//            }
//        }
//        return categoryListHashMap;
//    }


    private ContributorModuleData getContributorModuleData(List<Question> questionList, AssessmentModule assessmentModule) {
//       ContributorModuleData contributorModuleDataList = new ArrayList<>();
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        List<ContributorTopicData> contributorTopicDataList = setContributorTopicData(questionList, assessmentModule, contributorModuleData);
        if (!contributorTopicDataList.isEmpty()) {
            contributorModuleData.setModuleName(assessmentModule.getModuleName());
            setContributorTopicData(questionList, assessmentModule, contributorModuleData);
        }

//        contributorModuleDataList.add(contributorModuleData);
        return contributorModuleData;
    }

    private List<ContributorTopicData> setContributorTopicData(List<Question> questionList, AssessmentModule assessmentModule, ContributorModuleData contributorModuleData) {
        List<ContributorTopicData> contributorTopicDataList = new ArrayList<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            ContributorTopicData contributorTopicData = new ContributorTopicData();
            List<ContributorParameterData> contributorParameterDataList = setContributorParameterData(questionList, assessmentTopic, contributorTopicData);
            if (!contributorParameterDataList.isEmpty()) {
                contributorTopicData.setTopicName(assessmentTopic.getTopicName());
                contributorTopicDataList.add(contributorTopicData);
                contributorModuleData.setTopics(contributorTopicDataList);
            }
        }
        return contributorTopicDataList;
    }

    private List<ContributorParameterData> setContributorParameterData(List<Question> questionList, AssessmentTopic assessmentTopic, ContributorTopicData contributorTopicData) {
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            List<Question> parameterLevelQuestions;
            parameterLevelQuestions = questionList.stream().filter(question ->
                    Objects.equals(question.getParameter().getParameterId(), assessmentParameter.getParameterId())
            ).collect(Collectors.toList());
            if (!parameterLevelQuestions.isEmpty()) {
                ContributorParameterData contributorParameterData = new ContributorParameterData();
                contributorParameterData.setParameterName(assessmentParameter.getParameterName());
                setContributorQuestionData(parameterLevelQuestions, contributorParameterDataList, assessmentParameter, contributorParameterData);
                contributorTopicData.setParameters(contributorParameterDataList);
            }
        }
        return contributorParameterDataList;
    }

    private void setContributorQuestionData(List<Question> questionList, List<ContributorParameterData> contributorParameterDataList, AssessmentParameter assessmentParameter, ContributorParameterData contributorParameterData) {

        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (Question question : questionList) {
            if (question.getParameter().equals(assessmentParameter)) {
                ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
                setContributorQuestionData(question, contributorQuestionData);
                contributorQuestionDataList.add(contributorQuestionData);
            }
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

    public void deleteQuestion(Integer questionId, String userEmail) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        Integer moduleId = question.getParameter().getTopic().getModule().getModuleId();
        if (moduleContributorService.getRole(moduleId, userEmail) == ContributorRole.Author && question.getQuestionStatus() != Published) {
            questionRepository.delete(question);
        }

    }
}

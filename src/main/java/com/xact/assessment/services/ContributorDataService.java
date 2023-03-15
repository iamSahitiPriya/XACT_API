package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ContributorDataRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ContributorDataService {
    private final ContributorDataRepository contributorDataRepository;
    private final ModuleService moduleService;

    private final UserQuestionService userQuestionService;

    public ContributorDataService(ContributorDataRepository contributorDataRepository, ModuleService moduleService, UserQuestionService userQuestionService) {
        this.contributorDataRepository = contributorDataRepository;

        this.moduleService = moduleService;
        this.userQuestionService = userQuestionService;
    }

    public void save(List<UserQuestion> userQuestionList) {
        for (UserQuestion userQuestion : userQuestionList) {
            if (!userQuestion.isContributionStatus()) {
                ContributorData contributorData = new ContributorData();
                contributorData.setQuestion(userQuestion.getQuestion());
                contributorData.setParameter(userQuestion.getParameter());
                contributorDataRepository.save(contributorData);
                userQuestion.setContributionStatus(true);
               userQuestionService.updateUserQuestion(userQuestion);
            }
        }
    }


    public ContributorDataResponse getContributorQuestions(String userEmail) {
        List<AssessmentModule> assessmentModules = moduleService.getModuleByAuthor(userEmail);
        List<ContributorData> contributorDataList = contributorDataRepository.findByAuthor(userEmail);
        ContributorDataResponse contributorDataResponse = new ContributorDataResponse();
        List<ContributorModuleData> contributorModuleDataList = getContributorModuleData(contributorDataList, assessmentModules);
        contributorDataResponse.setContributorModuleData(contributorModuleDataList);

        return contributorDataResponse;
    }


    private List<ContributorModuleData> getContributorModuleData(List<ContributorData> contributorDataList, List<AssessmentModule> assessmentModules) {
        List<ContributorModuleData> contributorModuleDataList = new ArrayList<>();
        for (AssessmentModule assessmentModule : assessmentModules) {
            ContributorModuleData contributorModuleData = new ContributorModuleData();
            contributorModuleData.setModuleName(assessmentModule.getModuleName());
            setContributorTopicData(contributorDataList, assessmentModule, contributorModuleData);
            contributorModuleDataList.add(contributorModuleData);
        }
        return contributorModuleDataList;
    }

    private void setContributorTopicData(List<ContributorData> contributorDataList, AssessmentModule assessmentModule, ContributorModuleData contributorModuleData) {
        List<ContributorTopicData> contributorTopicDataList = new ArrayList<>();
        for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
            ContributorTopicData contributorTopicData = new ContributorTopicData();
            contributorTopicData.setTopicName(assessmentTopic.getTopicName());
            setContributorParameterData(contributorDataList, assessmentTopic, contributorTopicData);
            contributorTopicDataList.add(contributorTopicData);
            contributorModuleData.setContributorTopicDataList(contributorTopicDataList);
        }
    }

    private void setContributorParameterData(List<ContributorData> contributorDataList, AssessmentTopic assessmentTopic, ContributorTopicData contributorTopicData) {
        List<ContributorParameterData> contributorParameterDataList = new ArrayList<>();
        for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
            ContributorParameterData contributorParameterData = new ContributorParameterData();
            contributorParameterData.setParameterName(assessmentParameter.getParameterName());
            setContributorQuestionData(contributorDataList, contributorParameterDataList, assessmentParameter, contributorParameterData);
            contributorTopicData.setContributorParameterDataList(contributorParameterDataList);
        }
    }

    private void setContributorQuestionData(List<ContributorData> contributorDataList, List<ContributorParameterData> contributorParameterDataList, AssessmentParameter assessmentParameter, ContributorParameterData contributorParameterData) {
        List<ContributorQuestionData> contributorQuestionDataList = new ArrayList<>();
        for (ContributorData contributorData : contributorDataList) {
            if (contributorData.getParameter().equals(assessmentParameter)) {
                ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
                setContributorQuestionData(contributorData, contributorQuestionData);
                contributorQuestionDataList.add(contributorQuestionData);
            }
            contributorParameterData.setContributorQuestionDataList(contributorQuestionDataList);
        }
        contributorParameterDataList.add(contributorParameterData);
    }

    private void setContributorQuestionData(ContributorData contributorData, ContributorQuestionData contributorQuestionData) {
        contributorQuestionData.setQuestionId(contributorData.getQuestionId());
        contributorQuestionData.setQuestion(contributorData.getQuestion());
        contributorQuestionData.setContributorQuestionStatus(contributorData.getQuestionStatus());
        contributorQuestionData.setAuthorComments(contributorData.getAuthorComments());
        contributorQuestionData.setReviewerComments(contributorData.getReviewerComments());
    }
}

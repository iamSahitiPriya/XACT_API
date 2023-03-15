package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorData;
import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.repositories.ContributorDataRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ContributorDataService {
    private final ContributorDataRepository contributorDataRepository;
    private final ModuleService moduleService;

    public ContributorDataService(ContributorDataRepository contributorDataRepository, ModuleService moduleService) {
        this.contributorDataRepository = contributorDataRepository;

        this.moduleService = moduleService;
    }
    public void save(List<UserQuestion> userQuestionList){
        for (UserQuestion userQuestion: userQuestionList) {
            ContributorData contributorData = new ContributorData();
            contributorData.setQuestion(userQuestion.getQuestion());
            contributorData.setParameter(userQuestion.getParameter());
                contributorDataRepository.save(contributorData);

        }
    }

    public List<ContributorData> getQuestions(String userEmail) {
//        List<AssessmentModule> assessmentModuleList = moduleService.getModuleByAuthor(userEmail);
        List<ContributorData> contributorDataList = contributorDataRepository.findByAuthor(userEmail);
//        List<ContributorData> contributorDataList = new ArrayList<>();
//        for (ContributorData contributorData: contributorDataIterable) {
//            if (assessmentModuleList.contains(contributorData.getParameter().getTopic().getModule())){
//                contributorDataList.add(contributorData);
//            }
//        }
        return contributorDataList;
    }
}

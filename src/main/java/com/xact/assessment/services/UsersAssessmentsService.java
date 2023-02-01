/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;


import com.xact.assessment.dtos.ModuleRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.UserAssessmentModuleRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;

import java.util.*;

@Singleton
public class UsersAssessmentsService {
    UsersAssessmentsRepository usersAssessmentsRepository;
    private final UserAssessmentModuleRepository userAssessmentModuleRepository;
    private final UserQuestionService userQuestionService;
    private final ModuleService moduleService;


    public UsersAssessmentsService(UsersAssessmentsRepository assessmentRepository, UserAssessmentModuleRepository userAssessmentModuleRepository, UserQuestionService userQuestionService, ModuleService moduleService) {
        this.usersAssessmentsRepository = assessmentRepository;
        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
        this.userQuestionService = userQuestionService;
        this.moduleService = moduleService;
    }

    public List<Assessment> findAssessments(String userEmail) {
        List<AssessmentUser> assessmentUsers = usersAssessmentsRepository.findByUserEmail(userEmail);

        List<Assessment> usersAssessments = new ArrayList<>();
        for (AssessmentUser user : assessmentUsers) {
            usersAssessments.add(user.getUserId().getAssessment());
        }

        return usersAssessments;

    }

    public void createUsersInAssessment(Set<AssessmentUser> assessmentUsers) {
        usersAssessmentsRepository.saveAll(assessmentUsers);
    }

    public Set<AssessmentUser> getAssessmentFacilitatorsSet(Assessment assessment) {
        List<AssessmentUser> assessmentFacilitators = usersAssessmentsRepository.findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator);
        return new HashSet<>(assessmentFacilitators);
    }

    public Assessment getAssessment(Integer assessmentId, User user) {
        AssessmentUser assessmentUser = usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), assessmentId);
        return assessmentUser.getUserId().getAssessment();
    }

    public List<String> getAssessmentFacilitators(Integer assessmentId) {
        List<AssessmentUser> assessmentUsers = usersAssessmentsRepository.findUserByAssessmentId(assessmentId, AssessmentRole.Facilitator);
        List<String> assessmentUsers1 = new ArrayList<>();
        for (AssessmentUser eachUser : assessmentUsers) {
            assessmentUsers1.add(eachUser.getUserId().getUserEmail());
        }
        return assessmentUsers1;
    }

    public void saveAssessmentModules(List<ModuleRequest> moduleRequests, Assessment assessment) {
        for (ModuleRequest moduleRequest1 : moduleRequests) {
            UserAssessmentModule userAssessmentModule = new UserAssessmentModule();
            userAssessmentModule.setAssessment(assessment);
            AssessmentModule assessmentModule = moduleService.getModule(moduleRequest1.getModuleId());
            AssessmentModuleId assessmentModuleId = new AssessmentModuleId(assessment, assessmentModule);
            userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
            userAssessmentModule.setModule(assessmentModule);
            userAssessmentModuleRepository.save(userAssessmentModule);
        }
    }

    public List<UserQuestion> findAllUserQuestion(Integer assessmentId) {
        return userQuestionService.findAllUserQuestion(assessmentId);
    }

    public UserQuestion saveUserQuestion(Assessment assessment, Integer parameterId, String userQuestion) {
        return userQuestionService.saveUserQuestion(assessment, parameterId, userQuestion);
    }

    public void saveUserAnswer(Integer questionId, String answer) {
        userQuestionService.saveUserAnswer(questionId, answer);
    }

    public void updateUserQuestion(Integer questionId, String updatedQuestion) {
        userQuestionService.updateUserQuestion(questionId, updatedQuestion);
    }

    public Optional<UserQuestion> searchUserQuestion(Integer questionId) {
        return userQuestionService.searchUserQuestion(questionId);
    }

    public void deleteUserQuestion(Integer questionId) {
        userQuestionService.deleteUserQuestion(questionId);
    }

    public void updateAssessmentModules(List<ModuleRequest> moduleRequest, Assessment assessment) {
        userAssessmentModuleRepository.deleteByModule(assessment.getAssessmentId());
        saveAssessmentModules(moduleRequest, assessment);
    }


    public void updateUsersInAssessment(Set<AssessmentUser> assessmentUsers, Integer assessmentId) {
        usersAssessmentsRepository.deleteUsersByAssessmentId(assessmentId);
        usersAssessmentsRepository.saveAll(assessmentUsers);
    }
}


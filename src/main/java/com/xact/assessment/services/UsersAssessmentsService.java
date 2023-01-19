/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;


import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentUser;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class UsersAssessmentsService {
    UsersAssessmentsRepository usersAssessmentsRepository;

    public UsersAssessmentsService(UsersAssessmentsRepository assessmentRepository) {
        this.usersAssessmentsRepository = assessmentRepository;
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


    public void updateUsersInAssessment(Set<AssessmentUser> assessmentUsers, Integer assessmentId) {
        usersAssessmentsRepository.deleteUsersByAssessmentId(assessmentId);
        usersAssessmentsRepository.saveAll(assessmentUsers);
    }
}


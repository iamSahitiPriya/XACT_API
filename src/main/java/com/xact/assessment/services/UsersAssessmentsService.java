package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentUsers;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class UsersAssessmentsService {
    UsersAssessmentsRepository usersAssessmentsRepository;

    public UsersAssessmentsService(UsersAssessmentsRepository assessmentRepository) {
        this.usersAssessmentsRepository = assessmentRepository;
    }

    public List<Assessment> findAssessments(String user_email) {
        List<AssessmentUsers> assessment_users = usersAssessmentsRepository.findByUserEmail(user_email);

        List<Assessment> usersAssessments = new ArrayList<>();
        for (AssessmentUsers user : assessment_users) {
            usersAssessments.add(user.getUserId().getAssessment());
        }

        return usersAssessments;

    }


}


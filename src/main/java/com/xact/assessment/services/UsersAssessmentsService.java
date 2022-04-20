package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentUsers;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UsersAssessmentsService {
    UsersAssessmentsRepository usersAssessmentsRepository;

    public UsersAssessmentsService(UsersAssessmentsRepository assessmentRepository) {
        this.usersAssessmentsRepository = assessmentRepository;
    }

    public List<Assessment> findAssessments(String userEmail) {
        List<AssessmentUsers> assessmentUsers = usersAssessmentsRepository.findByUserEmail(userEmail);

        List<Assessment> usersAssessments = new ArrayList<>();
        for (AssessmentUsers user : assessmentUsers) {
            usersAssessments.add(user.getUserId().getAssessment());
        }

        return usersAssessments;

    }

    @Transactional
    public List<AssessmentUsers> createUsersInAssessment(List<AssessmentUsers> assessmentUsers) {
         return (List<AssessmentUsers>) usersAssessmentsRepository.saveAll(assessmentUsers);
    }


}


/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.AssessmentsRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;


@Singleton
public class AssessmentService {

    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentRepository assessmentRepository;
    private final UsersAssessmentsRepository usersAssessmentsRepository;
    private final AssessmentsRepository assessmentsRepository;

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository, AssessmentsRepository assessmentsRepository) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
        this.assessmentsRepository = assessmentsRepository;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);
        List<AssessmentUsers> assessmentUsers = getAssessmentUsers(assessmentRequest, user, assessment);

        createAssessment(assessment);
        usersAssessmentsService.createUsersInAssessment(assessmentUsers);

        return assessment;
    }

    private void createAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    private List<AssessmentUsers> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedinUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();

        List<AssessmentUsers> assessmentUsers = new ArrayList<>();
        for (UserDto eachUser : users) {
            eachUser.setRole(UserRole.Facilitator);
            if (loggedinUser.getUserEmail().equals(eachUser.getEmail())) {
                eachUser.setRole(UserRole.Owner);
            }
            AssessmentUsers assessmentUser = mapper.map(eachUser, AssessmentUsers.class);
            assessmentUser.setUserId(new UserId(eachUser.getEmail(), assessment));
            assessmentUsers.add(assessmentUser);

        }
        return assessmentUsers;
    }

    public Assessment getAssessment(Integer assessmentId, User user) {
        AssessmentUsers assessmentUsers = usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), assessmentId);
        return assessmentUsers.getUserId().getAssessment();
    }

    public Assessment finishAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Completed);
        assessmentsRepository.update(assessment);
        return assessment;
    }

    public Assessment reopenAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Active);
        assessmentsRepository.update(assessment);
        return assessment;
    }
}

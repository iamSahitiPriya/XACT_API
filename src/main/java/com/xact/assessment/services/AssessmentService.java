/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;


@Singleton
public class AssessmentService {

    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentRepository assessmentRepository;
    private final UsersAssessmentsRepository usersAssessmentsRepository;

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);
        Set<AssessmentUsers> assessmentUsers = getAssessmentUsers(assessmentRequest, user, assessment);
        createAssessment(assessment);
        usersAssessmentsService.createUsersInAssessment(assessmentUsers);
        return assessment;
    }

    private void createAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    public Set<AssessmentUsers> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedinUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();

        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        for (UserDto eachUser : users) {
            if (!eachUser.getEmail().isBlank()) {
                eachUser.setRole(UserRole.Facilitator);
                if (loggedinUser.getUserEmail().equals(eachUser.getEmail())) {
                    eachUser.setRole(UserRole.Owner);
                }
                AssessmentUsers assessmentUser = mapper.map(eachUser, AssessmentUsers.class);
                assessmentUser.setUserId(new UserId(eachUser.getEmail(), assessment));
                assessmentUsers.add(assessmentUser);
            }
        }
        return assessmentUsers;
    }

    public Assessment getAssessment(Integer assessmentId, User user) {
        AssessmentUsers assessmentUsers = usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), assessmentId);
        return assessmentUsers.getUserId().getAssessment();
    }

    public List<String> getAssessmentUsers(Integer assessmentId) {
        List<AssessmentUsers> assessmentUsers = usersAssessmentsRepository.findUserByAssessmentId(assessmentId, AssessmentRole.Facilitator);
        List<String> assessmentUsers1 = new ArrayList<>();
        for (AssessmentUsers eachUser : assessmentUsers) {

            assessmentUsers1.add(eachUser.getUserId().getUserEmail());
        }
        return assessmentUsers1;
    }

    public Assessment finishAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Completed);
        assessmentRepository.update(assessment);
        return assessment;
    }

    public Assessment reopenAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Active);
        assessmentRepository.update(assessment);
        return assessment;
    }


    @Transactional
    public void updateAssessment(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
        assessmentRepository.update(assessment);
        usersAssessmentsService.updateUsersInAssessment(assessmentUsers, assessment.getAssessmentId());
    }
    public void updateAssessment(Assessment assessment){
        assessmentRepository.update(assessment);
    }


}

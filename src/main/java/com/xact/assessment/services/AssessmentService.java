/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.OrganisationRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xact.assessment.models.AssessmentStatus.Active;
import static com.xact.assessment.models.AssessmentStatus.Completed;


@Singleton
public class AssessmentService {

    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentRepository assessmentRepository;
    private final UsersAssessmentsRepository usersAssessmentsRepository;

    private final AccessControlRepository accessControlRepository;
    private final OrganisationRepository organisationRepository;


    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository, AccessControlRepository accessControlRepository, OrganisationRepository organisationRepository) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
        this.accessControlRepository = accessControlRepository;
        this.organisationRepository=organisationRepository;

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

    public Set<AssessmentUsers> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedInUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();


        Optional<AssessmentUsers> assessmentOwner = Optional.empty();
        if (assessment.getAssessmentId() != null) {
            assessmentOwner = usersAssessmentsRepository.findOwnerByAssessmentId(assessment.getAssessmentId());
        }

        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        assessmentOwner.ifPresent(assessmentUsers::add);
        for (UserDto user : users) {
            if (!user.getEmail().isBlank()) {
                user.setRole(UserRole.Facilitator);
                if (assessmentOwner.isEmpty()) {
                    if (loggedInUser.getUserEmail().equals(user.getEmail())) {
                        user.setRole(UserRole.Owner);
                    }
                } else {
                    if (user.getEmail().equals(assessmentOwner.get().getUserId().getUserEmail())) {
                        continue;
                    }
                }
                AssessmentUsers assessmentUser = mapper.map(user, AssessmentUsers.class);
                assessmentUser.setUserId(new UserId(user.getEmail(), assessment));
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
        assessment.setUpdatedAt(new Date());
        return assessmentRepository.update(assessment);
    }

    public Assessment reopenAssessment(Assessment assessment) {
        assessment.setAssessmentStatus(Active);
        assessment.setUpdatedAt(new Date());
        return assessmentRepository.update(assessment);
    }


    @Transactional
    public void updateAssessment(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
        assessmentRepository.update(assessment);

        usersAssessmentsService.updateUsersInAssessment(assessmentUsers, assessment.getAssessmentId());
    }
    public void updateAssessment(Assessment assessment) {
        assessmentRepository.update(assessment);
    }


    public Optional<AccessControlRoles> getUserRole(String email) {
        return accessControlRepository.getAccessControlRolesByEmail(email);

    }
    public Integer getTotalAssessments(String startDate,String endDate) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        List<Assessment> assessmentList=assessmentRepository.TotalAssessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate));
        return assessmentList.size();
    }

    public Integer getTotalActiveAssessments(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        List<Assessment> assessmentList= assessmentRepository.TotalActiveAssessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate));
        return assessmentList.size();
    }

    public List<Assessment> getAdminAssessmentsData(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        List<Assessment> assessmentList= assessmentRepository.TotalAssessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate));
        return assessmentList;
    }

    public Integer getTotalCompletedAssessments(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        List<Assessment> assessmentList= assessmentRepository.TotalCompletedAssessments(simpleDateFormat.parse(startDate),simpleDateFormat.parse(endDate));
        return assessmentList.size();
    }


}

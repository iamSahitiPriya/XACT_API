/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.ModuleRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
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
    private final UserAssessmentModuleRepository userAssessmentModuleRepository;
    private final ModuleRepository moduleRepository;
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository, AccessControlRepository accessControlRepository, UserAssessmentModuleRepository userAssessmentModuleRepository, ModuleRepository moduleRepository) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
        this.accessControlRepository = accessControlRepository;
        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);

        Set<AssessmentUser> assessmentUserSet = getAssessmentUsers(assessmentRequest, user, assessment);
        createAssessment(assessment);

        usersAssessmentsService.createUsersInAssessment(assessmentUserSet);
        assessment.setAssessmentUsers(assessmentUserSet);

        return assessment;
    }


    private void createAssessment(Assessment assessment) {
        assessmentRepository.save(assessment);
    }

    public Set<AssessmentUser> getAssessmentUsers(AssessmentRequest assessmentRequest, User loggedInUser, Assessment assessment) {
        List<UserDto> users = assessmentRequest.getUsers();


        Optional<AssessmentUser> assessmentOwner = Optional.empty();
        if (assessment.getAssessmentId() != null) {
            assessmentOwner = assessment.getOwner();
        }

        Set<AssessmentUser> assessmentUsers = new HashSet<>();
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
                AssessmentUser assessmentUser = mapper.map(user, AssessmentUser.class);
                assessmentUser.setUserId(new UserId(user.getEmail(), assessment));
                assessmentUsers.add(assessmentUser);
            }
        }
        return assessmentUsers;
    }

    public Assessment getAssessment(Integer assessmentId, User user) {
        AssessmentUser assessmentUser = usersAssessmentsRepository.findByUserEmail(String.valueOf(user.getUserEmail()), assessmentId);
        return assessmentUser.getUserId().getAssessment();
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
    public void updateAssessment(Assessment assessment, Set<AssessmentUser> assessmentUsers) {
        usersAssessmentsService.updateUsersInAssessment(assessmentUsers, assessment.getAssessmentId());
        assessment.setAssessmentUsers(assessmentUsers);
        updateAssessment(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        assessment.setUpdatedAt(new Date());
        assessmentRepository.update(assessment);
    }


    public Optional<AccessControlRoles> getUserRole(String email) {
        return accessControlRepository.getAccessControlRolesByEmail(email);
    }

    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        Date startDateTime = simpleDateFormat.parse(startDate);
        Date endDateTime = simpleDateFormat.parse(endDate);
        return assessmentRepository.totalAssessments(startDateTime, endDateTime);
    }

    public List<Assessment> getAdminAssessmentsData(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        return assessmentRepository.totalAssessments(simpleDateFormat.parse(startDate), simpleDateFormat.parse(endDate));
    }

    public void saveAssessmentModules(List<ModuleRequest> moduleRequests, Assessment assessment) {
        for (ModuleRequest moduleRequest1 : moduleRequests) {
            UserAssessmentModule userAssessmentModule = new UserAssessmentModule();
            userAssessmentModule.setAssessment(assessment);
            AssessmentModule assessmentModule = getModule(moduleRequest1.getModuleId());
            AssessmentModuleId assessmentModuleId = new AssessmentModuleId(assessment, assessmentModule);
            userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
            userAssessmentModule.setModule(assessmentModule);
            userAssessmentModuleRepository.save(userAssessmentModule);
        }
    }

    public AssessmentModule getModule(Integer moduleId) {
        return moduleRepository.findByModuleId(moduleId);
    }

    public void updateAssessmentModules(List<ModuleRequest> moduleRequest, Assessment assessment) {
        userAssessmentModuleRepository.deleteByModule(assessment.getAssessmentId());
        saveAssessmentModules(moduleRequest, assessment);
    }

    public void softDeleteAssessment(Assessment assessment) {
        assessment.setDeleted(true);
        assessment.setUpdatedAt(new Date());
        updateAssessment(assessment);
    }
}

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
    private final EmailNotificationService emailNotificationService;

    private final ModuleRepository moduleRepository;
    private final AssessmentMasterDataService assessmentMasterDataService;
    private final static String datePattern = "yyyy-MM-dd";


    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository, AccessControlRepository accessControlRepository, UserAssessmentModuleRepository userAssessmentModuleRepository, EmailNotificationService emailNotificationService, ModuleRepository moduleRepository, AssessmentMasterDataService assessmentMasterDataService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
        this.accessControlRepository = accessControlRepository;

        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
        this.emailNotificationService = emailNotificationService;
        this.moduleRepository = moduleRepository;
        this.assessmentMasterDataService = assessmentMasterDataService;
    }

    @Transactional
    public Assessment createAssessment(AssessmentRequest assessmentRequest, User user) {
        Assessment assessment = mapper.map(assessmentRequest, Assessment.class);
        assessment.setAssessmentStatus(AssessmentStatus.Active);

        Organisation organisation = mapper.map(assessmentRequest, Organisation.class);
        assessment.setOrganisation(organisation);
        Set<AssessmentUsers> assessmentUsers = getAssessmentUsers(assessmentRequest, user, assessment);
        createAssessment(assessment);
        saveNotification(assessment,assessmentUsers);
        usersAssessmentsService.createUsersInAssessment(assessmentUsers);
        return assessment;
    }

    private void saveNotification(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
        for(AssessmentUsers eachUser : assessmentUsers) {
            EmailNotifier emailNotifier = new EmailNotifier();

            setNotificationPurpose(eachUser, emailNotifier);

            emailNotifier.setUserEmail(eachUser.getUserId().getUserEmail());
            emailNotifier.setStatus(NotificationStatus.N);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("assessment_id", String.valueOf(assessment.getAssessmentId()));
            payload.put("assessment_name", assessment.getAssessmentName());
            emailNotifier.setPayload(payload);

            emailNotificationService.saveNotification(emailNotifier);
        }
    }

    private void setNotificationPurpose(AssessmentUsers eachUser, EmailNotifier emailNotifier) {
        if(eachUser.getRole().equals(AssessmentRole.Owner))
            emailNotifier.setTemplateName(NotificationTemplateType.Created);
        else if(eachUser.getRole().equals(AssessmentRole.Facilitator))
            emailNotifier.setTemplateName(NotificationTemplateType.AddUser);
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
        usersAssessmentsService.updateUsersInAssessment(assessmentUsers,assessment.getAssessmentId());
        assessment.setAssessmentUsers(assessmentUsers);
        assessmentRepository.update(assessment);
    }

    public void updateAssessment(Assessment assessment) {
        assessmentRepository.update(assessment);
    }


    public Optional<AccessControlRoles> getUserRole(String email) {
        return accessControlRepository.getAccessControlRolesByEmail(email);
    }

    public List<Assessment> getTotalAssessments(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date startDateTime = simpleDateFormat.parse(startDate);
        Date endDateTime = simpleDateFormat.parse(endDate);
        return assessmentRepository.totalAssessments(startDateTime, endDateTime);
    }

    public List<Assessment> getAdminAssessmentsData(String startDate, String endDate) throws ParseException {
        DateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
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
}

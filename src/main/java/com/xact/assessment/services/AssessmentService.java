/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

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

    private final AssessmentMasterDataService assessmentMasterDataService;

    private final UserQuestionService userQuestionService;

    private final QuestionService questionService;
    private final ModuleRepository moduleRepository;
    private final ParameterService parameterService;

    private final AnswerService answerService;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;


    private final NotificationService notificationService;

    private final TopicService topicService;
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(UsersAssessmentsService usersAssessmentsService, AssessmentRepository assessmentRepository, UsersAssessmentsRepository usersAssessmentsRepository, AccessControlRepository accessControlRepository, UserAssessmentModuleRepository userAssessmentModuleRepository, AssessmentMasterDataService assessmentMasterDataService, UserQuestionService userQuestionService, QuestionService questionService, ModuleRepository moduleRepository, ParameterService parameterService, AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, NotificationService notificationService, TopicService topicService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.usersAssessmentsRepository = usersAssessmentsRepository;
        this.accessControlRepository = accessControlRepository;
        this.userAssessmentModuleRepository = userAssessmentModuleRepository;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.userQuestionService = userQuestionService;
        this.questionService = questionService;
        this.moduleRepository = moduleRepository;
        this.parameterService = parameterService;
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.notificationService = notificationService;
        this.topicService = topicService;
    }

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

    public Set<String> getNewlyAddedUser(Set<AssessmentUser> existingUsers, Set<AssessmentUser> newUsers) {
        Set<AssessmentUser> newUsersSet = new HashSet<>(newUsers);
        return getUpdatedUsers(existingUsers, newUsersSet);
    }

    public Set<String> getDeletedUser(Set<AssessmentUser> existingUsers, Set<AssessmentUser> deletedUsers) {
        Set<AssessmentUser> newUserSet = new HashSet<>(deletedUsers);
        return getUpdatedUsers(newUserSet, existingUsers);
    }


    public Set<AssessmentUser> getAssessmentFacilitatorsSet(Assessment assessment) {
        List<AssessmentUser> assessmentFacilitators = usersAssessmentsRepository.findUserByAssessmentId(assessment.getAssessmentId(), AssessmentRole.Facilitator);
        return new HashSet<>(assessmentFacilitators);
    }

    private Set<String> getUpdatedUsers(Set<AssessmentUser> assessmentUsers, Set<AssessmentUser> assessmentUsersSet) {
        assessmentUsersSet.removeAll(assessmentUsers);
        Set<String> users = new HashSet<>();
        for (AssessmentUser user : assessmentUsersSet) {
            if (user.getRole() == AssessmentRole.Facilitator) {
                users.add(user.getUserId().getUserEmail());
            }
        }
        return users;
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

    public Assessment getAssessmentById(Integer assessmentId) {
        return assessmentRepository.findByAssessmentId(assessmentId);
    }

    public List<Assessment> findAssessments(String userEmail) {
        return usersAssessmentsService.findAssessments(userEmail);
    }

    public List<Answer> getAnswers(Integer assessmentId) {
        return answerService.getAnswers(assessmentId);
    }

    public void saveAnswer(UpdateAnswerRequest answerRequest, Assessment assessment) {
        answerService.saveAnswer(answerRequest, assessment);
    }

    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return parameterService.getParameter(parameterId);
    }

    public List<TopicLevelAssessment> getTopicAssessmentData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
    }

    public List<TopicLevelRecommendation> getAssessmentTopicRecommendationData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId);
    }

    public List<ParameterLevelAssessment> getParameterAssessmentData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);

    }

    public List<ParameterLevelRecommendation> getAssessmentParameterRecommendationData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId);
    }

    public Optional<ParameterLevelRecommendation> searchParameterRecommendation(Integer recommendationId) {
        return topicAndParameterLevelAssessmentService.searchParameterRecommendation(recommendationId);

    }

    public void saveParameterLevelRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);

    }

    public boolean checkParameterRecommendationId(Integer recommendationId) {
        return topicAndParameterLevelAssessmentService.checkParameterRecommendationId(recommendationId);
    }

    public Optional<TopicLevelRecommendation> searchTopicRecommendation(Integer recommendationId) {
        return topicAndParameterLevelAssessmentService.searchTopicRecommendation(recommendationId);

    }

    public void saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);
    }

    public boolean checkTopicRecommendationId(Integer recommendationId) {
        return topicAndParameterLevelAssessmentService.checkTopicRecommendationId(recommendationId);

    }

    public void deleteRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteRecommendation(recommendationId);
    }

    public void deleteParameterRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteParameterRecommendation(recommendationId);
    }

    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicAndParameterLevelAssessmentService.searchTopic(topicLevelId);
    }

    public void saveRatingAndRecommendation(TopicLevelAssessment topicLevelAssessment) {
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);
    }

    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId);

    }

    public void saveRatingAndRecommendation(ParameterLevelAssessment parameterLevelAssessment) {
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);
    }


    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return questionService.getTopicByQuestionId(questionId);
    }

    public List<AssessmentCategory> getAllCategories() {
        return assessmentMasterDataService.getAllCategories();
    }

    public List<AssessmentCategory> getUserAssessmentCategories(Integer assessmentId) {
        return assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
    }

    public Notification setNotificationForCreateAssessment(Assessment assessment) {
        return notificationService.setNotificationForCreateAssessment(assessment);
    }

    public Notification setNotificationForAddUser(Assessment assessment, Set<String> newlyAddedUsers) {
        return notificationService.setNotificationForAddUser(assessment, newlyAddedUsers);
    }

    public Notification setNotificationForDeleteUser(Assessment assessment, Set<String> deletedUsers) {
        return notificationService.setNotificationForDeleteUser(assessment, deletedUsers);
    }

    public Notification setNotificationForReopenAssessment(Assessment assessment) {
        return notificationService.setNotificationForReopenAssessment(assessment);
    }

    public Notification setNotificationForCompleteAssessment(Assessment assessment) {
        return notificationService.setNotificationForCompleteAssessment(assessment);
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

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return topicService.getTopic(topicId);
    }
}

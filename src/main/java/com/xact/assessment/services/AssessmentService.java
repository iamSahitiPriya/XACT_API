/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentRepository;
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
    private final AccessControlService accessControlService;

    private final AssessmentMasterDataService assessmentMasterDataService;

    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    ModelMapper mapper = new ModelMapper();

    public AssessmentService(AssessmentRepository assessmentRepository, UsersAssessmentsService usersAssessmentsService, AccessControlService accessControlService, AssessmentMasterDataService assessmentMasterDataService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.assessmentRepository = assessmentRepository;
        this.accessControlService = accessControlService;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
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
        return usersAssessmentsService.getAssessmentFacilitatorsSet(assessment);
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
        return usersAssessmentsService.getAssessment(assessmentId, user);
    }

    public List<String> getAssessmentFacilitators(Integer assessmentId) {
        return usersAssessmentsService.getAssessmentFacilitators(assessmentId);
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
        return accessControlService.getAccessControlRolesByEmail(email);
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
        usersAssessmentsService.saveAssessmentModules(moduleRequests, assessment);
    }

    public void updateAssessmentModules(List<ModuleRequest> moduleRequest, Assessment assessment) {
        usersAssessmentsService.updateAssessmentModules(moduleRequest, assessment);
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
        return topicAndParameterLevelAssessmentService.getAnswers(assessmentId);
    }

    public void saveAnswer(UpdateAnswerRequest answerRequest, Assessment assessment) {
        topicAndParameterLevelAssessmentService.saveAnswer(answerRequest, assessment);
    }

    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return topicAndParameterLevelAssessmentService.getParameter(parameterId);
    }

    public List<TopicLevelRating> getTopicAssessmentData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
    }

    public List<TopicLevelRecommendation> getAssessmentTopicRecommendationData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId);
    }

    public List<ParameterLevelRating> getParameterAssessmentData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);

    }

    public List<ParameterLevelRecommendation> getAssessmentParameterRecommendationData(Integer assessmentId) {
        return topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId);
    }


    public void deleteRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteRecommendation(recommendationId);
    }

    public void deleteParameterRecommendation(Integer recommendationId) {
        topicAndParameterLevelAssessmentService.deleteParameterRecommendation(recommendationId);
    }

    public Optional<TopicLevelRating> searchTopic(TopicLevelId topicLevelId) {
        return topicAndParameterLevelAssessmentService.searchTopic(topicLevelId);
    }

    public void saveRatingAndRecommendation(TopicLevelRating topicLevelRating) {
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelRating);
    }

    public Optional<ParameterLevelRating> searchParameter(ParameterLevelId parameterLevelId) {
        return topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId);

    }

    public void saveRatingAndRecommendation(ParameterLevelRating parameterLevelRating) {
        topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelRating);
    }


    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return topicAndParameterLevelAssessmentService.getTopicByQuestionId(questionId);
    }

    public List<AssessmentCategory> getAllCategories() {
        return assessmentMasterDataService.getAllCategoriesByDesc();
    }

    public List<AssessmentCategory> getUserAssessmentCategories(Integer assessmentId) {
        return assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
    }

    public List<UserQuestion> findAllUserQuestion(Integer assessmentId) {
        return usersAssessmentsService.findAllUserQuestion(assessmentId);
    }

    public UserQuestion saveUserQuestion(Assessment assessment, Integer parameterId, String userQuestion) {
        return usersAssessmentsService.saveUserQuestion(assessment, parameterId, userQuestion);
    }

    public void saveUserAnswer(Integer questionId, String answer) {
        usersAssessmentsService.saveUserAnswer(questionId, answer);
    }

    public void updateUserQuestion(Integer questionId, String updatedQuestion) {
        usersAssessmentsService.updateUserQuestion(questionId, updatedQuestion);
    }

    public Optional<UserQuestion> searchUserQuestion(Integer questionId) {
        return usersAssessmentsService.searchUserQuestion(questionId);
    }

    public void deleteUserQuestion(Integer questionId) {
        usersAssessmentsService.deleteUserQuestion(questionId);
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return topicAndParameterLevelAssessmentService.getTopic(topicId);
    }

    public TopicLevelRecommendation updateTopicRecommendation(RecommendationRequest recommendationRequest) {
        return topicAndParameterLevelAssessmentService.updateTopicRecommendation(recommendationRequest);
    }

    public TopicLevelRecommendation saveTopicRecommendation(RecommendationRequest recommendationRequest, Assessment assessment, Integer topicId) {
        return topicAndParameterLevelAssessmentService.saveTopicRecommendation(recommendationRequest, assessment, topicId);
    }

    public ParameterLevelRecommendation updateParameterLevelRecommendation(RecommendationRequest parameterLevelRecommendationRequest) {
        return topicAndParameterLevelAssessmentService.updateParameterLevelRecommendation(parameterLevelRecommendationRequest);
    }

    public ParameterLevelRecommendation saveParameterLevelRecommendation(RecommendationRequest parameterLevelRecommendationRequest, Assessment assessment, Integer parameterId) {
        return topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendationRequest, assessment, parameterId);
    }

    public List<Assessment> findInactiveAssessments(Integer duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -duration);
        Date expiryDate = calendar.getTime();
        return assessmentRepository.findInactiveAssessments(expiryDate);

    }

}

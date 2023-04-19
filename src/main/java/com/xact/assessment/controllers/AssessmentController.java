/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.mappers.AssessmentMapper;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.*;
import com.xact.assessment.services.ActivityLogService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;


@Controller("/v1/assessments")
public class AssessmentController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentController.class);

    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;
    private final ActivityLogService activityLogService;
    private final NotificationService notificationService;


    private final AssessmentMapper assessmentMapper = new AssessmentMapper();
    private static final ModelMapper modelMapper = new ModelMapper();


    @Value("${validation.email:^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$}")
    private String emailPattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";


    public AssessmentController(UserAuthService userAuthService, AssessmentService assessmentService, ActivityLogService activityLogService, NotificationService notificationService) {
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.activityLogService = activityLogService;
        this.notificationService = notificationService;
    }


    @Get(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<List<AssessmentResponse>> getAssessments(Authentication authentication) {
        LOGGER.info("Get all assessments");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        List<AssessmentResponse> assessmentResponses = assessmentService.getAssessments(loggedInUser.getUserEmail());
        return HttpResponse.ok(assessmentResponses);
    }

    @Post(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> createAssessment(@Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        LOGGER.info("Create new assessment");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        assessmentRequest.validate(emailPattern);

        Assessment assessment = assessmentService.createAssessment(assessmentRequest, loggedInUser);
        CompletableFuture.supplyAsync(() -> notificationService.setNotificationForCreateAssessment(assessment));

        return HttpResponse.ok();
    }

    @Put(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> updateAssessment(@PathVariable("assessmentId") Integer assessmentId, @Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        LOGGER.info("Update assessment : {}", assessmentId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        assessmentRequest.validate(emailPattern);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
           assessment = assessmentService.setAssessment(assessment,assessmentRequest);
            Set<AssessmentUser> newUsers = assessmentService.getAssessmentUsers(assessmentRequest, loggedInUser, assessment);
            Set<AssessmentUser> existingUser = assessmentService.getAssessmentFacilitators(assessment);

            Set<String> newlyAddedUsers = assessmentService.getNewlyAddedUser(existingUser, newUsers);
            Set<String> deletedUsers = assessmentService.getDeletedUser(existingUser, newUsers);

            Assessment finalAssessment = assessment;
            CompletableFuture.supplyAsync(() -> notificationService.setNotificationForAddUser(finalAssessment, newlyAddedUsers));
            CompletableFuture.supplyAsync(() -> notificationService.setNotificationForDeleteUser(finalAssessment, deletedUsers));

            assessmentService.updateAssessmentAndUsers(assessment, newUsers);
        }
        return HttpResponse.ok();
    }


    @Put(value = "/{assessmentId}/open", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<AssessmentResponse> reopenAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {

        LOGGER.info("Reopen assessment : {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        Assessment openedAssessment = assessmentService.reopenAssessment(assessment);
        AssessmentResponse assessmentResponse = assessmentMapper.map(openedAssessment);

        //CompletableFuture.supplyAsync(() -> notificationService.setNotificationForReopenAssessment(assessment));

        return HttpResponse.ok(assessmentResponse);
    }


    @Put(value = "/{assessmentId}/finish", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<AssessmentResponse> finishAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        LOGGER.info("Finish assessment : {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        Assessment finishedAssessment = assessmentService.finishAssessment(assessment);
        AssessmentResponse assessmentResponse = assessmentMapper.map(finishedAssessment);

        CompletableFuture.supplyAsync(() -> notificationService.setNotificationForCompleteAssessment(assessment));

        return HttpResponse.ok(assessmentResponse);
    }


    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<AssessmentResponse> getAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        LOGGER.info("Get assessment : {}", assessmentId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        AssessmentResponse assessmentResponse = assessmentService.getAssessmentResponse(assessment);
        assessmentResponse.setOwner(loggedInUser.getUserEmail().equals(assessment.getOwnerEmail()));

        return HttpResponse.ok(assessmentResponse);
    }

    @Patch(value = "/{assessmentId}/parameters/{parameterId}/recommendations", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<RecommendationResponse> saveParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body RecommendationRequest parameterLevelRecommendationRequest, Authentication authentication) {
        LOGGER.info("Update individual parameter maturity recommendation. assessment: {}, parameter: {}", assessmentId, parameterId);
        User user = userAuthService.getCurrentUser(authentication);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        RecommendationResponse parameterLevelRecommendationResponse = new RecommendationResponse();
        if (assessment.isEditable()) {
            ParameterLevelRecommendation parameterLevelRecommendation;
            if (parameterLevelRecommendationRequest.getRecommendationId() != null) {
                parameterLevelRecommendation = assessmentService.updateParameterRecommendation(parameterLevelRecommendationRequest);
            } else {
                parameterLevelRecommendation = assessmentService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, parameterId);
            }
            parameterLevelRecommendationResponse = getParameterLevelRecommendationResponse(user, assessment, parameterLevelRecommendation);
            updateAssessment(assessment);
        }
        return HttpResponse.ok(parameterLevelRecommendationResponse);
    }


    @Patch(value = "/{assessmentId}/topics/{topicId}/recommendations", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<RecommendationResponse> saveTopicRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body RecommendationRequest recommendationRequest, Authentication authentication) {
        LOGGER.info("Update individual topic recommendation. assessment: {}, topic: {}", assessmentId, topicId);
        User user = userAuthService.getCurrentUser(authentication);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        RecommendationResponse topicLevelRecommendationResponse = new RecommendationResponse();
        if (assessment.isEditable()) {
            TopicLevelRecommendation topicLevelRecommendation;
            if (recommendationRequest.getRecommendationId() != null) {
                topicLevelRecommendation = assessmentService.updateTopicRecommendation(recommendationRequest);
            } else {
                topicLevelRecommendation = assessmentService.saveTopicRecommendation(recommendationRequest, assessment, topicId);
            }
            topicLevelRecommendationResponse = getTopicLevelRecommendationResponse(user, assessment, topicLevelRecommendation);
            updateAssessment(assessment);
        }
        return HttpResponse.ok(topicLevelRecommendationResponse);
    }

    @Delete(value = "/{assessmentId}/topics/{topicId}/recommendations/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<RecommendationRequest> deleteTopicRecommendation
            (@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer
                    topicId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
        LOGGER.info("Delete recommendation. assessment: {}, topic: {}", assessmentId, topicId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.deleteTopicRecommendation(recommendationId);
        }
        return HttpResponse.ok();
    }

    @Delete(value = "/{assessmentId}/parameters/{parameterId}/recommendations/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<RecommendationResponse> deleteParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Delete recommendation. assessment: {}, parameter: {}", assessmentId, parameterId);
        if (assessment.isEditable()) {
            assessmentService.deleteParameterRecommendation(recommendationId);
        }
        return HttpResponse.ok();
    }


    @Patch(value = "/{assessmentId}/topics/{topicId}/ratings", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelRating> saveTopicRating(@PathVariable("assessmentId") Integer
                                                                  assessmentId, @PathVariable("topicId") Integer topicId, @Body @Nullable String rating, Authentication
                                                                  authentication) {
        LOGGER.info("Update individual parameter maturity rating. assessment: {}, parameter: {}", assessmentId, topicId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.saveTopicRating(topicId,assessment,rating);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/{assessmentId}/parameters/{parameterId}/ratings")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterLevelRating> saveParameterRating(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body @Nullable String rating, Authentication authentication) {
        LOGGER.info("Update individual parameter maturity rating. assessment: {}, parameter: {}", assessmentId, parameterId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.saveParameterRating(parameterId,assessment,rating);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/{assessmentId}/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Assessment> saveModules(@PathVariable("assessmentId") Integer assessmentId, @Body List<ModuleRequest> moduleRequests, Authentication authentication) {
        LOGGER.info("Save modules: {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.saveAssessmentModules(moduleRequests, assessment);
        }
        return HttpResponse.ok();
    }

    @Put(value = "/{assessmentId}/modules")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Assessment> updateModules(@PathVariable("assessmentId") Integer assessmentId, @Body List<ModuleRequest> moduleRequest, Authentication authentication) {
        LOGGER.info("Update modules. assessment: {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.updateAssessmentModules(moduleRequest, assessment);
        }
        return HttpResponse.ok();
    }

    @Delete(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void deleteAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        LOGGER.info("Delete assessment : {}", assessmentId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable() && (assessment.getOwnerEmail().equals(loggedInUser.getUserEmail()))) {
            assessmentService.softDeleteAssessment(assessment);
            CompletableFuture.supplyAsync(() -> notificationService.setNotificationForDeleteAssessment(assessment));
        }
    }

    @Transactional
    @Get(value = "{assessmentId}/categories/all", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserAssessmentResponse> getCategories(@PathVariable("assessmentId") Integer assessmentId) {
        LOGGER.info("Get all category & assessment master data");
        List<AssessmentCategoryDto> assessmentCategoriesResponse = assessmentService.getAllCategories();
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse =  assessmentService.getUserAssessmentCategories(assessmentId);

        UserAssessmentResponse userAssessmentResponse = new UserAssessmentResponse();
        userAssessmentCategoriesResponse.sort(null);
        userAssessmentResponse.setAssessmentCategories(assessmentCategoriesResponse);
        userAssessmentCategoriesResponse.sort(null);
        userAssessmentResponse.setUserAssessmentCategories(userAssessmentCategoriesResponse);
        return HttpResponse.ok(userAssessmentResponse);
    }

    @Get(value = "{assessmentId}/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<UserAssessmentResponse> getSelectedCategories(@PathVariable("assessmentId") Integer assessmentId) {
        LOGGER.info("Get selected categories only");
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse  = assessmentService.getUserAssessmentCategories(assessmentId);
        UserAssessmentResponse userAssessmentResponse = new UserAssessmentResponse();
        userAssessmentCategoriesResponse.sort(null);
        userAssessmentResponse.setUserAssessmentCategories(userAssessmentCategoriesResponse);
        return HttpResponse.ok(userAssessmentResponse);
    }

    @Post(value = "/{assessmentId}/{parameterId}/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<UserQuestionResponse> saveUserQuestion(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body String userQuestion, Authentication authentication) {
        LOGGER.info("Save individual user added questions. assessment: {}, parameter:{}", assessmentId, parameterId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        UserQuestionResponse userQuestionResponse = new UserQuestionResponse();
        if (assessment.isEditable()) {
            userQuestionResponse = assessmentService.saveUserQuestion(assessment, parameterId, userQuestion);
            updateAssessment(assessment);
        }
        return HttpResponse.ok(userQuestionResponse);
    }

    @Patch(value = "/{assessmentId}/answers/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Assessment> updateAnswer(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, @Body @Nullable UpdateAnswerRequest answerRequest, Authentication authentication) {
        LOGGER.info("Update individual user added answer. assessment: {}, question:{}", assessmentId, questionId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        User user = userAuthService.getCurrentUser(authentication);
        AssessmentTopic assessmentTopic = null;
        if (assessment.isEditable() && answerRequest != null) {
            if (answerRequest.getType() == AnswerType.DEFAULT) {
                assessmentService.saveAnswer(answerRequest, assessment);
                assessmentTopic = assessmentService.getTopicByQuestionId(questionId);

            } else {
                assessmentService.saveUserAnswer(questionId, answerRequest.getAnswer());
                assessmentTopic = assessmentService.getTopicByQuestionId(questionId);
            }
            final AssessmentTopic finalAssessmentTopic = assessmentTopic;
            CompletableFuture.supplyAsync(() -> activityLogService.saveActivityLog(assessment, user, questionId, finalAssessmentTopic, ActivityType.valueOf(answerRequest.getType() + "_QUESTION")));
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/{assessmentId}/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserQuestion> updateUserQuestion(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, @Body String updatedQuestion, Authentication authentication) {
        LOGGER.info("Update individual user added questions. assessment: {}, parameter:{}", assessmentId, questionId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.updateUserQuestion(questionId, updatedQuestion);
        }
        return HttpResponse.ok();
    }

    @Delete(value = "/{assessmentId}/questions/{questionId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserQuestion> deleteUserQuestion(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Delete user question. assessment: {}", assessmentId);
        if (assessment.isEditable() && assessmentService.searchUserQuestion(questionId).isPresent()) {
            assessmentService.deleteUserQuestion(questionId);
        }
        return HttpResponse.ok();
    }


    private Assessment getAuthenticatedAssessment(Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        return assessmentService.getAssessment(assessmentId, loggedInUser);
    }



    private void updateAssessment(Assessment assessment) {
        LOGGER.info("Update assessment timestamp. assessment: {}", assessment.getAssessmentId());
        assessmentService.updateAssessment(assessment);
    }

    private RecommendationResponse getTopicLevelRecommendationResponse(User user, Assessment assessment, TopicLevelRecommendation topicLevelRecommendation) {
        RecommendationResponse topicLevelRecommendationResponse = modelMapper.map(topicLevelRecommendation, RecommendationResponse.class);
        TopicLevelRecommendation finalTopicLevelRecommendation = topicLevelRecommendation;
        CompletableFuture.supplyAsync(() -> activityLogService.saveActivityLog(assessment, user, finalTopicLevelRecommendation.getRecommendationId(), finalTopicLevelRecommendation.getTopic(), ActivityType.TOPIC_RECOMMENDATION));
        return topicLevelRecommendationResponse;
    }

    private RecommendationResponse getParameterLevelRecommendationResponse(User user, Assessment assessment, ParameterLevelRecommendation parameterLevelRecommendation) {
        RecommendationResponse parameterLevelRecommendationResponse = modelMapper.map(parameterLevelRecommendation, RecommendationResponse.class);
        ParameterLevelRecommendation finalParameterLevelRecommendation = parameterLevelRecommendation;
        CompletableFuture.supplyAsync(() -> activityLogService.saveActivityLog(assessment, user, finalParameterLevelRecommendation.getRecommendationId(), finalParameterLevelRecommendation.getParameter().getTopic(), ActivityType.PARAMETER_RECOMMENDATION));
        return parameterLevelRecommendationResponse;
    }

}

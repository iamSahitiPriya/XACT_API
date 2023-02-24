/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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
    private final MasterDataMapper masterDataMapper = new MasterDataMapper();


    @Value("${validation.email:^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$}")
    private String emailPattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

    @Inject


    private static final ModelMapper modelMapper = new ModelMapper();


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
        List<Assessment> assessments = assessmentService.findAssessments(loggedInUser.getUserEmail());
        List<AssessmentResponse> assessmentResponses = new ArrayList<>();
        if (Objects.nonNull(assessments))
            assessments.forEach(assessment ->
            {
                AssessmentResponse assessmentResponse = assessmentMapper.map(assessment);
                assessmentResponse.setAssessmentDescription(assessment.getAssessmentDescription());
                assessmentResponse.setOwner(loggedInUser.getUserEmail().equals(assessment.getOwnerEmail()));
                assessmentResponses.add(assessmentResponse);
            });
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
            assessment.setAssessmentName(assessmentRequest.getAssessmentName());
            assessment.getOrganisation().setOrganisationName(assessmentRequest.getOrganisationName());
            assessment.getOrganisation().setDomain(assessmentRequest.getDomain());
            assessment.getOrganisation().setIndustry(assessmentRequest.getIndustry());
            assessment.getOrganisation().setSize(assessmentRequest.getTeamSize());
            assessment.setAssessmentPurpose(assessmentRequest.getAssessmentPurpose());
            assessment.setAssessmentDescription(assessmentRequest.getAssessmentDescription());
            Set<AssessmentUser> newUsers = assessmentService.getAssessmentUsers(assessmentRequest, loggedInUser, assessment);
            Set<AssessmentUser> existingUser = assessmentService.getAssessmentFacilitatorsSet(assessment);

            Set<String> newlyAddedUsers = assessmentService.getNewlyAddedUser(existingUser, newUsers);
            Set<String> deletedUsers = assessmentService.getDeletedUser(existingUser, newUsers);

            CompletableFuture.supplyAsync(() -> notificationService.setNotificationForAddUser(assessment, newlyAddedUsers));
            CompletableFuture.supplyAsync(() -> notificationService.setNotificationForDeleteUser(assessment, deletedUsers));


            assessmentService.updateAssessment(assessment, newUsers);
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

        CompletableFuture.supplyAsync(() -> notificationService.setNotificationForReopenAssessment(assessment));

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

        List<Answer> answerResponse = assessmentService.getAnswers(assessment.getAssessmentId());

        List<UserQuestion> userQuestionList = assessmentService.findAllUserQuestion(assessment.getAssessmentId());

        List<TopicLevelRating> topicLevelRatingList = assessmentService.getTopicAssessmentData(assessment.getAssessmentId());
        List<TopicLevelRecommendation> topicLevelRecommendationList = assessmentService.getAssessmentTopicRecommendationData(assessment.getAssessmentId());
        List<TopicRatingAndRecommendation> topicRecommendationResponses = mergeTopicRatingAndRecommendation(topicLevelRatingList, topicLevelRecommendationList);
        List<ParameterLevelRating> parameterLevelRatingList = assessmentService.getParameterAssessmentData(assessment.getAssessmentId());

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = assessmentService.getAssessmentParameterRecommendationData(assessment.getAssessmentId());
        List<ParameterRatingAndRecommendation> paramRecommendationResponses = mergeParamRatingAndRecommendation(parameterLevelRatingList, parameterLevelRecommendationList);


        AssessmentResponse assessmentResponse = assessmentMapper.map(assessment, answerResponse, userQuestionList, topicRecommendationResponses, paramRecommendationResponses);
        assessmentResponse.setOwner(loggedInUser.getUserEmail().equals(assessment.getOwnerEmail()));

        return HttpResponse.ok(assessmentResponse);
    }

    @Patch(value = "/{assessmentId}/parameters/{parameterId}/recommendations", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ParameterLevelRecommendationResponse> saveParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Update individual parameter maturity recommendation. assessment: {}, parameter: {}", assessmentId, parameterId);
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        ParameterLevelRecommendationResponse parameterLevelRecommendationResponse = new ParameterLevelRecommendationResponse();
        AssessmentParameter assessmentParameter = assessmentService.getParameter(parameterId).orElseThrow();
        parameterLevelRecommendation.setAssessment(assessment);
        User user = userAuthService.getCurrentUser(authentication);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        if (assessment.isEditable()) {
            if (parameterLevelRecommendationRequest.getRecommendationId() != null) {
                parameterLevelRecommendation = assessmentService.searchParameterRecommendation(parameterLevelRecommendationRequest.getRecommendationId()).orElse(new ParameterLevelRecommendation());
                parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
            }
            parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
            parameterLevelRecommendation.setRecommendationImpact(parameterLevelRecommendationRequest.getImpact());
            parameterLevelRecommendation.setRecommendationEffort(parameterLevelRecommendationRequest.getEffort());
            parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());
            assessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);
            updateAssessment(assessment);
            if (assessmentService.checkParameterRecommendationId(parameterLevelRecommendation.getRecommendationId())) {
                parameterLevelRecommendationResponse.setRecommendationId(parameterLevelRecommendation.getRecommendationId());
                parameterLevelRecommendationResponse.setRecommendation(parameterLevelRecommendation.getRecommendation());
                ParameterLevelRecommendation finalParameterLevelRecommendation = parameterLevelRecommendation;
                CompletableFuture.supplyAsync(() -> activityLogService.saveActivityLog(assessment, user, finalParameterLevelRecommendation.getRecommendationId(), finalParameterLevelRecommendation.getParameter().getTopic(), ActivityType.PARAMETER_RECOMMENDATION));

            } else {
                parameterLevelRecommendationResponse.setRecommendationId(null);
            }
        }
        return HttpResponse.ok(parameterLevelRecommendationResponse);
    }


    @Patch(value = "/{assessmentId}/topics/{topicId}/recommendations", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<TopicLevelRecommendationResponse> saveTopicRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body TopicLevelRecommendationRequest topicLevelRecommendationRequest, Authentication authentication) {
        LOGGER.info("Update individual topic recommendation. assessment: {}, topic: {}", assessmentId, topicId);
        User user = userAuthService.getCurrentUser(authentication);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        TopicLevelRecommendationResponse topicLevelRecommendationResponse = new TopicLevelRecommendationResponse();
        if (assessment.isEditable()) {
            if (topicLevelRecommendationRequest.getRecommendationId() != null) {
                TopicLevelRecommendation topicLevelRecommendation = assessmentService.updateTopicRecommendation(topicLevelRecommendationRequest);
                topicLevelRecommendationResponse = getTopicLevelRecommendationResponse(user, assessment, topicLevelRecommendation);
            }
            else {
                TopicLevelRecommendation topicLevelRecommendation = assessmentService.saveTopicRecommendation(topicLevelRecommendationRequest,assessment,topicId);
                topicLevelRecommendationResponse = getTopicLevelRecommendationResponse(user, assessment, topicLevelRecommendation);
            }
            updateAssessment(assessment);
        }
        return HttpResponse.ok(topicLevelRecommendationResponse);
    }

    @Delete(value = "/{assessmentId}/topics/{topicId}/recommendations/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelRecommendationRequest> deleteRecommendation
            (@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer
                    topicId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
        LOGGER.info("Delete recommendation. assessment: {}, topic: {}", assessmentId, topicId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessmentService.deleteRecommendation(recommendationId);
        }
        return HttpResponse.ok();
    }

    @Delete(value = "/{assessmentId}/parameters/{parameterId}/recommendations/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterLevelRecommendationRequest> deleteParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
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
            AssessmentTopic assessmentTopic = assessmentService.getTopic(topicId).orElseThrow();
            TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
            TopicLevelRating topicLevelRating = assessmentService.searchTopic(topicLevelId).orElse(new TopicLevelRating());
            topicLevelRating.setTopicLevelId(topicLevelId);
            Integer topicRating = rating != null ? Integer.valueOf(rating) : null;
            topicLevelRating.setRating(topicRating);
            assessmentService.saveRatingAndRecommendation(topicLevelRating);
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
            AssessmentParameter assessmentParameter = assessmentService.getParameter(parameterId).orElseThrow();
            ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
            ParameterLevelRating parameterLevelRating = assessmentService.searchParameter(parameterLevelId).orElse(new ParameterLevelRating());
            parameterLevelRating.setParameterLevelId(parameterLevelId);
            Integer parameterRating = rating != null ? Integer.valueOf(rating) : null;
            parameterLevelRating.setRating(parameterRating);
            assessmentService.saveRatingAndRecommendation(parameterLevelRating);
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
        List<AssessmentCategory> assessmentCategories = assessmentService.getAllCategories();
        List<AssessmentCategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(masterDataMapper.mapTillModuleOnly(assessmentCategory)));
        }

        List<AssessmentCategory> userAssessmentCategories = assessmentService.getUserAssessmentCategories(assessmentId);
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(userAssessmentCategories)) {
            userAssessmentCategories.forEach(assessmentCategory -> userAssessmentCategoriesResponse.add(masterDataMapper.mapTillModuleOnly(assessmentCategory)));

        }
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
        List<AssessmentCategory> userAssessmentCategories = assessmentService.getUserAssessmentCategories(assessmentId);
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(userAssessmentCategories)) {
            userAssessmentCategories.forEach(assessmentCategory -> userAssessmentCategoriesResponse.add(masterDataMapper.mapAssessmentCategory(assessmentCategory)));
        }
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
        UserQuestion savedQuestion = new UserQuestion();
        if (assessment.isEditable()) {
            savedQuestion = assessmentService.saveUserQuestion(assessment, parameterId, userQuestion);
            updateAssessment(assessment);
        }
        UserQuestionResponse userQuestionResponse = modelMapper.map(savedQuestion, UserQuestionResponse.class);
        return HttpResponse.ok(userQuestionResponse);
    }

    @Patch(value = "/{assessmentId}/answers/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse updateAnswer(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, @Body @Nullable UpdateAnswerRequest answerRequest, Authentication authentication) {
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


    private List<ParameterRatingAndRecommendation> mergeParamRatingAndRecommendation(List<ParameterLevelRating> parameterLevelRatingList, List<ParameterLevelRecommendation> parameterLevelRecommendationList) {
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedParams = new HashSet<>();

        for (ParameterLevelRating paramLevelAssessment : parameterLevelRatingList) {
            Integer parameterId = paramLevelAssessment.getParameterLevelId().getParameter().getParameterId();
            processedParams.add(parameterId);
            ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
            parameterRatingAndRecommendation.setParameterId(parameterId);
            parameterRatingAndRecommendation.setRating(paramLevelAssessment.getRating());
            parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(getParameterRecommendation(parameterLevelRecommendationList, parameterId));
            parameterRatingAndRecommendationsResponse.add(parameterRatingAndRecommendation);
        }

        Set<Integer> parameterIds = parameterLevelRecommendationList.stream()
                .map(paramLevelRecommendation -> paramLevelRecommendation.getParameter().getParameterId())
                .collect(toSet());

        for (Integer paramId : parameterIds) {
            if (!processedParams.contains(paramId)) {
                processedParams.add(paramId);
                ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();

                parameterRatingAndRecommendation.setParameterId(paramId);
                parameterRatingAndRecommendation.setParameterLevelRecommendationRequest(getParameterRecommendation(parameterLevelRecommendationList, paramId));
                parameterRatingAndRecommendationsResponse.add(parameterRatingAndRecommendation);
            }
        }
        return parameterRatingAndRecommendationsResponse;
    }

    private List<ParameterLevelRecommendationRequest> getParameterRecommendation(List<ParameterLevelRecommendation> parameterLevelRecommendationList, Integer parameterId) {
        List<ParameterLevelRecommendationRequest> parameterLevelRecommendationRequests = new ArrayList<>();
        List<ParameterLevelRecommendation> matchingList = parameterLevelRecommendationList.stream().filter(parameterLevelRecommendation -> parameterId.equals(parameterLevelRecommendation.getParameter().getParameterId())).toList();
        for (ParameterLevelRecommendation parameterLevelRecommendation : matchingList) {
            ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = modelMapper.map(parameterLevelRecommendation, ParameterLevelRecommendationRequest.class);
            parameterLevelRecommendationRequests.add(parameterLevelRecommendationRequest);
        }
        return parameterLevelRecommendationRequests;
    }

    private List<TopicRatingAndRecommendation> mergeTopicRatingAndRecommendation(List<TopicLevelRating> topicLevelRatingList, List<TopicLevelRecommendation> topicLevelRecommendationList) {
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedTopics = new HashSet<>();

        for (TopicLevelRating topicLevelRating : topicLevelRatingList) {
            Integer topicId = topicLevelRating.getTopicLevelId().getTopic().getTopicId();
            processedTopics.add(topicId);
            TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
            topicRatingAndRecommendation.setTopicId(topicId);
            topicRatingAndRecommendation.setRating(topicLevelRating.getRating());
            topicRatingAndRecommendation.setTopicLevelRecommendationRequest(getTopicRecommendation(topicLevelRecommendationList, topicId));
            topicRatingAndRecommendationsResponse.add(topicRatingAndRecommendation);
        }

        Set<Integer> topicIds = topicLevelRecommendationList.stream()
                .map(topicLevelRecommendation -> topicLevelRecommendation.getTopic().getTopicId())
                .collect(toSet());

        for (Integer topicId : topicIds) {
            if (!processedTopics.contains(topicId)) {
                processedTopics.add(topicId);
                TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
                topicRatingAndRecommendation.setTopicId(topicId);
                topicRatingAndRecommendation.setTopicLevelRecommendationRequest(getTopicRecommendation(topicLevelRecommendationList, topicId));
                topicRatingAndRecommendationsResponse.add(topicRatingAndRecommendation);
            }
        }
        return topicRatingAndRecommendationsResponse;
    }

    private List<TopicLevelRecommendationRequest> getTopicRecommendation(List<TopicLevelRecommendation> topicLevelRecommendationList, Integer topicId) {
        List<TopicLevelRecommendationRequest> topicLevelRecommendationRequests = new ArrayList<>();
        List<TopicLevelRecommendation> matchingList = topicLevelRecommendationList.stream().filter(topicLevelRecommendation -> topicId.equals(topicLevelRecommendation.getTopic().getTopicId())).toList();
        for (TopicLevelRecommendation topicLevelRecommendation : matchingList) {
            TopicLevelRecommendationRequest topicLevelRecommendationRequest = modelMapper.map(topicLevelRecommendation, TopicLevelRecommendationRequest.class);
            topicLevelRecommendationRequests.add(topicLevelRecommendationRequest);
        }
        return topicLevelRecommendationRequests;
    }

    private void updateAssessment(Assessment assessment) {
        LOGGER.info("Update assessment timestamp. assessment: {}", assessment.getAssessmentId());
        assessmentService.updateAssessment(assessment);
    }

    private TopicLevelRecommendationResponse getTopicLevelRecommendationResponse(User user, Assessment assessment, TopicLevelRecommendation topicLevelRecommendation) {
        TopicLevelRecommendationResponse topicLevelRecommendationResponse = new TopicLevelRecommendationResponse();
        if (assessmentService.checkTopicRecommendationId(topicLevelRecommendation.getRecommendationId())) {
            topicLevelRecommendationResponse = modelMapper.map(topicLevelRecommendation,TopicLevelRecommendationResponse.class);
            TopicLevelRecommendation finalTopicLevelRecommendation = topicLevelRecommendation;
            CompletableFuture.supplyAsync(() -> activityLogService.saveActivityLog(assessment, user, finalTopicLevelRecommendation.getRecommendationId(), finalTopicLevelRecommendation.getTopic(), ActivityType.TOPIC_RECOMMENDATION));

        } else {
            topicLevelRecommendationResponse.setRecommendationId(null);
        }
        return topicLevelRecommendationResponse;
    }
}

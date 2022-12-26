/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.mappers.AssessmentMapper;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
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

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toSet;


@Controller("/v1/assessments")
public class AssessmentController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentController.class);

    private final AnswerService answerService;
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final UsersAssessmentsService usersAssessmentsService;
    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;
    private final ParameterService parameterService;
    private final TopicService topicService;
    private final UserQuestionService userQuestionService;
    private final NotificationService notificationService;

    private AssessmentMapper assessmentMapper = new AssessmentMapper();

    @Value("${validation.email:^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$}")
    private String emailPattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

    @Inject


    private static final ModelMapper modelMapper = new ModelMapper();


    public AssessmentController(UsersAssessmentsService usersAssessmentsService, UserAuthService userAuthService, AssessmentService assessmentService, AnswerService answerService, TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, ParameterService parameterService, TopicService topicService, NotificationService notificationService, UserQuestionService userQuestionService) {
        this.usersAssessmentsService = usersAssessmentsService;
        this.userAuthService = userAuthService;
        this.assessmentService = assessmentService;
        this.answerService = answerService;
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.parameterService = parameterService;
        this.topicService = topicService;
        this.userQuestionService = userQuestionService;
        this.notificationService = notificationService;

    }


    @Get(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AssessmentResponse>> getAssessments(Authentication authentication) {
        LOGGER.info("Get all assessments");
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        List<Assessment> assessments = usersAssessmentsService.findAssessments(loggedInUser.getUserEmail());
        List<AssessmentResponse> assessmentResponses = new ArrayList<>();
        if (Objects.nonNull(assessments))
            assessments.forEach(assessment ->
            {
                AssessmentResponse assessmentResponse = assessmentMapper.map(assessment);
                assessmentResponse.setOwner(loggedInUser.getUserEmail().equals(assessment.getOwnerEmail()));
                assessmentResponses.add(assessmentResponse);
            });
        return HttpResponse.ok(assessmentResponses);
    }

    @Post(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> createAssessment(@Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        LOGGER.info("Create new assessment");
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        assessmentRequest.validate(emailPattern);

        Assessment assessment = assessmentService.createAssessment(assessmentRequest, loggedInUser);

        AssessmentResponse assessmentResponse = assessmentMapper.map(assessment);

        CompletableFuture.supplyAsync(() -> notificationService.setNotificationForCreateAssessment(assessment));

        return HttpResponse.created(assessmentResponse);
    }

    @Put(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Assessment> updateAssessment(@PathVariable("assessmentId") Integer assessmentId, @Valid @Body AssessmentRequest assessmentRequest, Authentication authentication) {
        LOGGER.info("Update assessment : {}", assessmentId);
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        assessmentRequest.validate(emailPattern);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            assessment.setAssessmentName(assessmentRequest.getAssessmentName());
            assessment.getOrganisation().setOrganisationName(assessmentRequest.getOrganisationName());
            assessment.getOrganisation().setDomain(assessmentRequest.getDomain());
            assessment.getOrganisation().setIndustry(assessmentRequest.getIndustry());
            assessment.getOrganisation().setSize(assessmentRequest.getTeamSize());
            assessment.setAssessmentPurpose(assessmentRequest.getAssessmentPurpose());
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


    @Put(value = "/{assessmentId}/statuses/open", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentResponse> reopenAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {

        LOGGER.info("Reopen assessment : {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        Assessment openedAssessment = assessmentService.reopenAssessment(assessment);
        AssessmentResponse assessmentResponse = assessmentMapper.map(openedAssessment);

        CompletableFuture.supplyAsync(() -> notificationService.setNotificationForReopenAssessment(assessment));

        return HttpResponse.ok(assessmentResponse);
    }


    @Put(value = "/{assessmentId}/statuses/finish", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
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
    public HttpResponse<AssessmentResponse> getAssessment(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        LOGGER.info("Get assessment : {}", assessmentId);
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);

        List<Answer> answerResponse = answerService.getAnswers(assessment.getAssessmentId());

        List<UserQuestion> userQuestionList = userQuestionService.findAllUserQuestion(assessment.getAssessmentId());

        List<TopicLevelAssessment> topicLevelAssessmentList = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessment.getAssessmentId());
        List<TopicLevelRecommendation> topicLevelRecommendationList = topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessment.getAssessmentId());
        List<TopicRatingAndRecommendation> topicRecommendationResponses = mergeTopicRatingAndRecommendation(topicLevelAssessmentList, topicLevelRecommendationList);
        List<ParameterLevelAssessment> parameterLevelAssessmentList = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessment.getAssessmentId());

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessment.getAssessmentId());
        List<ParameterRatingAndRecommendation> paramRecommendationResponses = mergeParamRatingAndRecommendation(parameterLevelAssessmentList, parameterLevelRecommendationList);


        AssessmentResponse assessmentResponse = assessmentMapper.map(assessment, answerResponse, userQuestionList, topicRecommendationResponses, paramRecommendationResponses);
        assessmentResponse.setOwner(loggedInUser.getUserEmail().equals(assessment.getOwnerEmail()));

        return HttpResponse.ok(assessmentResponse);
    }


    @Post(value = "/notes/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessmentRequest> saveAnswer(@PathVariable("assessmentId") Integer assessmentId, @Body TopicLevelAssessmentRequest topicLevelAssessmentRequests, Authentication authentication) {
        LOGGER.info("Save assessment data : {}", assessmentId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);

        if (assessment.isEditable()) {
            List<Answer> answerList = setAnswerListToSave(topicLevelAssessmentRequests, assessment);
            List<UserQuestion> userQuestionList = setUserQuestionListToSave(topicLevelAssessmentRequests, assessment);
            if (topicLevelAssessmentRequests.isRatedAtTopicLevel()) {
                TopicLevelAssessment topicLevelRatingAndRecommendation = setTopicLevelRatingAndRecommendation(topicLevelAssessmentRequests, assessment);
                List<TopicLevelRecommendation> topicLevelRecommendationList = setTopicLevelRecommendation(topicLevelAssessmentRequests, assessment);
                topicAndParameterLevelAssessmentService.saveTopicLevelAssessment(topicLevelRatingAndRecommendation, topicLevelRecommendationList, answerList, userQuestionList);
            } else {
                List<ParameterLevelAssessment> parameterLevelAssessmentList = setParameterLevelRatingAndResommendationList(topicLevelAssessmentRequests, assessment);
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = setParameterLevelRecommendation(assessment, topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList());
                topicAndParameterLevelAssessmentService.saveParameterLevelAssessment(parameterLevelAssessmentList, parameterLevelRecommendationList, answerList, userQuestionList);
            }
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }


    @Patch(value = "/parameterRecommendation/{assessmentId}/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterLevelRecommendationResponse> saveParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Update individual parameter maturity recommendation. assessment: {}, parameter: {}", assessmentId, parameterId);
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        ParameterLevelRecommendationResponse parameterLevelRecommendationResponse = new ParameterLevelRecommendationResponse();
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        if (assessment.isEditable()) {
            if (parameterLevelRecommendationRequest.getRecommendationId() == null) {
                parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
                parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
            } else {
                parameterLevelRecommendation = topicAndParameterLevelAssessmentService.searchParameterRecommendation(parameterLevelRecommendationRequest.getRecommendationId()).orElse(new ParameterLevelRecommendation());
                parameterLevelRecommendation.setRecommendationId(parameterLevelRecommendationRequest.getRecommendationId());
                parameterLevelRecommendation.setRecommendation(parameterLevelRecommendationRequest.getRecommendation());
                saveParameterRecommendationImpact(parameterLevelRecommendationRequest, parameterLevelRecommendation);
                saveParameterRecommendationEffort(parameterLevelRecommendationRequest, parameterLevelRecommendation);
                saveParameterRecommendationDeliveryHorizon(parameterLevelRecommendationRequest, parameterLevelRecommendation);
            }
            topicAndParameterLevelAssessmentService.saveParameterLevelRecommendation(parameterLevelRecommendation);
            updateAssessment(assessment);
            if (topicAndParameterLevelAssessmentService.checkParameterRecommendationId(parameterLevelRecommendation.getRecommendationId())) {
                parameterLevelRecommendationResponse.setRecommendationId(parameterLevelRecommendation.getRecommendationId());
                parameterLevelRecommendationResponse.setRecommendation(parameterLevelRecommendation.getRecommendation());
            } else {
                parameterLevelRecommendationResponse.setRecommendationId(null);
            }

        }
        return HttpResponse.ok(parameterLevelRecommendationResponse);
    }


    @Patch(value = "/topicRecommendationText/{assessmentId}/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelRecommendationResponse> saveTopicRecommendationText(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body TopicLevelRecommendationTextRequest topicLevelRecommendationTextRequest, Authentication authentication) {
        LOGGER.info("Update individual topic recommendation text. assessment: {}, topic: {}", assessmentId, topicId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        TopicLevelRecommendationResponse topicLevelRecommendationResponse = new TopicLevelRecommendationResponse();
        AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setTopic(assessmentTopic);
        if (assessment.isEditable()) {
            if (topicLevelRecommendationTextRequest.getRecommendationId() == null) {
                topicLevelRecommendation.setRecommendationId(topicLevelRecommendationTextRequest.getRecommendationId());
            } else {
                topicLevelRecommendation = topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendationTextRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
            }
            topicLevelRecommendation.setRecommendation(topicLevelRecommendationTextRequest.getRecommendation());
            topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);
            updateAssessment(assessment);
            topicLevelRecommendationResponse.setRecommendationId(topicLevelRecommendation.getRecommendationId());
            topicLevelRecommendationResponse.setRecommendation(topicLevelRecommendation.getRecommendation());
        }
        return HttpResponse.ok(topicLevelRecommendationResponse);
    }

    @Patch(value = "/topicRecommendationFields/{assessmentId}/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelRecommendationRequest> saveTopicRecommendationFields(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, @Body TopicLevelRecommendationRequest topicLevelRecommendationRequest, Authentication authentication) {
        LOGGER.info("Update individual topic maturity recommendation. assessment: {}, parameter: {}", assessmentId, topicId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            TopicLevelRecommendation topicLevelRecommendation = topicAndParameterLevelAssessmentService.searchTopicRecommendation(topicLevelRecommendationRequest.getRecommendationId()).orElse(new TopicLevelRecommendation());
            AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
            topicLevelRecommendation.setAssessment(assessment);
            topicLevelRecommendation.setTopic(assessmentTopic);
            topicLevelRecommendation.setRecommendationId(topicLevelRecommendationRequest.getRecommendationId());
            saveTopicRecommendationImpact(topicLevelRecommendationRequest, topicLevelRecommendation);
            saveTopicRecommendationEffort(topicLevelRecommendationRequest, topicLevelRecommendation);
            saveTopicRecommendationDeliveryHorizon(topicLevelRecommendationRequest, topicLevelRecommendation);
            topicAndParameterLevelAssessmentService.saveTopicLevelRecommendation(topicLevelRecommendation);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }


    @Delete(value = "/deleteRecommendation/{assessmentId}/{topicId}/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelRecommendationRequest> deleteRecommendation
            (@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer
                    topicId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
        LOGGER.info("Delete recommendation. assessment: {}, topic: {}", assessmentId, topicId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            topicAndParameterLevelAssessmentService.deleteRecommendation(recommendationId);
        }
        return HttpResponse.ok();
    }

    @Delete(value = "/deleteParameterRecommendation/{assessmentId}/{parameterId}/{recommendationId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterLevelRecommendationRequest> deleteParameterRecommendation(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @PathVariable("recommendationId") Integer recommendationId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Delete recommendation. assessment: {}, parameter: {}", assessmentId, parameterId);
        if (assessment.isEditable()) {
            topicAndParameterLevelAssessmentService.deleteParameterRecommendation(recommendationId);
        }
        return HttpResponse.ok();
    }


    @Patch(value = "/topicRating/{assessmentId}/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicLevelAssessment> saveTopicRating(@PathVariable("assessmentId") Integer
                                                                      assessmentId, @PathVariable("topicId") Integer topicId, @Body @Nullable String rating, Authentication
                                                                      authentication) {
        LOGGER.info("Update individual parameter maturity rating. assessment: {}, parameter: {}", assessmentId, topicId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentTopic assessmentTopic = topicService.getTopic(topicId).orElseThrow();
            TopicLevelId topicLevelId = new TopicLevelId(assessment, assessmentTopic);
            TopicLevelAssessment topicLevelAssessment = topicAndParameterLevelAssessmentService.searchTopic(topicLevelId).orElse(new TopicLevelAssessment());
            topicLevelAssessment.setTopicLevelId(topicLevelId);
            Integer topicRating = rating != null ? Integer.valueOf(rating) : null;
            topicLevelAssessment.setRating(topicRating);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(topicLevelAssessment);
            updateAssessment(assessment);
        }
        return HttpResponse.ok();
    }

    @Patch(value = "/parameterRating/{assessmentId}/{parameterId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterLevelAssessment> saveParameterRating(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body @Nullable String rating, Authentication authentication) {
        LOGGER.info("Update individual parameter maturity rating. assessment: {}, parameter: {}", assessmentId, parameterId);
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable()) {
            AssessmentParameter assessmentParameter = parameterService.getParameter(parameterId).orElseThrow();
            ParameterLevelId parameterLevelId = new ParameterLevelId(assessment, assessmentParameter);
            ParameterLevelAssessment parameterLevelAssessment = topicAndParameterLevelAssessmentService.searchParameter(parameterLevelId).orElse(new ParameterLevelAssessment());
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            Integer parameterRating = rating != null ? Integer.valueOf(rating) : null;
            parameterLevelAssessment.setRating(parameterRating);
            topicAndParameterLevelAssessmentService.saveRatingAndRecommendation(parameterLevelAssessment);
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
        User loggedInUser = userAuthService.getLoggedInUser(authentication);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment.isEditable() && (assessment.getOwnerEmail().equals(loggedInUser.getUserEmail())))
            assessmentService.softDeleteAssessment(assessment);
    }


    private Assessment getAuthenticatedAssessment(Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        return assessmentService.getAssessment(assessmentId, loggedInUser);
    }

    private TopicLevelAssessment setTopicLevelRatingAndRecommendation(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        TopicLevelId topicLevelId = modelMapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelId.class);
        topicLevelId.setAssessment(assessment);
        TopicLevelAssessment topicLevelAssessment = modelMapper.map(topicLevelAssessmentRequests.getTopicRatingAndRecommendation(), TopicLevelAssessment.class);
        topicLevelAssessment.setTopicLevelId(topicLevelId);
        return topicLevelAssessment;
    }

    private List<TopicLevelRecommendation> setTopicLevelRecommendation(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        for (TopicLevelRecommendationRequest topicLevelRecommendationRequest : topicLevelAssessmentRequests.getTopicRatingAndRecommendation().getTopicLevelRecommendationRequest()) {
            TopicLevelRecommendation topicLevelRecommendation = modelMapper.map(topicLevelRecommendationRequest, TopicLevelRecommendation.class);
            topicLevelRecommendation.setAssessment(assessment);
            AssessmentTopic assessmentTopic = topicService.getTopic(topicLevelAssessmentRequests.getTopicRatingAndRecommendation().getTopicId()).orElseThrow();
            topicLevelRecommendation.setTopic(assessmentTopic);
            topicLevelRecommendationList.add(topicLevelRecommendation);
        }
        return topicLevelRecommendationList;
    }

    private List<ParameterLevelRecommendation> setParameterLevelRecommendation(Assessment assessment, List<ParameterLevelAssessmentRequest> parameterLevelAssessmentRequest) {
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest1 : parameterLevelAssessmentRequest) {
            for (ParameterLevelRecommendationRequest parameterLevelRecommendationRequest : parameterLevelAssessmentRequest1.getParameterRatingAndRecommendation().getParameterLevelRecommendationRequest()) {
                parameterLevelRecommendationList.add(setRecommendation(parameterLevelRecommendationRequest, assessment, parameterLevelAssessmentRequest1));
            }
        }
        return parameterLevelRecommendationList;
    }

    private ParameterLevelRecommendation setRecommendation(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest1, Assessment assessment, ParameterLevelAssessmentRequest parameterLevelAssessmentRequest) {
        ParameterLevelRecommendation parameterLevelRecommendation = modelMapper.map(parameterLevelRecommendationRequest1, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        AssessmentParameter assessmentParameter = parameterService.getParameter(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation().getParameterId()).orElseThrow();
        parameterLevelRecommendation.setParameter(assessmentParameter);
        return parameterLevelRecommendation;
    }

    private List<ParameterLevelAssessment> setParameterLevelRatingAndResommendationList(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<ParameterLevelAssessment> parameterLevelAssessmentList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            ParameterLevelId parameterLevelId = modelMapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelId.class);
            parameterLevelId.setAssessment(assessment);
            ParameterLevelAssessment parameterLevelAssessment = modelMapper.map(parameterLevelAssessmentRequest.getParameterRatingAndRecommendation(), ParameterLevelAssessment.class);
            parameterLevelAssessment.setParameterLevelId(parameterLevelId);
            parameterLevelAssessmentList.add(parameterLevelAssessment);
        }
        return parameterLevelAssessmentList;
    }

    private List<Answer> setAnswerListToSave(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<Answer> answerList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            for (AnswerRequest answerRequest : parameterLevelAssessmentRequest.getAnswerRequest()) {
                AnswerId answerId = modelMapper.map(answerRequest, AnswerId.class);
                answerId.setAssessment(assessment);
                Answer answer = modelMapper.map(answerRequest, Answer.class);
                answer.setAnswerId(answerId);
                answerList.add(answer);
            }
        }
        return answerList;
    }

    private List<UserQuestion> setUserQuestionListToSave(TopicLevelAssessmentRequest topicLevelAssessmentRequests, Assessment assessment) {
        List<UserQuestion> userQuestionList = new ArrayList<>();
        for (ParameterLevelAssessmentRequest parameterLevelAssessmentRequest : topicLevelAssessmentRequests.getParameterLevelAssessmentRequestList()) {
            for (UserQuestionRequest userQuestionRequest : parameterLevelAssessmentRequest.getUserQuestionRequestList()) {
                UserQuestion userQuestion = modelMapper.map(userQuestionRequest, UserQuestion.class);
                userQuestion.setAssessment(assessment);
                AssessmentParameter parameter = parameterService.getParameter(userQuestionRequest.getParameterId()).orElseThrow();
                userQuestion.setParameter(parameter);
                userQuestionList.add(userQuestion);
            }
        }
        return userQuestionList;
    }


    private void saveTopicRecommendationEffort(TopicLevelRecommendationRequest topicLevelRecommendationRequest, TopicLevelRecommendation topicLevelRecommendation) {
        if (!Objects.equals(topicLevelRecommendationRequest.getEffort(), "") && topicLevelRecommendationRequest.getEffort() != null) {
            topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.valueOf(topicLevelRecommendationRequest.getEffort()));
        } else {
            topicLevelRecommendation.setRecommendationEffort(null);
        }
    }

    private TopicLevelRecommendation saveTopicRecommendationImpact(TopicLevelRecommendationRequest topicLevelRecommendationRequest, TopicLevelRecommendation topicLevelRecommendation) {
        if (!Objects.equals(topicLevelRecommendationRequest.getImpact(), "") && topicLevelRecommendationRequest.getImpact() != null) {
            topicLevelRecommendation.setRecommendationImpact(RecommendationImpact.valueOf(topicLevelRecommendationRequest.getImpact()));
        } else {
            topicLevelRecommendation.setRecommendationImpact(null);
        }
        return topicLevelRecommendation;
    }

    private void saveTopicRecommendationDeliveryHorizon(TopicLevelRecommendationRequest topicLevelRecommendationRequest, TopicLevelRecommendation topicLevelRecommendation) {
        topicLevelRecommendation.setDeliveryHorizon(topicLevelRecommendationRequest.getDeliveryHorizon());
    }

    private void saveParameterRecommendationEffort(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, ParameterLevelRecommendation parameterLevelRecommendation) {
        if (!Objects.equals(parameterLevelRecommendationRequest.getEffort(), "") && parameterLevelRecommendationRequest.getEffort() != null) {
            parameterLevelRecommendation.setRecommendationEffort(RecommendationEffort.valueOf(parameterLevelRecommendationRequest.getEffort()));
        } else {
            parameterLevelRecommendation.setRecommendationEffort(null);
        }
    }

    private void saveParameterRecommendationImpact(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, ParameterLevelRecommendation parameterLevelRecommendation) {

        if (!Objects.equals(parameterLevelRecommendationRequest.getImpact(), "") && parameterLevelRecommendationRequest.getImpact() != null) {
            parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.valueOf(parameterLevelRecommendationRequest.getImpact()));
        } else {
            parameterLevelRecommendation.setRecommendationImpact(null);
        }
    }

    private void saveParameterRecommendationDeliveryHorizon(ParameterLevelRecommendationRequest parameterLevelRecommendationRequest, ParameterLevelRecommendation parameterLevelRecommendation) {
        if (!Objects.equals(parameterLevelRecommendationRequest.getDeliveryHorizon(), "") && parameterLevelRecommendationRequest.getDeliveryHorizon() != null) {

            parameterLevelRecommendation.setDeliveryHorizon(parameterLevelRecommendationRequest.getDeliveryHorizon());
        } else {
            parameterLevelRecommendation.setDeliveryHorizon(null);
        }
    }

    @Post(value = "/{assessmentId}/{parameterId}/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserQuestionResponse> saveUserQuestion(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("parameterId") Integer parameterId, @Body String userQuestion, Authentication authentication) {
        LOGGER.info("Save individual user added questions. assessment: {}, parameter:{}", assessmentId, parameterId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        UserQuestion savedQuestion = new UserQuestion();
        if (assessment.isEditable()) {
            savedQuestion = userQuestionService.saveUserQuestion(assessment, parameterId, userQuestion);
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
        if (assessment.isEditable() && answerRequest!=null) {
            if(answerRequest.getType() == AnswerType.DEFAULT){
                answerService.saveAnswer(answerRequest,assessment);
            }
            else {
                userQuestionService.saveUserAnswer(questionId, answerRequest.getAnswer());
            }
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
            userQuestionService.updateUserQuestion(questionId, updatedQuestion);
        }
        return HttpResponse.ok();
    }

    private List<ParameterRatingAndRecommendation> mergeParamRatingAndRecommendation(List<ParameterLevelAssessment> parameterLevelAssessmentList, List<ParameterLevelRecommendation> parameterLevelRecommendationList) {
        List<ParameterRatingAndRecommendation> parameterRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedParams = new HashSet<>();

        for (ParameterLevelAssessment paramLevelAssessment : parameterLevelAssessmentList) {
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

    private List<TopicRatingAndRecommendation> mergeTopicRatingAndRecommendation(List<TopicLevelAssessment> topicLevelAssessmentList, List<TopicLevelRecommendation> topicLevelRecommendationList) {
        List<TopicRatingAndRecommendation> topicRatingAndRecommendationsResponse = new ArrayList<>();
        Set<Integer> processedTopics = new HashSet<>();

        for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessmentList) {
            Integer topicId = topicLevelAssessment.getTopicLevelId().getTopic().getTopicId();
            processedTopics.add(topicId);
            TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
            topicRatingAndRecommendation.setTopicId(topicId);
            topicRatingAndRecommendation.setRating(topicLevelAssessment.getRating());
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

    @Delete(value = "/{assessmentId}/questions/{questionId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserQuestion> deleteUserQuestion(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("questionId") Integer questionId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        LOGGER.info("Delete user question. assessment: {}", assessmentId);
        if (assessment.isEditable() && userQuestionService.searchUserQuestion(questionId).isPresent()) {
            userQuestionService.deleteUserQuestion(questionId);
        }
        return HttpResponse.ok();
    }

}

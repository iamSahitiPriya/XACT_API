/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;


import com.xact.assessment.annotations.ContributorAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Introspected
@ContributorAuth
@Controller("/v1/contributor")
@Transactional
public class ContributorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContributorController.class);
    private static final ModelMapper mapper = new ModelMapper();
    private final UserAuthService userAuthService;
    private final ModuleContributorService contributorService;

    static {
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMap = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
            }
        };
        PropertyMap<AssessmentTopic, AssessmentTopicDto> topicMap = new PropertyMap<>() {
            protected void configure() {
                map().setModule(source.getModule().getModuleId());
            }
        };
        PropertyMap<AssessmentParameter, AssessmentParameterDto> parameterMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<Question, QuestionDto> questionMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        PropertyMap<AssessmentTopicReference, AssessmentTopicReferenceDto> topicReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<AssessmentParameterReference, AssessmentParameterReferenceDto> parameterReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        mapper.addMappings(moduleMap);
        mapper.addMappings(topicMap);
        mapper.addMappings(parameterMap);
        mapper.addMappings(questionMap);
        mapper.addMappings(topicReferenceMap);
        mapper.addMappings(parameterReferenceMap);
    }

    public ContributorController(UserAuthService userAuthService, ModuleContributorService contributorService) {
        this.userAuthService = userAuthService;
        this.contributorService = contributorService;
    }


    @Get(value = "/questions{?role}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ContributorResponse> getContributorQuestions(@QueryValue ContributorRole role, Authentication authentication) {
        LOGGER.info("Get all questions");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        ContributorResponse contributorResponse = contributorService.getContributorResponse(role, loggedInUser.getUserEmail());

        return HttpResponse.ok(contributorResponse);
    }

    @Post(value = "/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionResponse> createQuestion(@Body @Valid QuestionRequest questionRequest, Authentication authentication) {
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        LOGGER.info("{}: Create questions - {}", loggedInUser.getUserEmail(), questionRequest.getQuestionText());
        Question question = contributorService.createAssessmentQuestion(loggedInUser.getUserEmail(), questionRequest);
        return getQuestionResponse(question);
    }

    @Patch(value = "/modules/{moduleId}/questions{?status}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionStatusUpdateResponse> updateContributorQuestionsStatus(@PathVariable Integer moduleId, @QueryValue ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Authentication authentication) {
        LOGGER.info("Update question status");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        QuestionStatusUpdateResponse questionStatusUpdateResponse = contributorService.updateContributorQuestionsStatus(moduleId, status, questionStatusUpdateRequest, loggedInUser.getUserEmail());

        return HttpResponse.ok(questionStatusUpdateResponse);

    }

    @Delete(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> deleteQuestion(@PathVariable Integer questionId, Authentication authentication) {
        LOGGER.info("Delete question: {}", questionId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        contributorService.deleteQuestion(questionId, loggedInUser.getUserEmail());

        return HttpResponse.ok();

    }

    @Patch(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionDto> updateQuestion(@PathVariable Integer questionId, @Body String questionText, Authentication authentication) {
        LOGGER.info("Update question: {}", questionId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        Question question = contributorService.updateContributorQuestion(questionId, questionText, loggedInUser.getUserEmail());
        QuestionDto questionResponse = new QuestionDto();
        questionResponse.setParameter(question.getParameter().getParameterId());
        questionResponse.setQuestionText(question.getQuestionText());
        questionResponse.setQuestionId(question.getQuestionId());
        questionResponse.setStatus(question.getQuestionStatus());

        return HttpResponse.ok(questionResponse);
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> createTopic(@Body @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("{}: Create topics - {}", authentication.getName(), assessmentTopicRequest.getTopicName());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        AssessmentModule assessmentModule = contributorService.getModuleById(assessmentTopicRequest.getModule());
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentTopic assessmentTopic = contributorService.createAssessmentTopics(assessmentTopicRequest);
            TopicResponse topicResponse = mapper.map(assessmentTopic, TopicResponse.class);
            topicResponse.setModuleId(assessmentTopic.getModule().getModuleId());
            topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());
            return HttpResponse.ok(topicResponse);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> createParameter(@Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("{}: Create parameter - {}", authentication.getName(), assessmentParameterRequest.getParameterName());
        AssessmentModule assessmentModule = contributorService.getModule(assessmentParameterRequest.getTopic());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentParameter assessmentParameter = contributorService.createAssessmentParameter(assessmentParameterRequest);
            ParameterResponse parameterResponse = mapper.map(assessmentParameter, ParameterResponse.class);
            parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
            parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
            parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
            return HttpResponse.ok(parameterResponse);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Post(value = "/topic-references", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReferenceDto> createTopicReference(@Body TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Create topic reference - {}", authentication.getName(), topicReferencesRequest.getReference());
        AssessmentModule assessmentModule = contributorService.getModule(topicReferencesRequest.getTopic());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentTopicReference assessmentTopicReference = contributorService.createAssessmentTopicReference(topicReferencesRequest);
            AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference, AssessmentTopicReferenceDto.class);
            return HttpResponse.ok(assessmentTopicReferenceDto);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Post(value = "/parameter-references", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> createParameterReference(@Body ParameterReferencesRequest parameterReferencesRequests, Authentication authentication) {
        LOGGER.info("{}: Create parameter reference - {}", authentication.getName(), parameterReferencesRequests.getParameter());
        AssessmentModule assessmentModule = contributorService.getModuleByParameter(parameterReferencesRequests.getParameter());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentParameterReference assessmentParameterReference = contributorService.createAssessmentParameterReference(parameterReferencesRequests);
            AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
            return HttpResponse.ok(assessmentParameterReferenceDto);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Put(value = "/topics/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> updateTopic(@PathVariable("topicId") Integer topicId, @Body @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("{}: Update topic - {}", authentication.getName(), assessmentTopicRequest.getTopicName());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        AssessmentModule assessmentModule = contributorService.getModuleById(assessmentTopicRequest.getModule());
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentTopic assessmentTopic = contributorService.updateTopic(topicId, assessmentTopicRequest);
            TopicResponse topicResponse = mapper.map(assessmentTopic, TopicResponse.class);
            topicResponse.setUpdatedAt(assessmentTopic.getUpdatedAt());
            topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());
            return HttpResponse.ok(topicResponse);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Put(value = "/parameters/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("{}: Update parameter - {}", authentication.getName(), assessmentParameterRequest.getParameterName());
        AssessmentModule assessmentModule = contributorService.getModule(assessmentParameterRequest.getTopic());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentParameter assessmentParameter = contributorService.updateParameter(parameterId, assessmentParameterRequest);
            ParameterResponse parameterResponse = mapper.map(assessmentParameter, ParameterResponse.class);
            parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
            parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
            parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
            return HttpResponse.ok(parameterResponse);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Put(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionResponse> updateQuestion(@PathVariable("questionId") Integer questionId, QuestionRequest questionRequest, Authentication authentication) {
        LOGGER.info("{}: Update question - {}", authentication.getName(), questionRequest.getQuestionText());
        Question question = contributorService.updateQuestion(questionId, questionRequest);
        return getQuestionResponse(question);
    }

    @Put(value = "/topic-references/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReferenceDto> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Update topic-reference - {}", authentication.getName(), topicReferencesRequest.getReference());
        AssessmentModule assessmentModule = contributorService.getModule(topicReferencesRequest.getTopic());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentTopicReference assessmentTopicReference = contributorService.updateTopicReference(referenceId, topicReferencesRequest);
            AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference, AssessmentTopicReferenceDto.class);
            return HttpResponse.ok(assessmentTopicReferenceDto);
        } else {
            return HttpResponse.unauthorized();
        }
    }

    @Put(value = "/parameter-references/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Update parameter-reference - {}", authentication.getName(), parameterReferencesRequest.getReference());
        AssessmentModule assessmentModule = contributorService.getModuleByParameter(parameterReferencesRequest.getParameter());
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if (isValid(loggedInUser, assessmentModule)) {
            AssessmentParameterReference assessmentParameterReference = contributorService.updateParameterReference(referenceId, parameterReferencesRequest);
            AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
            return HttpResponse.ok(assessmentParameterReferenceDto);
        } else {
            return HttpResponse.unauthorized();
        }

    }

    @Delete(value = "topic-references/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicReferencesRequest> deleteTopicReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("{}: Delete topic reference. referenceId - {}", authentication.getName(), referenceId);
        AssessmentModule assessmentModule = contributorService.getModuleByTopicReference(referenceId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if(isValid(loggedInUser,assessmentModule)) {
            contributorService.deleteTopicReference(referenceId);
            return HttpResponse.ok();
        }else{
            return HttpResponse.unauthorized();
        }
    }

    @Delete(value = "parameter-references/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterReferencesRequest> deleteParameterReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("{}: Delete parameter reference. referenceId: {}", authentication.getName(), referenceId);
        AssessmentModule assessmentModule = contributorService.getModuleByParameterReference(referenceId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        if(isValid(loggedInUser,assessmentModule)) {
            contributorService.deleteParameterReference(referenceId);
            return HttpResponse.ok();
        }else{
            return HttpResponse.unauthorized();
        }
    }

    private boolean isValid(User loggedInUser, AssessmentModule module) {
        return contributorService.validate(loggedInUser, module);
    }

    private HttpResponse<QuestionResponse> getQuestionResponse(Question question) {
        QuestionResponse questionResponse = mapper.map(question, QuestionResponse.class);
        questionResponse.setCategory(question.getParameter().getTopic().getModule().getCategory().getCategoryId());
        questionResponse.setModule(question.getParameter().getTopic().getModule().getModuleId());
        questionResponse.setTopic(question.getParameter().getTopic().getTopicId());
        questionResponse.setParameterId(question.getParameter().getParameterId());
        return HttpResponse.ok(questionResponse);
    }


}

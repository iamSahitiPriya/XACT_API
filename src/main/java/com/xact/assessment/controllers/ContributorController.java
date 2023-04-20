/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;


import com.xact.assessment.annotations.ContributorAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Question;
import com.xact.assessment.models.User;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;

@ContributorAuth
@Controller("/v1/contributor")
public class ContributorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContributorController.class);
    private static final ModelMapper mapper = new ModelMapper();
    private final UserAuthService userAuthService;
    private final ModuleContributorService contributorService;

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
        Question question = contributorService.createAssessmentQuestion(loggedInUser.getUserEmail(),questionRequest);
        return getQuestionResponse(question);
    }

    private HttpResponse<QuestionResponse> getQuestionResponse(Question question) {
        QuestionResponse questionResponse = mapper.map(question, QuestionResponse.class);
        questionResponse.setCategory(question.getParameter().getTopic().getModule().getCategory().getCategoryId());
        questionResponse.setModule(question.getParameter().getTopic().getModule().getModuleId());
        questionResponse.setTopic(question.getParameter().getTopic().getTopicId());
        questionResponse.setParameterId(question.getParameter().getParameterId());
        return HttpResponse.ok(questionResponse);
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


}

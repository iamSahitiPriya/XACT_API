/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;


import com.xact.assessment.annotations.ContributorAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Question;
import com.xact.assessment.models.User;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;

@ContributorAuth
@Controller("/v1/contributor")
public class ContributorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContributorController.class);

    private final QuestionService questionService;
    private final UserAuthService userAuthService;


    public ContributorController(QuestionService questionService, UserAuthService userAuthService) {
        this.questionService = questionService;

        this.userAuthService = userAuthService;
    }

    @Get(value = "/questions{?role}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ContributorResponse> getContributorQuestions(@QueryValue ContributorRole role, Authentication authentication) {
        LOGGER.info("Get all questions");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        ContributorResponse contributorResponse = questionService.getContributorResponse(role, loggedInUser.getUserEmail());

        return HttpResponse.ok(contributorResponse);
    }


    @Patch(value = "/modules/{moduleId}/questions{?status}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionStatusUpdateResponse> updateContributorQuestionsStatus(@PathVariable Integer moduleId, @QueryValue ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Authentication authentication) {
        LOGGER.info("Update question status");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        QuestionStatusUpdateResponse questionStatusUpdateResponse = questionService.updateContributorQuestionsStatus(moduleId, status, questionStatusUpdateRequest, loggedInUser.getUserEmail());

        return HttpResponse.ok(questionStatusUpdateResponse);

    }

    @Delete(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> deleteQuestion(@PathVariable Integer questionId, Authentication authentication) {
        LOGGER.info("Delete question: {}", questionId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        questionService.deleteQuestion(questionId, loggedInUser.getUserEmail());

        return HttpResponse.ok();

    }

    @Patch(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionDto> updateQuestion(@PathVariable Integer questionId, @Body String questionText, Authentication authentication) {
        LOGGER.info("Update question: {}", questionId);
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        Question question = questionService.updateContributorQuestion(questionId, questionText, loggedInUser.getUserEmail());
        QuestionDto questionResponse = new QuestionDto();
        questionResponse.setParameter(question.getParameter().getParameterId());
        questionResponse.setQuestionText(question.getQuestionText());
        questionResponse.setQuestionId(question.getQuestionId());
        questionResponse.setStatus(question.getQuestionStatus());

        return HttpResponse.ok(questionResponse);

    }


}

package com.xact.assessment.controllers;


import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Question;
import com.xact.assessment.services.QuestionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;

@Controller("/v1/contributor")
public class ContributorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContributorController.class);

    private final QuestionService questionService;


    public ContributorController(QuestionService questionService) {
        this.questionService = questionService;

    }

    @Get(value = "/questions{?role}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ContributorResponse> getContributorQuestions(@QueryValue ContributorRole role, Authentication authentication) {
        LOGGER.info("Get all questions");
        ContributorResponse contributorResponse = questionService.getContributorResponse(role, authentication.getName());

        return HttpResponse.ok(contributorResponse);
    }


    @Patch(value = "/{moduleId}/questions/{status}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionStatusUpdateResponse> updateContributorQuestionsStatus(@PathVariable Integer moduleId, @PathVariable ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Authentication authentication) {
        LOGGER.info("update question status");
        QuestionStatusUpdateResponse questionStatusUpdateResponse = questionService.updateContributorQuestionsStatus(moduleId, status, questionStatusUpdateRequest, authentication.getName());

        return HttpResponse.ok(questionStatusUpdateResponse);

    }

    @Delete(value = "/question/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> deleteQuestion(@PathVariable Integer questionId, Authentication authentication) {
        LOGGER.info("delete question: {}",questionId);
        questionService.deleteQuestion(questionId, authentication.getName());

        return HttpResponse.ok();

    }

    @Patch(value = "/question/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<QuestionDto> updateQuestion(@PathVariable Integer questionId, @Body String questionText, Authentication authentication) {
        LOGGER.info("Update question: {}", questionId);
        Question question = questionService.updateContributorQuestion(questionId, questionText, authentication.getName());
        QuestionDto questionResponse = new QuestionDto();
        questionResponse.setParameter(question.getParameter().getParameterId());
        questionResponse.setQuestionText(question.getQuestionText());
        questionResponse.setQuestionId(question.getQuestionId());
        questionResponse.setStatus(question.getQuestionStatus());

        return HttpResponse.ok(questionResponse);

    }


}

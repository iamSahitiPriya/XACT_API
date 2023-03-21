package com.xact.assessment.controllers;


import com.xact.assessment.dtos.*;

import com.xact.assessment.models.Question;
import com.xact.assessment.services.QuestionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

@Controller("/v1")
public class ContributorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentController.class);

    private final QuestionService questionService;

    public ContributorController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Get(value = "/contributor/questions{?role}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ContributorDataResponse> getContributorQuestions(@QueryValue ContributorRole role, Authentication authentication) {
        LOGGER.info("Get all questions");
        ContributorDataResponse contributorDataResponse = questionService.getContributorQuestions(role,authentication.getName());

        return HttpResponse.ok(contributorDataResponse);

    }

    @Patch(value = "/{moduleId}/questions/{status}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> updateContributorQuestionsStatus(@PathVariable Integer moduleId, @PathVariable ContributorQuestionStatus status, QuestionStatusUpdateRequest questionStatusUpdateRequest, Authentication authentication) {
        LOGGER.info("update question status");
        questionService.updateContributorQuestionsStatus(moduleId, status, questionStatusUpdateRequest, authentication.getName());

        return HttpResponse.ok();

    }

    @Delete(value = "/question/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> deleteQuestion(@PathVariable Integer questionId, Authentication authentication) {
        LOGGER.info("delete question");
        questionService.deleteQuestion(questionId, authentication.getName());

        return HttpResponse.ok();

    }


}

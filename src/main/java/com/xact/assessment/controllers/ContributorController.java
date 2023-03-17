package com.xact.assessment.controllers;


import com.xact.assessment.dtos.ContributorCategoryData;

import com.xact.assessment.dtos.ContributorDataResponse;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.services.QuestionService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
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

    @Get(value = "/{role}/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<ContributorDataResponse> getContributorQuestions(@PathVariable ContributorRole role, Authentication authentication) {
        LOGGER.info("Get all questions");
        ContributorDataResponse contributorDataResponse = questionService.getContributorQuestions(role,authentication.getName());

        return HttpResponse.ok(contributorDataResponse);

    }

}
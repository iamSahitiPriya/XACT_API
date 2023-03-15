package com.xact.assessment.controllers;


import com.xact.assessment.models.ContributorData;
import com.xact.assessment.services.ContributorDataService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
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

    private final ContributorDataService contributorDataService;

    public ContributorController(ContributorDataService contributorDataService) {
        this.contributorDataService = contributorDataService;
    }

    @Get(value = "/author/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Transactional
    public HttpResponse<List<ContributorData>> getQuestions(Authentication authentication) {
        LOGGER.info("Get all questions");
        List<ContributorData> contributorDataList = contributorDataService.getQuestions(authentication.getName());
        return HttpResponse.ok(contributorDataList);

    }

}
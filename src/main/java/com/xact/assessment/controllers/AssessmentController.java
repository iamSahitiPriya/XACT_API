package com.xact.assessment.controllers;

import com.xact.assessment.functions.AssessmentHandler;
import com.xact.assessment.models.Assessment;
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

@Controller("/v1/assessments")
public class AssessmentController {
    private static Logger logger = LoggerFactory.getLogger(AssessmentHandler.class);


    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse get(@PathVariable String assessmentId, Authentication authentication) {
        logger.info("AssessmentId : {}",assessmentId);
        logger.info("Auth attributes : " + authentication.getAttributes());
        Assessment assessment = new Assessment();
        assessment.setName("Created successfully");
        return HttpResponse.ok(assessment);
    }
    @Get(value = "/open/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse get(@PathVariable String assessmentId) {
        logger.info("AssessmentId : {}",assessmentId);
        Assessment assessment = new Assessment();
        assessment.setName("Created an anonymous endpoint");
        return HttpResponse.ok(assessment);
    }
}

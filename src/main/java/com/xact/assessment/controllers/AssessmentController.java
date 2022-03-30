package com.xact.assessment.controllers;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.AssessmentService;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentController.class);
    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService){
        this.assessmentService= assessmentService;
    }

    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Assessment> getAssessmentSecure(@PathVariable String assessmentId, Authentication authentication) {
        LOGGER.info("AssessmentId : {}",assessmentId);
        LOGGER.info("Auth attributes : " + authentication.getAttributes());
        Assessment assessment = new Assessment();
        assessment.setName("Created successfully");
        return HttpResponse.ok(assessment);
    }


    @Get(value = "/open/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<Assessment> getAssessmentOpen(@PathVariable String assessmentId) {
        LOGGER.info("AssessmentId : {}",assessmentId);
        return HttpResponse.ok(assessmentService.getAssessmentById(assessmentId));
    }
}

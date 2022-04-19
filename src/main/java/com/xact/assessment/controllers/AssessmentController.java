package com.xact.assessment.controllers;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.services.UsersAssessmentsService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

import static com.xact.assessment.constants.AuthConstants.EMAIL;

@Controller("/v1/assessments")
public class AssessmentController {
    private UsersAssessmentsService usersAssessmentsService;

    public AssessmentController(UsersAssessmentsService usersAssessmentsService) {
        this.usersAssessmentsService = usersAssessmentsService;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public List<Assessment> getAssessments(Authentication authentication) {
        String userEmail = (String) authentication.getAttributes().get(EMAIL);

        return usersAssessmentsService.findAssessments(userEmail);
    }


}

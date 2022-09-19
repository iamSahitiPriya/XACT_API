/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

@Controller("/v1/users")

public class UserController {
    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;

    public UserController(AssessmentService assessmentService, UserAuthService userAuthService) {
        this.assessmentService = assessmentService;
        this.userAuthService = userAuthService;
    }
    @Get(value = "/roles")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Optional<AccessControlRoles>> getRole(Authentication authentication){
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        Optional<AccessControlRoles> accessControlRoles = assessmentService.getUserRole(loggedInUser.getUserEmail());
        System.out.println("-----"+accessControlRoles);
        return HttpResponse.ok(accessControlRoles);
    }
}

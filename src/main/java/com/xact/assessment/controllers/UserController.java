/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("/v1/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final AssessmentService assessmentService;
    private final UserAuthService userAuthService;

    public UserController(AssessmentService assessmentService, UserAuthService userAuthService) {
        this.assessmentService = assessmentService;
        this.userAuthService = userAuthService;
    }

    @Get(value = "/roles")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AccessControlRoles>> getRole(Authentication authentication) {
        List<AccessControlRoles> roles = new ArrayList<>();
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        Optional<AccessControlRoles> accessControlRoles = assessmentService.getUserRole(loggedInUser.getUserEmail());
        accessControlRoles.ifPresent(roles::add);
        List<ContributorRole> contributorRoles = userAuthService.getContributorRoles(loggedInUser.getUserEmail());
        contributorRoles.stream().forEach(contributorRole -> roles.add(AccessControlRoles.valueOf(contributorRole.toString())));
        return HttpResponse.ok(roles);
    }

    @Get(value = "/login")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void login(@Header("authorization") String authorizationHeader,Authentication authentication) {
        LOGGER.info("Fetching user details");
        User currentUser = userAuthService.getCurrentUser(authentication);
        userAuthService.login(authorizationHeader,currentUser);
    }
}

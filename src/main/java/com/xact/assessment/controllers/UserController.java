/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.services.AccessControlService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Controller("/v1/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final AccessControlService accessControlService;
    private final UserAuthService userAuthService;

    private static final ModelMapper modelMapper = new ModelMapper();

    public UserController(AccessControlService accessControlService, UserAuthService userAuthService) {
        this.accessControlService = accessControlService;
        this.userAuthService = userAuthService;
    }

    @Get(value = "/roles")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Set<AccessControlRoles>> getRole(Authentication authentication) {
        LOGGER.info("Getting role for the user: {}", authentication.getName());
        Set<AccessControlRoles> roles = new HashSet<>();
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        Optional<AccessControlRoles> accessControlRoles = accessControlService.getAccessControlRolesByEmail(loggedInUser.getUserEmail());
        accessControlRoles.ifPresent(roles::add);
        Set<ModuleContributor> contributorRoles = userAuthService.getContributorRoles(loggedInUser.getUserEmail());
        contributorRoles.stream().forEach(contributorRole -> roles.add(AccessControlRoles.valueOf(contributorRole.getContributorRole().toString())));
        return HttpResponse.ok(roles);
    }

    @Get(value = "/login")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public void login(@Header("authorization") String authorizationHeader, Authentication authentication) {
        LOGGER.info("Fetching user details");
        User currentUser = userAuthService.getCurrentUser(authentication);
        userAuthService.login(authorizationHeader, currentUser);
    }

    @Get
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<UserInfoDto>> getUsers(Authentication authentication) {
        LOGGER.info("Fetching all user details");
        List<UserInfo> users = userAuthService.getUsers();
        List<UserInfoDto> userInfoResponse = new ArrayList<>();
        users.forEach(userInfo -> userInfoResponse.add(modelMapper.map(userInfo, UserInfoDto.class)));
        return HttpResponse.ok(userInfoResponse);
    }
}

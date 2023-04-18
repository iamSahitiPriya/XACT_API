/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.CategoryDto;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

@Introspected
@Controller("/v1")
@Transactional
public class AssessmentMasterDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentMasterDataController.class);

    private final AssessmentMasterDataService assessmentMasterDataService;
    private final UserAuthService userAuthService;

    public AssessmentMasterDataController(AssessmentMasterDataService assessmentMasterDataService, UserAuthService userAuthService) {
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.userAuthService = userAuthService;
    }

    @Get(value = "/categories{?role}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<CategoryDto>> getMasterData(Authentication authentication, @QueryValue String role) {
        LOGGER.info("Get master data");
        User loggedInUser = userAuthService.getCurrentUser(authentication);
        List<CategoryDto> assessmentCategories = assessmentMasterDataService.getMasterDataByRole(loggedInUser,role);
        return HttpResponse.ok(assessmentCategories);
    }
}

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.CategoryDto;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Introspected
@Controller("/v1")
public class AssessmentMasterDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentMasterDataController.class);


    private final MasterDataMapper masterDataMapper = new MasterDataMapper();
    private final AssessmentMasterDataService assessmentMasterDataService;

    public AssessmentMasterDataController(AssessmentMasterDataService assessmentMasterDataService) {
        this.assessmentMasterDataService = assessmentMasterDataService;

    }

    @Get(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<CategoryDto>> getMasterData(Authentication authentication) {
        LOGGER.info("Get master data");
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getCategories();
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(masterDataMapper.mapCategory(assessmentCategory)));
        }
        return HttpResponse.ok(assessmentCategoriesResponse);
    }
}

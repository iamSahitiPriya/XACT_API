/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.annotations.AdminAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.mappers.MasterDataMapper;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentStatus;
import com.xact.assessment.services.AdminService;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Introspected
@AdminAuth
@Controller("/v1/admin")
@Transactional
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private static final ModelMapper mapper = new ModelMapper();
    private final MasterDataMapper masterDataMapper = new MasterDataMapper();
    private final AdminService adminService;

    @Value("${validation.email:^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$}")
    private String emailPattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Post(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<CategoryDto> createAssessmentCategory(@Body @Valid AssessmentCategoryRequest assessmentCategory, Authentication authentication) {
        LOGGER.info("{}: Create category - {}", authentication.getName(), assessmentCategory.getCategoryName());
        AssessmentCategory assessmentCategory1 = adminService.createAssessmentCategory(assessmentCategory);
        CategoryDto categoryDto = masterDataMapper.mapCategory(assessmentCategory1);
        return HttpResponse.ok(categoryDto);
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> createAssessmentModule(@Body @Valid AssessmentModuleRequest assessmentModule, Authentication authentication) {
        LOGGER.info("{}: Create module - {}", authentication.getName(), assessmentModule.getModuleName());
        AssessmentModule assessmentModule1 = adminService.createAssessmentModule(assessmentModule);
        ModuleResponse moduleResponse = mapper.map(assessmentModule1, ModuleResponse.class);
        moduleResponse.setCategoryId(assessmentModule1.getCategory().getCategoryId());
        return HttpResponse.ok(moduleResponse);
    }


    @Put(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<CategoryDto> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body @Valid AssessmentCategoryRequest assessmentCategoryRequest, Authentication authentication) {
        LOGGER.info("{}: Update category - {}", authentication.getName(), assessmentCategoryRequest.getCategoryName());
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        AssessmentCategory assessmentCategory1 = adminService.updateCategory(assessmentCategory, assessmentCategoryRequest);
        CategoryDto categoryDto = masterDataMapper.mapCategory(assessmentCategory1);
        return HttpResponse.ok(categoryDto);
    }

    @Put(value = "/modules/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> updateModule(@PathVariable("moduleId") Integer moduleId, @Body @Valid AssessmentModuleRequest assessmentModuleRequest, Authentication authentication) {
        LOGGER.info("{}: Update module - {}", authentication.getName(), assessmentModuleRequest.getModuleName());
        AssessmentModule assessmentModule = adminService.updateModule(moduleId, assessmentModuleRequest);
        ModuleResponse moduleResponse = mapper.map(assessmentModule, ModuleResponse.class);
        moduleResponse.setCategoryId(assessmentModule.getCategory().getCategoryId());
        moduleResponse.setUpdatedAt(assessmentModule.getUpdatedAt());
        moduleResponse.setContributors(assessmentModuleRequest.getContributors());
        return HttpResponse.ok(moduleResponse);
    }

    @Get(value = "/assessments/{startDate}/{endDate}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AdminAssessmentResponse> getAssessmentsCount(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, Authentication authentication) throws ParseException {
        LOGGER.info("{}: Get assessment from {} to {}", authentication.getName(), startDate, endDate);
        AdminAssessmentResponse adminAssessmentResponse = new AdminAssessmentResponse();
        List<Assessment> allAssessments = adminService.getTotalAssessments(startDate, endDate);
        List<Assessment> activeAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Active).toList();
        List<Assessment> completedAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Completed).toList();
        adminAssessmentResponse.setTotalAssessments(allAssessments.size());
        adminAssessmentResponse.setTotalActiveAssessments(activeAssessments.size());
        adminAssessmentResponse.setTotalCompleteAssessments(completedAssessments.size());
        return HttpResponse.ok(adminAssessmentResponse);
    }


    @Post(value = "/modules/{moduleId}/contributors")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ContributorDto> saveModuleContributor(@PathVariable("moduleId") Integer moduleId, @Valid @Body ContributorRequest contributorRequest, Authentication authentication) {
        LOGGER.info("Save Contributor For {} module by {}", moduleId, authentication.getName());
        contributorRequest.validate(emailPattern);
        adminService.saveContributors(moduleId, contributorRequest.getContributors());
        return HttpResponse.ok();
    }

    private AssessmentCategory getCategory(Integer categoryId) {
        return adminService.getCategory(categoryId);
    }


}

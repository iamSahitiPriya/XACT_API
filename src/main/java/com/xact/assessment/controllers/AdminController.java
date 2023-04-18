/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.annotations.AdminAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.AssessmentService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
    private static final ModelMapper mapper = new ModelMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    static {
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMap = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
            }
        };
        PropertyMap<AssessmentTopic, AssessmentTopicDto> topicMap = new PropertyMap<>() {
            protected void configure() {
                map().setModule(source.getModule().getModuleId());
            }
        };
        PropertyMap<AssessmentParameter, AssessmentParameterDto> parameterMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<Question, QuestionDto> questionMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        PropertyMap<AssessmentTopicReference, AssessmentTopicReferenceDto> topicReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setTopic(source.getTopic().getTopicId());
            }
        };
        PropertyMap<AssessmentParameterReference, AssessmentParameterReferenceDto> parameterReferenceMap = new PropertyMap<>() {
            protected void configure() {
                map().setParameter(source.getParameter().getParameterId());
            }
        };
        mapper.addMappings(moduleMap);
        mapper.addMappings(topicMap);
        mapper.addMappings(parameterMap);
        mapper.addMappings(questionMap);
        mapper.addMappings(topicReferenceMap);
        mapper.addMappings(parameterReferenceMap);
    }

    private final AssessmentMasterDataService assessmentMasterDataService;

    private final AssessmentService assessmentService;


    public AdminController(AssessmentMasterDataService assessmentMasterDataService, AssessmentService assessmentService) {
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.assessmentService = assessmentService;
    }

    @Post(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<CategoryDto> createAssessmentCategory(@Body @Valid AssessmentCategoryRequest assessmentCategory, Authentication authentication) {
        LOGGER.info("{}: Create category - {}", authentication.getName(), assessmentCategory.getCategoryName());
        AssessmentCategory assessmentCategory1 = assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
        CategoryDto categoryDto = mapper.map(assessmentCategory1, CategoryDto.class);
        return HttpResponse.ok(categoryDto);
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> createAssessmentModule(@Body @Valid AssessmentModuleRequest assessmentModule, Authentication authentication) {
        LOGGER.info("{}: Create module - {}", authentication.getName(), assessmentModule.getModuleName());
        AssessmentModule assessmentModule1 = assessmentMasterDataService.createAssessmentModule(assessmentModule);
        ModuleResponse moduleResponse = mapper.map(assessmentModule1, ModuleResponse.class);
        moduleResponse.setCategoryId(assessmentModule1.getCategory().getCategoryId());
        return HttpResponse.ok(moduleResponse);
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> createTopic(@Body @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("{}: Create topics - {}", authentication.getName(), assessmentTopicRequest.getTopicName());
        AssessmentTopic assessmentTopic = assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
        TopicResponse topicResponse = mapper.map(assessmentTopic, TopicResponse.class);
        topicResponse.setModuleId(assessmentTopic.getModule().getModuleId());
        topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());

        return HttpResponse.ok(topicResponse);
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> createParameter(@Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("{}: Create parameter - {}", authentication.getName(), assessmentParameterRequest.getParameterName());

        AssessmentParameter assessmentParameter = assessmentMasterDataService.createAssessmentParameter(assessmentParameterRequest);
        ParameterResponse parameterResponse = mapper.map(assessmentParameter, ParameterResponse.class);
        parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
        parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
        parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
        return HttpResponse.ok(parameterResponse);
    }

    @Post(value = "/topic-references", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReferenceDto> createTopicReference(@Body TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Create topic reference - {}", authentication.getName(), topicReferencesRequest.getReference());
        AssessmentTopicReference assessmentTopicReference = assessmentMasterDataService.createAssessmentTopicReference(topicReferencesRequest);
        AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference, AssessmentTopicReferenceDto.class);
        return HttpResponse.ok(assessmentTopicReferenceDto);
    }

    @Post(value = "/parameter-references", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> createParameterReference(@Body ParameterReferencesRequest parameterReferencesRequests, Authentication authentication) {
        LOGGER.info("{}: Create parameter reference - {}", authentication.getName(), parameterReferencesRequests.getParameter());
        AssessmentParameterReference assessmentParameterReference = assessmentMasterDataService.createAssessmentParameterReference(parameterReferencesRequests);
        AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
        return HttpResponse.ok(assessmentParameterReferenceDto);
    }

    @Put(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<CategoryDto> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body @Valid AssessmentCategoryRequest assessmentCategoryRequest, Authentication authentication) {
        LOGGER.info("{}: Update category - {}", authentication.getName(), assessmentCategoryRequest.getCategoryName());
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        AssessmentCategory assessmentCategory1 = assessmentMasterDataService.updateCategory(assessmentCategory, assessmentCategoryRequest);
        CategoryDto categoryDto = mapper.map(assessmentCategory1, CategoryDto.class);
        return HttpResponse.ok(categoryDto);
    }

    @Put(value = "/modules/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> updateModule(@PathVariable("moduleId") Integer moduleId, @Body @Valid AssessmentModuleRequest assessmentModuleRequest, Authentication authentication) {
        LOGGER.info("{}: Update module - {}", authentication.getName(), assessmentModuleRequest.getModuleName());
        AssessmentModule assessmentModule = assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest);
        ModuleResponse moduleResponse = mapper.map(assessmentModule, ModuleResponse.class);
        moduleResponse.setCategoryId(assessmentModule.getCategory().getCategoryId());
        moduleResponse.setUpdatedAt(assessmentModule.getUpdatedAt());
        return HttpResponse.ok(moduleResponse);
    }

    @Put(value = "/topics/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> updateTopic(@PathVariable("topicId") Integer topicId, @Body @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("{}: Update topic - {}", authentication.getName(), assessmentTopicRequest.getTopicName());
        AssessmentTopic assessmentTopic = assessmentMasterDataService.updateTopic(topicId, assessmentTopicRequest);
        TopicResponse topicResponse = mapper.map(assessmentTopic, TopicResponse.class);
        topicResponse.setUpdatedAt(assessmentTopic.getUpdatedAt());
        topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());
        return HttpResponse.ok(topicResponse);
    }

    @Put(value = "/parameters/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("{}: Update parameter - {}", authentication.getName(), assessmentParameterRequest.getParameterName());
        AssessmentParameter assessmentParameter = assessmentMasterDataService.updateParameter(parameterId, assessmentParameterRequest);
        ParameterResponse parameterResponse = mapper.map(assessmentParameter, ParameterResponse.class);
        parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
        parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
        parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
        return HttpResponse.ok(parameterResponse);
    }

    @Put(value = "/topic-references/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReferenceDto> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Update topic-reference - {}", authentication.getName(), topicReferencesRequest.getReference());
        AssessmentTopicReference assessmentTopicReference = assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
        AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference, AssessmentTopicReferenceDto.class);
        return HttpResponse.ok(assessmentTopicReferenceDto);
    }

    @Put(value = "/parameter-references/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest, Authentication authentication) {
        LOGGER.info("{}: Update parameter-reference - {}", authentication.getName(), parameterReferencesRequest.getReference());
        AssessmentParameterReference assessmentParameterReference = assessmentMasterDataService.updateParameterReference(referenceId, parameterReferencesRequest);
        AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
        return HttpResponse.ok(assessmentParameterReferenceDto);

    }

    @Delete(value = "topic-references/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicReferencesRequest> deleteTopicReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("{}: Delete topic reference. referenceId - {}", authentication.getName(), referenceId);
        assessmentMasterDataService.deleteTopicReference(referenceId);
        return HttpResponse.ok();
    }

    @Delete(value = "parameter-references/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterReferencesRequest> deleteParameterReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("{}: Delete parameter reference. referenceId: {}", authentication.getName(), referenceId);
        assessmentMasterDataService.deleteParameterReference(referenceId);
        return HttpResponse.ok();
    }

    @Get(value = "/assessments/{startDate}/{endDate}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AdminAssessmentResponse> getAssessmentsCount(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, Authentication authentication) throws ParseException {
        LOGGER.info("{}: Get assessment from {} to {}", authentication.getName(), startDate, endDate);
        AdminAssessmentResponse adminAssessmentResponse = new AdminAssessmentResponse();
        List<Assessment> allAssessments = assessmentService.getTotalAssessments(startDate, endDate);
        List<Assessment> activeAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Active).toList();
        List<Assessment> completedAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Completed).toList();
        adminAssessmentResponse.setTotalAssessments(allAssessments.size());
        adminAssessmentResponse.setTotalActiveAssessments(activeAssessments.size());
        adminAssessmentResponse.setTotalCompleteAssessments(completedAssessments.size());
        return HttpResponse.ok(adminAssessmentResponse);
    }

    private AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }


}

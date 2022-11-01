/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Introspected
@AdminAuth
@Controller("/v1/admin")
public class AdminController {
    private static final ModelMapper mapper = new ModelMapper();

    private static  final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

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


    @Get(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<CategoryDto>> getMasterData(Authentication authentication) {
        LOGGER.info("Get master data");
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getCategories();
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(mapper.map(assessmentCategory, CategoryDto.class)));
        }
        return HttpResponse.ok(assessmentCategoriesResponse);
    }

    @Get(value="/modules",produces= MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<ModuleDto>> getModuleMasterData(Authentication authentication){
        LOGGER.info("Get Module Data");
        List<AssessmentModule> assessmentModules=assessmentMasterDataService.getModules();
        List<ModuleDto> assessmentModulesResponse = new ArrayList<>();
        if(Objects.nonNull(assessmentModules)){
            assessmentModules.forEach(assessmentModule ->assessmentModulesResponse.add(mapper.map(assessmentModule,ModuleDto.class)));
        }
        return HttpResponse.ok(assessmentModulesResponse);
    }


    @Post(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> createAssessmentCategory(@Body @Valid AssessmentCategoryRequest assessmentCategory, Authentication authentication) {
        LOGGER.info("Admin: Create category");
        assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
            return HttpResponse.ok();
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> createAssessmentModule(@Body @Valid List<AssessmentModuleRequest> assessmentModules, Authentication authentication) {
        LOGGER.info("Admin: Create module");
        for (AssessmentModuleRequest assessmentModule : assessmentModules) {
            assessmentMasterDataService.createAssessmentModule(assessmentModule);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> createTopics(@Body @Valid List<AssessmentTopicRequest> assessmentTopicRequests, Authentication authentication) {
        LOGGER.info("Admin: Create topics");
        for (AssessmentTopicRequest assessmentTopicRequest : assessmentTopicRequests) {
            assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> createParameters(@Body @Valid List<AssessmentParameterRequest> assessmentParameterRequests, Authentication authentication) {
        LOGGER.info("Admin: Create parameter");
        for (AssessmentParameterRequest assessmentParameter : assessmentParameterRequests) {
            assessmentMasterDataService.createAssessmentParameter(assessmentParameter);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> createQuestions(@Body @Valid List<QuestionRequest> questionRequests, Authentication authentication) {
        LOGGER.info("Admin: Create questions");
        for (QuestionRequest questionRequest : questionRequests) {
            assessmentMasterDataService.createAssessmentQuestions(questionRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topicReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> createTopicReference(@Body List<TopicReferencesRequest> topicReferencesRequests, Authentication authentication) {
        LOGGER.info("Admin: Create topic reference");
        for (TopicReferencesRequest topicReferencesRequest : topicReferencesRequests) {
            assessmentMasterDataService.createAssessmentTopicReferences(topicReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameterReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> createParameterReferences(@Body List<ParameterReferencesRequest> parameterReferencesRequests, Authentication authentication) {
        LOGGER.info("Admin: Create parameter reference");
        for (ParameterReferencesRequest parameterReferencesRequest : parameterReferencesRequests) {
            assessmentMasterDataService.createAssessmentParameterReferences(parameterReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Put(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body  @Valid AssessmentCategoryRequest assessmentCategoryRequest, Authentication authentication) {
        LOGGER.info("Admin: Update category: {}",categoryId);
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
        assessmentCategory.setActive(assessmentCategoryRequest.isActive());
        assessmentCategory.setComments(assessmentCategoryRequest.getComments());
        assessmentMasterDataService.updateCategory(assessmentCategory,assessmentCategoryRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/modules/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> updateModule(@PathVariable("moduleId") Integer moduleId, @Body @Valid AssessmentModuleRequest assessmentModuleRequest, Authentication authentication) {
        LOGGER.info("Admin: Update module: {}",moduleId);
        assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topics/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> updateTopic(@PathVariable("topicId") Integer topicId, @Body  @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("Admin: Update topic: {}",topicId);
        assessmentMasterDataService.updateTopic(topicId, assessmentTopicRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameters/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("Admin: Update parameter: {}",parameterId);
        assessmentMasterDataService.updateParameter(parameterId, assessmentParameterRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> updateQuestion(@PathVariable("questionId") Integer questionId, QuestionRequest questionRequest, Authentication authentication) {
        LOGGER.info("Admin: Update question: {}",questionId);
        assessmentMasterDataService.updateQuestion(questionId, questionRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topicReferences/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameterReferences/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest, Authentication authentication) {
        assessmentMasterDataService.updateParameterReferences(referenceId, parameterReferencesRequest);
        return HttpResponse.ok();

    }

    @Get(value = "/assessments/{startDate}/{endDate}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @AdminAuth
    public HttpResponse<AdminAssessmentResponse> getAssessmentsCount(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, Authentication authentication) throws ParseException {
        LOGGER.info("Admin: Get assessment from {} to {}",startDate,endDate);
        AdminAssessmentResponse adminAssessmentResponse = new AdminAssessmentResponse();
        List<Assessment> allAssessments = assessmentService.getTotalAssessments(startDate, endDate);
        List<Assessment> activeAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Active).collect(Collectors.toList());
        List<Assessment> completedAssessments = allAssessments.stream().filter(assessment -> assessment.getAssessmentStatus() == AssessmentStatus.Completed).collect(Collectors.toList());
        adminAssessmentResponse.setTotalAssessments(allAssessments.size());
        adminAssessmentResponse.setTotalActiveAssessments(activeAssessments.size());
        adminAssessmentResponse.setTotalCompleteAssessments(completedAssessments.size());
        return HttpResponse.ok(adminAssessmentResponse);
    }

    private AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }


}

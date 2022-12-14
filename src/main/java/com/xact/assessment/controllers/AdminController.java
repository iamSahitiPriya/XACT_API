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
import java.util.List;

@Introspected
@AdminAuth
@Controller("/v1/admin")
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
        LOGGER.info("Admin: Create category");
        AssessmentCategory assessmentCategory1 = assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
        CategoryDto categoryDto = mapper.map(assessmentCategory1, CategoryDto.class);
            return HttpResponse.ok(categoryDto);
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> createAssessmentModule(@Body @Valid AssessmentModuleRequest assessmentModule, Authentication authentication) {
        LOGGER.info("Admin: Create module");
            AssessmentModule assessmentModule1=assessmentMasterDataService.createAssessmentModule(assessmentModule);
            ModuleResponse moduleResponse = mapper.map(assessmentModule1,ModuleResponse.class);
            moduleResponse.setCategoryId(assessmentModule1.getCategory().getCategoryId());
        return HttpResponse.ok(moduleResponse);
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> createTopics(@Body @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("Admin: Create topics");
        AssessmentTopic assessmentTopic = assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
        TopicResponse topicResponse = mapper.map(assessmentTopic,TopicResponse.class);
        topicResponse.setModuleId(assessmentTopic.getModule().getModuleId());
        topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());

        return HttpResponse.ok(topicResponse);
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> createParameters(@Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("Admin: Create parameter");

            AssessmentParameter assessmentParameter=assessmentMasterDataService.createAssessmentParameter(assessmentParameterRequest);
            ParameterResponse parameterResponse=mapper.map(assessmentParameter,ParameterResponse.class);
            parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
            parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
            parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
        return HttpResponse.ok(parameterResponse);
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
    public HttpResponse<AssessmentTopicReferenceDto> createTopicReference(@Body TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        LOGGER.info("Admin: Create topic reference");
        AssessmentTopicReference assessmentTopicReference = assessmentMasterDataService.createAssessmentTopicReferences(topicReferencesRequest);
        AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference,AssessmentTopicReferenceDto.class);
        return HttpResponse.ok(assessmentTopicReferenceDto);
    }

    @Post(value = "/parameterReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> createParameterReferences(@Body ParameterReferencesRequest parameterReferencesRequests, Authentication authentication) {
        LOGGER.info("Admin: Create parameter reference");
        AssessmentParameterReference assessmentParameterReference = assessmentMasterDataService.createAssessmentParameterReferences(parameterReferencesRequests);
        AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
        return HttpResponse.ok(assessmentParameterReferenceDto);
    }

    @Put(value = "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<CategoryDto> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body  @Valid AssessmentCategoryRequest assessmentCategoryRequest, Authentication authentication) {
        LOGGER.info("Admin: Update category: {}",categoryId);
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
        assessmentCategory.setActive(assessmentCategoryRequest.isActive());
        assessmentCategory.setComments(assessmentCategoryRequest.getComments());
        AssessmentCategory assessmentCategory1 = assessmentMasterDataService.updateCategory(assessmentCategory,assessmentCategoryRequest);
        CategoryDto categoryDto = mapper.map(assessmentCategory1, CategoryDto.class);
        return HttpResponse.ok(categoryDto);
    }

    @Put(value = "/modules/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ModuleResponse> updateModule(@PathVariable("moduleId") Integer moduleId, @Body @Valid AssessmentModuleRequest assessmentModuleRequest, Authentication authentication) {
        LOGGER.info("Admin: Update module: {}", moduleId);
        AssessmentModule assessmentModule=assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest);
        ModuleResponse moduleResponse=mapper.map(assessmentModule,ModuleResponse.class);
        moduleResponse.setCategoryId(assessmentModule.getCategory().getCategoryId());
        moduleResponse.setUpdatedAt(assessmentModule.getUpdatedAt());
        return HttpResponse.ok(moduleResponse);
    }

    @Put(value = "/topics/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicResponse> updateTopic(@PathVariable("topicId") Integer topicId, @Body  @Valid AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        LOGGER.info("Admin: Update topic: {}",topicId);
        AssessmentTopic assessmentTopic = assessmentMasterDataService.updateTopic(topicId, assessmentTopicRequest);
        TopicResponse topicResponse = mapper.map(assessmentTopic,TopicResponse.class);
        topicResponse.setUpdatedAt(assessmentTopic.getUpdatedAt());
        topicResponse.setCategoryId(assessmentTopic.getModule().getCategory().getCategoryId());
        return HttpResponse.ok(topicResponse);
    }

    @Put(value = "/parameters/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterResponse> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body @Valid AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        LOGGER.info("Admin: Update parameter: {}", parameterId);
        AssessmentParameter assessmentParameter=assessmentMasterDataService.updateParameter(parameterId, assessmentParameterRequest);
        ParameterResponse parameterResponse=mapper.map(assessmentParameter,ParameterResponse.class);
        parameterResponse.setModuleId(assessmentParameter.getTopic().getModule().getModuleId());
        parameterResponse.setTopicId(assessmentParameter.getTopic().getTopicId());
        parameterResponse.setCategoryId(assessmentParameter.getTopic().getModule().getCategory().getCategoryId());
        return HttpResponse.ok(parameterResponse);
    }

    @Put(value = "/questions/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> updateQuestion(@PathVariable("questionId") Integer questionId, QuestionRequest questionRequest, Authentication authentication) {
        LOGGER.info("Admin: Update question: {}", questionId);
        assessmentMasterDataService.updateQuestion(questionId, questionRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topicReferences/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReferenceDto> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        AssessmentTopicReference assessmentTopicReference = assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
        AssessmentTopicReferenceDto assessmentTopicReferenceDto = mapper.map(assessmentTopicReference,AssessmentTopicReferenceDto.class);
        return HttpResponse.ok(assessmentTopicReferenceDto);
    }

    @Put(value = "/parameterReferences/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReferenceDto> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest, Authentication authentication) {
        AssessmentParameterReference assessmentParameterReference = assessmentMasterDataService.updateParameterReferences(referenceId, parameterReferencesRequest);
        AssessmentParameterReferenceDto assessmentParameterReferenceDto = mapper.map(assessmentParameterReference, AssessmentParameterReferenceDto.class);
        return HttpResponse.ok(assessmentParameterReferenceDto);

    }

    @Delete(value="topicReferences/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<TopicReferencesRequest> deleteTopicReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("Admin: Delete topic reference. referenceId: {}" , referenceId);
        assessmentMasterDataService.deleteTopicReference(referenceId);
        return  HttpResponse.ok();
    }

    @Delete(value="parameterReferences/{referenceId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<ParameterReferencesRequest> deleteParameterReference(@PathVariable("referenceId") Integer referenceId, Authentication authentication) {
        LOGGER.info("Admin: Delete parameter reference. referenceId: {}" , referenceId);
        assessmentMasterDataService.deleteParameterReference(referenceId);
        return  HttpResponse.ok();
    }

    @Get(value = "/assessments/{startDate}/{endDate}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AdminAssessmentResponse> getAssessmentsCount(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, Authentication authentication) throws ParseException {
        LOGGER.info("Admin: Get assessment from {} to {}", startDate, endDate);
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

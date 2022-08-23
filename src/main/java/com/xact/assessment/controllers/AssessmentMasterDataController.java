/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
<<<<<<< HEAD
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
=======
import io.micronaut.http.annotation.*;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
<<<<<<< HEAD
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller("/v1/assessment-master-data")
public class AssessmentMasterDataController {
<<<<<<< HEAD

    private static ModelMapper mapper = new ModelMapper();
=======
    private final Logger LOGGER = LoggerFactory.getLogger(AssessmentMasterDataController.class);


    private static final ModelMapper mapper = new ModelMapper();
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

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

<<<<<<< HEAD
    private AssessmentMasterDataService assessmentMasterDataService;
=======
    private final AssessmentMasterDataService assessmentMasterDataService;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

    public AssessmentMasterDataController(AssessmentMasterDataService assessmentMasterDataService) {
        this.assessmentMasterDataService = assessmentMasterDataService;
    }

    @Get(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AssessmentCategoryDto>> getAssessmentMasterData() {
<<<<<<< HEAD
=======
        LOGGER.info("Get assessment master data");
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();
        List<AssessmentCategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(mapper.map(assessmentCategory, AssessmentCategoryDto.class)));
        }
        return HttpResponse.ok(assessmentCategoriesResponse);
    }

<<<<<<< HEAD
=======
    @Post(value = "/category", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> createAssessmentCategory(@Body AssessmentCategoryRequest assessmentCategory) {
        assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
        return HttpResponse.ok();
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> createAssessmentModule(@Body List<AssessmentModuleRequest> assessmentModules) {
        for (AssessmentModuleRequest assessmentModule : assessmentModules) {
            assessmentMasterDataService.createAssessmentModule(assessmentModule);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> createTopics(@Body List<AssessmentTopicRequest> assessmentTopicRequests) {
        for (AssessmentTopicRequest assessmentTopicRequest : assessmentTopicRequests) {
            assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> createParameters(@Body List<AssessmentParameterRequest> assessmentParameterRequests) {
        for (AssessmentParameterRequest assessmentParameter : assessmentParameterRequests) {
            assessmentMasterDataService.createAssessmentParameter(assessmentParameter);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> createQuestions(@Body List<QuestionRequest> questionRequests) {
        for (QuestionRequest questionRequest : questionRequests) {
            assessmentMasterDataService.createAssessmentQuestions(questionRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topicReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> createTopicReference(@Body List<TopicReferencesRequest> topicReferencesRequests) {
        for (TopicReferencesRequest topicReferencesRequest : topicReferencesRequests) {
            assessmentMasterDataService.createAssessmentTopicReferences(topicReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameterReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> createParameterReferences(@Body List<ParameterReferencesRequest> parameterReferencesRequests) {
        for (ParameterReferencesRequest parameterReferencesRequest : parameterReferencesRequests) {
            assessmentMasterDataService.createAssessmentParameterReferences(parameterReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Put(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body AssessmentCategoryRequest assessmentCategoryRequest) {
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
        assessmentCategory.setActive(assessmentCategoryRequest.isActive());
        assessmentMasterDataService.updateCategory(assessmentCategory);
        return HttpResponse.ok();
    }

    @Put(value = "/module/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> updateModule(@PathVariable("moduleId") Integer moduleId, @Body AssessmentModuleRequest assessmentModuleRequest) {
        assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topic/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> updateTopic(@PathVariable("topicId") Integer topicId, @Body AssessmentTopicRequest assessmentTopicRequest) {
        assessmentMasterDataService.updateTopic(topicId, assessmentTopicRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameter/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body AssessmentParameterRequest assessmentParameterRequest) {
        assessmentMasterDataService.updateParameter(parameterId, assessmentParameterRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/question/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> updateQuestion(@PathVariable("questionId") Integer questionId, QuestionRequest questionRequest) {
        assessmentMasterDataService.updateQuestion(questionId, questionRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topicReference/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest) {
        assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameterReference/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest) {
        assessmentMasterDataService.updateParameterReferences(referenceId, parameterReferencesRequest);
        return HttpResponse.ok();

    }

    private AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }

>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
}

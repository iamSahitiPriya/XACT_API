/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

@Introspected
@Controller("/v1/assessment-master-data")
public class AssessmentMasterDataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssessmentMasterDataController.class);


    private static final ModelMapper moduleMapper = new ModelMapper();
    private static final ModelMapper mapper = new ModelMapper();

    static {
        PropertyMap<AssessmentModule, AssessmentModuleDto> moduleMapOnly = new PropertyMap<>() {
            protected void configure() {
                map().setCategory(source.getCategory().getCategoryId());
                map().setTopics(new TreeSet<>());
            }
        };
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
        moduleMapper.addMappings(moduleMapOnly);
    }

    private final AssessmentMasterDataService assessmentMasterDataService;

    public AssessmentMasterDataController(AssessmentMasterDataService assessmentMasterDataService) {
        this.assessmentMasterDataService = assessmentMasterDataService;

    }

    @Get(value = "{assessmentId}/categories/all", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserAssessmentResponse> getCategories(@PathVariable("assessmentId") Integer assessmentId) {
        LOGGER.info("Get all category & assessment master data");
        List<AssessmentCategory> userAssessmentCategories = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(userAssessmentCategories)) {
            userAssessmentCategories.forEach(assessmentCategory -> userAssessmentCategoriesResponse.add(moduleMapper.map(assessmentCategory, AssessmentCategoryDto.class)));
        }
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();
        List<AssessmentCategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(moduleMapper.map(assessmentCategory, AssessmentCategoryDto.class)));
        }
        UserAssessmentResponse userAssessmentResponse = new UserAssessmentResponse();
        userAssessmentResponse.setAssessmentCategories(assessmentCategoriesResponse);
        userAssessmentResponse.setUserAssessmentCategories(userAssessmentCategoriesResponse);
        return HttpResponse.ok(userAssessmentResponse);
    }

    @Get(value = "{assessmentId}/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<UserAssessmentResponse> getSelectedCategories(@PathVariable("assessmentId") Integer assessmentId) {
        LOGGER.info("Get selected categories only");
        List<AssessmentCategory> userAssessmentCategories = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
        List<AssessmentCategoryDto> userAssessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(userAssessmentCategories)) {
            userAssessmentCategories.forEach(assessmentCategory -> userAssessmentCategoriesResponse.add(mapper.map(assessmentCategory, AssessmentCategoryDto.class)));
        }
        UserAssessmentResponse userAssessmentResponse = new UserAssessmentResponse();
        userAssessmentResponse.setUserAssessmentCategories(userAssessmentCategoriesResponse);
        return HttpResponse.ok(userAssessmentResponse);
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
}
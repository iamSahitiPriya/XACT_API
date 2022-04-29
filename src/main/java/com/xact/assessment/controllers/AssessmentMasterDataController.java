package com.xact.assessment.controllers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller("/v1/assessment-master-data")
public class AssessmentMasterDataController {

    private static ModelMapper mapper = new ModelMapper();

    private AssessmentMasterDataService assessmentMasterDataService;

    public AssessmentMasterDataController(AssessmentMasterDataService assessmentMasterDataService) {
        this.assessmentMasterDataService = assessmentMasterDataService;
    }

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

    @Get(value = "/categories", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AssessmentCategoryDto>> getAssessmentMasterData() {
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();
        List<AssessmentCategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(mapper.map(assessmentCategory, AssessmentCategoryDto.class)));
        }
        return HttpResponse.ok(assessmentCategoriesResponse);
    }
}
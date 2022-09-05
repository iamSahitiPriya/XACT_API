package com.xact.assessment.controllers;

import com.xact.assessment.annotations.AdminAuth;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Introspected
@AdminAuth
@Controller("/v1/admin")
public class AdminController {
    private static final ModelMapper mapper = new ModelMapper();

    private final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

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

    public AdminController(AssessmentMasterDataService assessmentMasterDataService) {
        this.assessmentMasterDataService = assessmentMasterDataService;
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
    @Post(value = "/category", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> createAssessmentCategory(@Body AssessmentCategoryRequest assessmentCategory, Authentication authentication) {
        assessmentMasterDataService.createAssessmentCategory(assessmentCategory);
        return HttpResponse.ok();
    }

    @Post(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> createAssessmentModule(@Body List<AssessmentModuleRequest> assessmentModules, Authentication authentication) {
        for (AssessmentModuleRequest assessmentModule : assessmentModules) {
            assessmentMasterDataService.createAssessmentModule(assessmentModule);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> createTopics(@Body List<AssessmentTopicRequest> assessmentTopicRequests, Authentication authentication) {
        for (AssessmentTopicRequest assessmentTopicRequest : assessmentTopicRequests) {
            assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> createParameters(@Body List<AssessmentParameterRequest> assessmentParameterRequests, Authentication authentication) {
        for (AssessmentParameterRequest assessmentParameter : assessmentParameterRequests) {
            assessmentMasterDataService.createAssessmentParameter(assessmentParameter);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/questions", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> createQuestions(@Body List<QuestionRequest> questionRequests, Authentication authentication) {
        for (QuestionRequest questionRequest : questionRequests) {
            assessmentMasterDataService.createAssessmentQuestions(questionRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/topicReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> createTopicReference(@Body List<TopicReferencesRequest> topicReferencesRequests, Authentication authentication) {
        for (TopicReferencesRequest topicReferencesRequest : topicReferencesRequests) {
            assessmentMasterDataService.createAssessmentTopicReferences(topicReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Post(value = "/parameterReferences", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> createParameterReferences(@Body List<ParameterReferencesRequest> parameterReferencesRequests, Authentication authentication) {
        for (ParameterReferencesRequest parameterReferencesRequest : parameterReferencesRequests) {
            assessmentMasterDataService.createAssessmentParameterReferences(parameterReferencesRequest);
        }
        return HttpResponse.ok();
    }

    @Put(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentCategory> updateCategory(@PathVariable("categoryId") Integer categoryId, @Body AssessmentCategoryRequest assessmentCategoryRequest, Authentication authentication) {
        AssessmentCategory assessmentCategory = getCategory(categoryId);
        assessmentCategory.setCategoryName(assessmentCategoryRequest.getCategoryName());
        assessmentCategory.setActive(assessmentCategoryRequest.isActive());
        assessmentMasterDataService.updateCategory(assessmentCategory);
        return HttpResponse.ok();
    }

    @Put(value = "/module/{moduleId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentModule> updateModule(@PathVariable("moduleId") Integer moduleId, @Body AssessmentModuleRequest assessmentModuleRequest, Authentication authentication) {
        assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topic/{topicId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopic> updateTopic(@PathVariable("topicId") Integer topicId, @Body AssessmentTopicRequest assessmentTopicRequest, Authentication authentication) {
        assessmentMasterDataService.updateTopic(topicId, assessmentTopicRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameter/{parameterId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameter> updateParameter(@PathVariable("parameterId") Integer parameterId, @Body AssessmentParameterRequest assessmentParameterRequest, Authentication authentication) {
        assessmentMasterDataService.updateParameter(parameterId, assessmentParameterRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/question/{questionId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<Question> updateQuestion(@PathVariable("questionId") Integer questionId, QuestionRequest questionRequest, Authentication authentication) {
        assessmentMasterDataService.updateQuestion(questionId, questionRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/topicReference/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentTopicReference> updateTopicReference(@PathVariable("referenceId") Integer referenceId, TopicReferencesRequest topicReferencesRequest, Authentication authentication) {
        assessmentMasterDataService.updateTopicReference(referenceId, topicReferencesRequest);
        return HttpResponse.ok();
    }

    @Put(value = "/parameterReference/{referenceId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<AssessmentParameterReference> updateParameterReference(@PathVariable("referenceId") Integer referenceId, ParameterReferencesRequest parameterReferencesRequest, Authentication authentication) {
        assessmentMasterDataService.updateParameterReferences(referenceId, parameterReferencesRequest);
        return HttpResponse.ok();

    }

    private AssessmentCategory getCategory(Integer categoryId) {
        return assessmentMasterDataService.getCategory(categoryId);
    }
}

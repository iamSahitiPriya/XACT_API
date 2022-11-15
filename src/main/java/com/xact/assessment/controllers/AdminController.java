/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.*;

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
    public HttpResponse<List<CategoryDto>> getCategories(Authentication authentication) {
        LOGGER.info("Get all category data");
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getCategories();
        List<CategoryDto> assessmentCategoriesResponse = new ArrayList<>();
        if (Objects.nonNull(assessmentCategories)) {
            assessmentCategories.forEach(assessmentCategory -> assessmentCategoriesResponse.add(mapper.map(assessmentCategory, CategoryDto.class)));
        }
        return HttpResponse.ok(assessmentCategoriesResponse);
    }

    @Get(value = "/modules", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AdminDataResponse>> getModules(Authentication authentication) throws JsonProcessingException {
        LOGGER.info("Get all modules data");
        List<AdminDataResponse> dataResponses = new ArrayList<>();
        Map<CategoryDto,List<ModuleDto>> categoryMap = new LinkedHashMap<>();
        List<AssessmentModule> assessmentModules = assessmentMasterDataService.getModules();

        if (Objects.nonNull(assessmentModules)) {
            for(AssessmentModule assessmentModule : assessmentModules) {
                getModulesWithCategory(categoryMap, assessmentModule);
            }

            dataResponses = getDataResponse(categoryMap);
        }
        return HttpResponse.ok(dataResponses);
    }

    @Get(value = "/topics", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AdminDataResponse>> getTopics(Authentication authentication) {
        LOGGER.info("Get all topics data");
        List<AdminDataResponse> dataResponses = new ArrayList<>();
        Map<AssessmentModule,List<TopicDto>> topicMap = new LinkedHashMap<>();
        Map<CategoryDto, HashMap<ModuleDto, List<TopicDto>>> categoryMap = new LinkedHashMap<>();
        List<AssessmentTopic> assessmentTopics = assessmentMasterDataService.getTopics();

        if (Objects.nonNull(assessmentTopics)) {
            for(AssessmentTopic assessmentTopic : assessmentTopics) {
                AssessmentModule module = assessmentTopic.getModule();
                if(topicMap.containsKey(assessmentTopic.getModule())) {
                    List<TopicDto> topic = topicMap.get(module);
                    topic.add(mapper.map(assessmentTopic,TopicDto.class));
                    topicMap.put(module,topic);
                }
                else {
                    List<TopicDto> topic = new ArrayList<>();
                    topic.add(mapper.map(assessmentTopic,TopicDto.class));
                    topicMap.put(module,topic);
                }
            }
        }
        topicMap.forEach((module,topics) -> {
            CategoryDto categoryDto = mapper.map(module.getCategory(),CategoryDto.class);
            ModuleDto moduleDto = mapper.map(module,ModuleDto.class);
            if(categoryMap.containsKey(categoryDto)) {
                HashMap<ModuleDto, List<TopicDto>> modules = categoryMap.get(categoryDto);
                modules.put(moduleDto,topics);
                categoryMap.put(categoryDto,modules);
            }
            else {
                HashMap<ModuleDto,List<TopicDto>> modules = new LinkedHashMap<>();
                modules.put(moduleDto,topics);
                categoryMap.put(categoryDto,modules);
            }
        });
        categoryMap.forEach((category, modules) -> {
            AdminDataResponse adminDataResponse = new AdminDataResponse();
            adminDataResponse.setCategory(category);
            List<ModuleDto> moduleDtoList = new ArrayList<>();
            modules.forEach((module, topics) -> {
                ModuleDto moduleDto = mapper.map(module,ModuleDto.class);
                moduleDto.setTopics(topics);
                moduleDtoList.add(moduleDto);
            });
            adminDataResponse.setModules(moduleDtoList);
            dataResponses.add(adminDataResponse);
        });
        return HttpResponse.ok(dataResponses);
    }
    @Get(value = "/parameters", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<List<AdminDataResponse>> getParameters(Authentication authentication) {
        LOGGER.info("Get all topics data");
        List<AdminDataResponse> dataResponses = new ArrayList<>();
        List<AssessmentParameter> assessmentParameters = assessmentMasterDataService.getParameters();
         Map<AssessmentTopic,List<ParameterDto>> parameterMap=new LinkedHashMap<>();
         Map<AssessmentModule,List<TopicDto>> moduleMap=new LinkedHashMap<>();
         Map<CategoryDto,List<ModuleDto>> categoryMap=new LinkedHashMap<>();
        if (Objects.nonNull(assessmentParameters)) {
            for(AssessmentParameter assessmentParameter : assessmentParameters) {
                AssessmentTopic assessmentTopic=mapper.map(assessmentParameter.getTopic(),AssessmentTopic.class);
                if(parameterMap.containsKey(assessmentTopic)) {
                    List<ParameterDto> parameter = parameterMap.get(assessmentTopic);
                    parameter.add(mapper.map(assessmentParameter,ParameterDto.class));
                    parameterMap.put(assessmentTopic,parameter);
                }
                else {
                    List<ParameterDto> parameter=new ArrayList<>();
                    parameter.add(mapper.map(assessmentParameter,ParameterDto.class));
                    parameterMap.put(assessmentTopic,parameter);
                }
            }
        }
        parameterMap.forEach((topic,parameters)->{
            AssessmentModule assessmentModule=mapper.map(topic.getModule(),AssessmentModule.class);
            TopicDto topicDto=mapper.map(topic,TopicDto.class);
            topicDto.setParameters(parameters);

            if(moduleMap.containsKey(assessmentModule)){
                List<TopicDto> topics = moduleMap.get(assessmentModule);
                topics.add(topicDto);
                moduleMap.put(assessmentModule,topics);
            }
            else{
              List<TopicDto> topics=new ArrayList<>();
              topics.add(topicDto);
              moduleMap.put(assessmentModule,topics);
            }
        });
        moduleMap.forEach((module,topics)->{
            CategoryDto categoryDto=mapper.map(module.getCategory(),CategoryDto.class);
            ModuleDto moduleDto=mapper.map(module,ModuleDto.class);
            moduleDto.setTopics(topics);
            if(categoryMap.containsKey(categoryDto)){
                List<ModuleDto> modules=categoryMap.get(categoryDto);
                modules.add(moduleDto);
                categoryMap.put(categoryDto,modules);

            }else{
                List<ModuleDto> modules=new ArrayList<>();
                modules.add(moduleDto);
                categoryMap.put(categoryDto,modules);
            }
        });
        categoryMap.forEach((category,modules)->{
            AdminDataResponse adminDataResponse=new AdminDataResponse();
            adminDataResponse.setCategory(category);
            adminDataResponse.setModules(modules);
            dataResponses.add(adminDataResponse);

        });
        return HttpResponse.ok(dataResponses);
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


    private void getModulesWithCategory(Map<CategoryDto, List<ModuleDto>> categoryMap, AssessmentModule assessmentModule) {
        CategoryDto categoryDto = mapper.map(assessmentModule.getCategory(),CategoryDto.class);
        if(categoryMap.containsKey(categoryDto)) {
            List<ModuleDto> module = categoryMap.get(categoryDto);
            module.add(mapper.map(assessmentModule,ModuleDto.class));
            categoryMap.put(categoryDto,module);
        }
        else {
            List<ModuleDto> module = new ArrayList<>();
            module.add(mapper.map(assessmentModule,ModuleDto.class));
            categoryMap.put(categoryDto,module);
        }
    }

    private List<AdminDataResponse> getDataResponse(Map<CategoryDto, List<ModuleDto>> categoryMap) {
        List<AdminDataResponse> dataResponses = new ArrayList<>();
        categoryMap.forEach((category, modules) -> {
            AdminDataResponse adminModuleResponse = new AdminDataResponse();
            adminModuleResponse.setCategory(category);
            adminModuleResponse.setModules(modules);
            dataResponses.add(adminModuleResponse);
        });
        return dataResponses;
    }

}

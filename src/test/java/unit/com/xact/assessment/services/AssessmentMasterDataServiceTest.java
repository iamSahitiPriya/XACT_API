/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentCategoryRequest;
import com.xact.assessment.dtos.AssessmentModuleRequest;
import com.xact.assessment.dtos.AssessmentParameterRequest;
import com.xact.assessment.dtos.AssessmentTopicRequest;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ModuleService moduleService = mock(ModuleService.class);
    private final TopicService topicService = mock(TopicService.class);
    private final ParameterService parameterService = mock(ParameterService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository = mock(AssessmentTopicReferenceRepository.class);
    private final AssessmentParameterReferenceRepository assessmentParameterReferenceRepository = mock(AssessmentParameterReferenceRepository.class);

    private final AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryRepository, moduleService, questionService,assessmentTopicReferenceRepository, parameterService,topicService,assessmentParameterReferenceRepository);

    @Test
    void getAllCategories() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();

        assertEquals(assessmentCategories, allCategories);

        verify(categoryRepository).findAll();
    }

    @Test
    void shouldCreateCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryId(1);
        categoryRequest.setCategoryName("Dummy category");
        categoryRequest.setActive(false);
        assessmentMasterDataService.createAssessmentCategory(categoryRequest);
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryRequest.getCategoryId(),categoryRequest.getCategoryName(),categoryRequest.isActive());

        when(categoryRepository.save(assessmentCategory)).thenReturn(assessmentCategory);
        verify(categoryRepository).save(assessmentCategory);
    }

    @Test
    void shouldCreateModule() {
        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleId(1);
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);
        AssessmentCategory category = new AssessmentCategory(1,"Dummy",false);
        when(categoryRepository.findCategoryById(assessmentModuleRequest.getCategory())).thenReturn(category);


        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleId(),assessmentModuleRequest.getModuleName(),category,assessmentModuleRequest.isActive());
        assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest);
        verify(moduleService).createModule(assessmentModule);
    }

    @Test
    void shouldCreateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicId(1);
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicId(),topicRequest.getTopicName(),assessmentModule);

        topicService.createTopic(assessmentTopic);
        verify(topicService).createTopic(assessmentTopic);

    }

    @Test
    void shouldCreateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterId(1);
        parameterRequest.setParameterName("parameter");
        parameterRequest.setTopic(1);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentParameter assessmentParameter = new AssessmentParameter(parameterRequest.getParameterId(),parameterRequest.getParameterName(),assessmentTopic);

        parameterService.createParameter(assessmentParameter);
        verify(parameterService).createParameter(assessmentParameter);
    }
}

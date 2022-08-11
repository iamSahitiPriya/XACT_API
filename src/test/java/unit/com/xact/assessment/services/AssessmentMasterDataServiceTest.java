/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldCreateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionId(1);
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question(questionRequest.getQuestionId(),questionRequest.getQuestionText(),assessmentParameter);

        questionService.createQuestion(question);
        verify(questionService).createQuestion(question);
    }

    @Test
    void shouldCreateTopicReferences() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(Rating.valueOf("TWO"));
        topicReferencesRequest.setReferenceId(1);

        AssessmentTopic topic = new AssessmentTopic();
        when(topicService.getTopic(1)).thenReturn(Optional.of(topic));
        assessmentMasterDataService.createAssessmentTopicReferences(topicReferencesRequest);
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(topic,topicReferencesRequest.getRating(), topicReferencesRequest.getReference());

        when(assessmentTopicReferenceRepository.save(assessmentTopicReference)).thenReturn(assessmentTopicReference);
        verify(assessmentTopicReferenceRepository).save(assessmentTopicReference);

    }

    @Test
    void shouldCreateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(Rating.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));
        assessmentMasterDataService.createAssessmentParameterReferences(parameterReferencesRequest);
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter,parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());

        when(assessmentParameterReferenceRepository.save(assessmentParameterReference)).thenReturn(assessmentParameterReference);
        verify(assessmentParameterReferenceRepository).save(assessmentParameterReference);
    }
}

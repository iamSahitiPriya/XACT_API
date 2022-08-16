/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        categoryRequest.setCategoryName("Dummy category");
        categoryRequest.setActive(false);
        List<AssessmentCategory> categories = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categories);
        assessmentMasterDataService.createAssessmentCategory(categoryRequest);
        AssessmentCategory assessmentCategory = new AssessmentCategory(1,categoryRequest.getCategoryName(),categoryRequest.isActive(),categoryRequest.getComments());

        when(categoryRepository.save(assessmentCategory)).thenReturn(assessmentCategory);
        verify(categoryRepository).save(assessmentCategory);
    }

    @Test
    void shouldCreateModule() {
        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);
        AssessmentCategory category = new AssessmentCategory(1,"Dummy",false,"");
        when(categoryRepository.findCategoryById(assessmentModuleRequest.getCategory())).thenReturn(category);
        AssessmentModule assessmentModule = new AssessmentModule(1,assessmentModuleRequest.getModuleName(),category,assessmentModuleRequest.isActive(),assessmentModuleRequest.getComments());
        List<AssessmentModule> assessmentModules  = new ArrayList<>();
        when(moduleService.getAllModules()).thenReturn(assessmentModules);
        assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest);
        verify(moduleService).createModule(assessmentModule);
    }

    @Test
    void shouldCreateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicId(1);
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicId(),topicRequest.getTopicName(),assessmentModule,topicRequest.isActive(),topicRequest.getComments());
        List<AssessmentTopic> topics = new ArrayList<>();
        when(topicService.getAllTopics()).thenReturn(topics);
        when(moduleService.getModule(1)).thenReturn(assessmentModule);
        assessmentMasterDataService.createAssessmentTopics(topicRequest);
        verify(topicService).createTopic(assessmentTopic);

    }

    @Test
    void shouldCreateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterId(1);
        parameterRequest.setParameterName("parameter");
        parameterRequest.setTopic(1);
        parameterRequest.setActive(false);
        parameterRequest.setComments("");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentParameter assessmentParameter = new AssessmentParameter(parameterRequest.getParameterId(),parameterRequest.getParameterName(),assessmentTopic,parameterRequest.isActive(),parameterRequest.getComments());
        List<AssessmentParameter> parameters = new ArrayList<>();
        when(parameterService.getAllParameters()).thenReturn(parameters);
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        assessmentMasterDataService.createAssessmentParameter(parameterRequest);
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
        List<Question> questions = new ArrayList<>();
        Integer parameterId = 1;
        when(questionService.getAllQuestion()).thenReturn(questions);
        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        assessmentMasterDataService.createAssessmentQuestions(questionRequest);
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

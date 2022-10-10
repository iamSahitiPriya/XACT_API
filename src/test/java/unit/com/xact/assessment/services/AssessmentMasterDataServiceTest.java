/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final ModuleService moduleService = mock(ModuleService.class);
    private final TopicService topicService = mock(TopicService.class);
    private final ParameterService parameterService = mock(ParameterService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final ModuleRepository moduleRepository = mock(ModuleRepository.class);
    private final UserAssessmentModuleRepository userAssessmentModuleRepository = mock(UserAssessmentModuleRepository.class);
    private final AssessmentTopicReferenceRepository assessmentTopicReferenceRepository = mock(AssessmentTopicReferenceRepository.class);
    private final AssessmentParameterReferenceRepository assessmentParameterReferenceRepository = mock(AssessmentParameterReferenceRepository.class);
    private final AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryRepository, moduleService, questionService, assessmentTopicReferenceRepository, parameterService, topicService, userAssessmentModuleRepository, assessmentParameterReferenceRepository);

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
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryRequest.getCategoryName(), categoryRequest.isActive(), categoryRequest.getComments());

        when(categoryRepository.save(assessmentCategory)).thenReturn(assessmentCategory);
        verify(categoryRepository).save(assessmentCategory);
    }

    @Test
    void shouldCreateModule() {
        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);
        AssessmentCategory category = new AssessmentCategory("Dummy", false, "");
        when(categoryRepository.findCategoryById(assessmentModuleRequest.getCategory())).thenReturn(category);
        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), category, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
        List<AssessmentModule> assessmentModules = new ArrayList<>();
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
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicName(), assessmentModule, topicRequest.isActive(), topicRequest.getComments());
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
        AssessmentParameter assessmentParameter = new AssessmentParameter(parameterRequest.getParameterName(), assessmentTopic, parameterRequest.isActive(), parameterRequest.getComments());
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
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
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
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(topic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());

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
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());

        when(assessmentParameterReferenceRepository.save(assessmentParameterReference)).thenReturn(assessmentParameterReference);
        verify(assessmentParameterReferenceRepository).save(assessmentParameterReference);
    }

    @Test
    void shouldUpdateCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("Dummy category");
        categoryRequest.setActive(false);
        List<AssessmentCategory> categories = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categories);
        assessmentMasterDataService.createAssessmentCategory(categoryRequest);
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryRequest.getCategoryName(), categoryRequest.isActive(), categoryRequest.getComments());
        assessmentCategory.setCategoryName("this is a category");
        assessmentMasterDataService.updateCategory(assessmentCategory, categoryRequest);
        verify(categoryRepository).update(assessmentCategory);
    }

    @Test
    void shouldUpdateModule() {
        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);
        AssessmentCategory category = new AssessmentCategory("Dummy", false, "");
        when(categoryRepository.findCategoryById(assessmentModuleRequest.getCategory())).thenReturn(category);
        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), category, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
        List<AssessmentModule> assessmentModules = new ArrayList<>();
        when(moduleService.getAllModules()).thenReturn(assessmentModules);
        when(moduleRepository.update(assessmentModule)).thenReturn(assessmentModule);
        assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest);

        AssessmentModuleRequest assessmentModule1 = new AssessmentModuleRequest();
        assessmentModule1.setModuleName("This is an updated module");
        when(moduleService.getModule(1)).thenReturn(assessmentModule);

        assessmentMasterDataService.updateModule(1,assessmentModuleRequest);
        verify(moduleService).updateModule(assessmentModule);
    }

    @Test
    void shouldUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicId(1);
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicName(), assessmentModule, topicRequest.isActive(), topicRequest.getComments());
        List<AssessmentTopic> topics = new ArrayList<>();
        when(topicService.getAllTopics()).thenReturn(topics);
        when(moduleService.getModule(1)).thenReturn(assessmentModule);
        assessmentMasterDataService.createAssessmentTopics(topicRequest);

        AssessmentTopicRequest assessmentTopicRequest = new AssessmentTopicRequest();
        assessmentTopicRequest.setTopicName("This is an updated topic name");
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        assessmentMasterDataService.updateTopic(1,assessmentTopicRequest);
        verify(topicService).updateTopic(assessmentTopic);
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterId(1);
        parameterRequest.setParameterName("parameter");
        parameterRequest.setTopic(1);
        parameterRequest.setActive(false);
        parameterRequest.setComments("");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentParameter assessmentParameter = new AssessmentParameter(parameterRequest.getParameterName(), assessmentTopic, parameterRequest.isActive(), parameterRequest.getComments());

        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setParameterName("this is an updated parameter");
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        assessmentMasterDataService.updateParameter(1,assessmentParameterRequest);
        verify(parameterService).updateParameter(assessmentParameter);
    }

    @Test
    void shouldUpdateQuestions() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionId(1);
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);

        QuestionRequest questionRequest1 = new QuestionRequest();
        questionRequest1.setParameter(1);
        questionRequest1.setQuestionText("This is an updated question");
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        when(questionService.getQuestion(1)).thenReturn(Optional.of(question));
        assessmentMasterDataService.updateQuestion(1,questionRequest1);
        verify(questionService).updateQuestion(question);

    }

    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(Rating.valueOf("TWO"));
        topicReferencesRequest.setReferenceId(1);

        AssessmentTopic topic = new AssessmentTopic();
        when(topicService.getTopic(1)).thenReturn(Optional.of(topic));

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("This is an updated references");
        referencesRequest.setTopic(1);
        when(assessmentTopicReferenceRepository.findById(1)).thenReturn(Optional.of(assessmentTopicReference));
        assessmentMasterDataService.updateTopicReference(1,referencesRequest);
        verify(assessmentTopicReferenceRepository).update(assessmentTopicReference);
    }

    @Test
    void shouldUpdateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(Rating.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));

        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setParameter(1);
        referencesRequest.setReference("UPDATE REFERENCE");
        when(assessmentParameterReferenceRepository.findById(1)).thenReturn(Optional.of(parameterReference));
        assessmentMasterDataService.updateParameterReferences(1,referencesRequest);
        verify(assessmentParameterReferenceRepository).update(parameterReference);
    }

    @Test
    void shouldGetUserAssessmentCategories() {
        Integer assessmentId = 1;
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("category1");
        List<AssessmentModule> assessmentModules = new ArrayList<>();
        AssessmentModule module = new AssessmentModule();
        module.setActive(true);
        module.setModuleName("module1");
        module.setModuleId(1);
        module.setCategory(category);
        Set<AssessmentModule> validModules = Collections.singleton(module);
        category.setModules(validModules);
        assessmentModules.add(module);
        when(userAssessmentModuleRepository.findModuleByAssessment(assessmentId)).thenReturn(assessmentModules);

        List<AssessmentCategory> actualAssessmentCategories = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);

        assertEquals(actualAssessmentCategories.size(),1);
        assertEquals(actualAssessmentCategories.get(0).getCategoryName(),"category1");
    }

    @Test
    void getUserAssessmentCategories() {
      Integer assessmentId =1;
      Assessment assessment=new Assessment();
      assessment.setAssessmentId(assessmentId);
      Set<AssessmentModule> assessmentModuleSet=new HashSet<>();
      List<AssessmentModule> assessmentModules1=new ArrayList<>();

      AssessmentCategory assessmentCategory=new AssessmentCategory();
      assessmentCategory.setCategoryId(1);
      assessmentCategory.setActive(true);

      AssessmentModule assessmentModule1=new AssessmentModule();
      assessmentModule1.setModuleId(1);
      assessmentModule1.setActive(true);
      assessmentModule1.setCategory(assessmentCategory);
      assessmentModuleSet.add(assessmentModule1);
        assessmentCategory.setModules(assessmentModuleSet);
        assessmentModules1.add(assessmentModule1);


      when(userAssessmentModuleRepository.findModuleByAssessment(assessmentId)).thenReturn(assessmentModules1);

      when(categoryRepository.findCategoryById(1)).thenReturn(assessmentCategory);

      assessmentMasterDataService.getUserAssessmentCategories(assessmentId);

      verify(userAssessmentModuleRepository).findModuleByAssessment(assessmentId);
      verify(categoryRepository).findCategoryById(assessmentModule1.getCategory().getCategoryId());

    }
}

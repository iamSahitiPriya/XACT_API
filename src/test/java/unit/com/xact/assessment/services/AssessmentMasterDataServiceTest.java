/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.*;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleRepository;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.xact.assessment.dtos.ContributorQuestionStatus.PUBLISHED;
import static com.xact.assessment.models.AssessmentStatus.Active;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private final CategoryService categoryService = mock(CategoryService.class);
    private final TopicService topicService = mock(TopicService.class);
    private final ModuleService moduleService = mock(ModuleService.class);
    private final ParameterService parameterService = mock(ParameterService.class);
    private final QuestionService questionService = mock(QuestionService.class);
    private final ModuleRepository moduleRepository = mock(ModuleRepository.class);
    private final UserAssessmentModuleService userAssessmentModuleService = mock(UserAssessmentModuleService.class);
    private final ModuleService moduleService1 = new ModuleService(moduleRepository);
    private final AccessControlService accessControlService = mock(AccessControlService.class);
    private final ModuleContributorService moduleContributorService = mock(ModuleContributorService.class);
    private final AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryService, moduleService, questionService, parameterService, topicService, userAssessmentModuleService, accessControlService, moduleContributorService);

    @Test
    void getAllCategories() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(categoryService.getAllCategories()).thenReturn(allCategories);

        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();

        assertEquals(assessmentCategories, allCategories);

        verify(categoryService).getAllCategories();
    }

    @Test
    void shouldCreateCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("Dummy category");
        categoryRequest.setActive(false);
        List<AssessmentCategory> categories = new ArrayList<>();
        when(categoryService.getCategoriesSortedByUpdatedDate()).thenReturn(categories);
        assessmentMasterDataService.createAssessmentCategory(categoryRequest);
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryRequest.getCategoryName(), categoryRequest.isActive(), categoryRequest.getComments());

        doNothing().when(categoryService).save(assessmentCategory);
        verify(categoryService).save(assessmentCategory);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsCreatedWithDuplicateName() throws DuplicateRecordException {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("category");
        assessmentCategory.setActive(true);
        assessmentCategory.setComments("comments");


        List<AssessmentCategory> assessmentCategories = new ArrayList<>();
        assessmentCategories.add(assessmentCategory);
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(assessmentCategory.getCategoryName());

        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("category");
        categoryRequest.setActive(false);

        when(categoryService.getCategoriesSortedByUpdatedDate()).thenReturn(assessmentCategories);
        when(categoryService.getCategoryNames()).thenReturn(categoryNames);
        AssessmentCategory assessmentCategory1 = new AssessmentCategory(categoryRequest.getCategoryName(), categoryRequest.isActive(), categoryRequest.getComments());

        doNothing().when(categoryService).save(assessmentCategory1);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentCategory(categoryRequest));

    }


    @Test
    void shouldCreateModule() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("Dummy");
        category.setCategoryId(1);
        category.setActive(true);
        AssessmentModule assessmentModule1 = new AssessmentModule(1, "new nodule", category, true, "");
        category.setModules(Collections.singleton(assessmentModule1));

        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);

        when(categoryService.findCategoryById(assessmentModuleRequest)).thenReturn(category);

        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), category, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());

        assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest);
        verify(moduleService).createModule(assessmentModule);
    }

    @Test
    void shouldCreateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentTopic assessmentTopic = new AssessmentTopic(topicRequest.getTopicName(), assessmentModule, topicRequest.isActive(), topicRequest.getComments());
        AssessmentTopic assessmentTopic1 = new AssessmentTopic(2, "new topic", assessmentModule, true, "");
        assessmentModule.setTopics(Collections.singleton(assessmentTopic1));
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
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(1, "new parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter1));
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterName(parameterRequest.getParameterName()).topic(assessmentTopic).isActive(parameterRequest.isActive()).comments(parameterRequest.getComments()).build();
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        assessmentMasterDataService.createAssessmentParameter(parameterRequest);
        verify(parameterService).createParameter(assessmentParameter);
    }

    @Test
    void shouldCreateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);
        List<Question> questions = new ArrayList<>();
        Integer parameterId = 1;
        when(questionService.getAllQuestions()).thenReturn(questions);
        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));

        assessmentMasterDataService.createAssessmentQuestion("hello@thoughtworks.com",questionRequest);
        verify(questionService).createQuestion("hello@thoughtworks.com",question);
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
        assessmentMasterDataService.createAssessmentTopicReference(topicReferencesRequest);
        AssessmentTopicReference assessmentTopicReference1 = new AssessmentTopicReference(topic, topicReferencesRequest.getRating(), topicReferencesRequest.getReference());

        doNothing().when(topicService).saveTopicReference(assessmentTopicReference1);
        verify(topicService).saveTopicReference(assessmentTopicReference1);

    }

    @Test
    void shouldCreateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(Rating.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));
        assessmentMasterDataService.createAssessmentParameterReference(parameterReferencesRequest);
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter, parameterReferencesRequest.getRating(), parameterReferencesRequest.getReference());

        doNothing().when(parameterService).saveParameterReference(assessmentParameterReference);
        verify(parameterService).saveParameterReference(assessmentParameterReference);
    }

    @Test
    void shouldUpdateCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("Dummy category");
        categoryRequest.setActive(false);
        List<AssessmentCategory> categories = new ArrayList<>();
        when(categoryService.getCategoriesSortedByUpdatedDate()).thenReturn(categories);
        assessmentMasterDataService.createAssessmentCategory(categoryRequest);
        AssessmentCategory assessmentCategory = new AssessmentCategory(categoryRequest.getCategoryName(), categoryRequest.isActive(), categoryRequest.getComments());
        assessmentCategory.setCategoryName("this is a category");
        assessmentMasterDataService.updateCategory(assessmentCategory, categoryRequest);
        verify(categoryService).update(assessmentCategory);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsUpdatesWithDuplicateName() throws DuplicateRecordException {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("category");
        assessmentCategory.setActive(true);
        assessmentCategory.setComments("comments");

        AssessmentCategory assessmentCategory1 = new AssessmentCategory(2, "secondCategory", true, "");

        List<AssessmentCategory> assessmentCategories = new ArrayList<>();
        assessmentCategories.add(assessmentCategory);
        assessmentCategories.add(assessmentCategory1);
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add(assessmentCategory.getCategoryName());
        categoryNames.add(assessmentCategory1.getCategoryName());

        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("category");
        categoryRequest.setActive(false);

        when(categoryService.getCategoriesSortedByUpdatedDate()).thenReturn(assessmentCategories);
        when(categoryService.getCategoryNames()).thenReturn(categoryNames);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateCategory(assessmentCategory1, categoryRequest));

    }

    @Test
    void shouldUpdateModule() {
        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("Dummy module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);
        AssessmentCategory category = new AssessmentCategory("Dummy", false, "");
        when(categoryService.findCategoryById(assessmentModuleRequest)).thenReturn(category);
        AssessmentModule assessmentModule = new AssessmentModule(assessmentModuleRequest.getModuleName(), category, assessmentModuleRequest.isActive(), assessmentModuleRequest.getComments());
        AssessmentModule assessmentModule1 = new AssessmentModule(2, "new module", category, true, "");
        category.setModules(Collections.singleton(assessmentModule1));
        when(moduleRepository.update(assessmentModule)).thenReturn(assessmentModule);
        assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest);

        AssessmentModuleRequest assessmentModule2 = new AssessmentModuleRequest();
        assessmentModule2.setModuleName("This is an updated module");
        when(moduleService.getModule(1)).thenReturn(assessmentModule);

        assessmentMasterDataService.updateModule(1, assessmentModuleRequest);
        verify(moduleService).updateModule(assessmentModule);
    }

    @Test
    void shouldThrowExceptionWhenModuleIsCreatedWithDuplicateName() throws DuplicateRecordException {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("category");
        assessmentCategory.setActive(true);
        assessmentCategory.setComments("comments");


        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleName("module");
        assessmentModule.setCategory(assessmentCategory);
        assessmentModule.setActive(true);
        assessmentModule.setComments("comments");

        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);

        Set<AssessmentModule> assessmentModules = new HashSet<>();
        assessmentModules.add(assessmentModule);
        assessmentCategory.setModules(assessmentModules);

        when(categoryService.findCategoryById(assessmentModuleRequest)).thenReturn(assessmentCategory);
        doNothing().when(moduleService).createModule(assessmentModule);
        when(moduleRepository.save(assessmentModule)).thenReturn(assessmentModule);
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);


        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentModule(assessmentModuleRequest));

    }

    @Test
    void shouldThrowExceptionWhenModuleIsUpdatedWithDuplicateName() throws DuplicateRecordException {
        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "comments");
        AssessmentModule assessmentModule = new AssessmentModule(1, "module", assessmentCategory, true, "comments");
        AssessmentModule assessmentModule1 = new AssessmentModule(2, "module1", assessmentCategory, true, "comments");

        AssessmentModuleRequest assessmentModuleRequest = new AssessmentModuleRequest();
        assessmentModuleRequest.setModuleName("module");
        assessmentModuleRequest.setActive(false);
        assessmentModuleRequest.setCategory(1);

        Set<AssessmentModule> modules = new HashSet<>();
        modules.add(assessmentModule);
        modules.add(assessmentModule1);
        assessmentCategory.setModules(modules);

        Integer moduleId = assessmentModule1.getModuleId();
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);
        when(moduleService.getModule(moduleId)).thenReturn(assessmentModule1);
        when(categoryService.findCategoryById(assessmentModuleRequest)).thenReturn(assessmentCategory);
        doNothing().when(moduleService).updateModule(assessmentModule);
        when(moduleRepository.update(assessmentModule)).thenReturn(assessmentModule);


        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateModule(moduleId, assessmentModuleRequest));
    }


    @Test
    void shouldUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("topic");
        topicRequest.setModule(1);
        topicRequest.setComments("");
        topicRequest.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule("module", null, true, "");
        AssessmentTopic assessmentTopic = new AssessmentTopic("new topic", assessmentModule, topicRequest.isActive(), topicRequest.getComments());
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        when(moduleService.getModule(topicRequest.getModule())).thenReturn(assessmentModule);
        assessmentMasterDataService.updateTopic(1, topicRequest);
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
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterName(parameterRequest.getParameterName()).topic(assessmentTopic).isActive(parameterRequest.isActive()).comments(parameterRequest.getComments()).build();
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(2, "new parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter1));

        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setParameterName("this is an updated parameter");
        when(topicService.getTopic(1)).thenReturn(Optional.of(assessmentTopic));
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        assessmentMasterDataService.updateParameter(1, assessmentParameterRequest);
        verify(parameterService).updateParameter(assessmentParameter);
    }

    @Test
    void shouldUpdateQuestions() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("hello");
        questionRequest.setParameter(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question(questionRequest.getQuestionText(), assessmentParameter);

        QuestionRequest questionRequest1 = new QuestionRequest();
        questionRequest1.setParameter(1);
        questionRequest1.setQuestionText("This is an updated question");
        when(parameterService.getParameter(1)).thenReturn(Optional.of(assessmentParameter));
        when(questionService.getQuestion(1)).thenReturn(Optional.of(question));
        assessmentMasterDataService.updateQuestion(1, questionRequest1);
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

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(topic, Rating.TWO, "new reference");
        topic.setReferences(Collections.singleton(assessmentTopicReference));


        when(topicService.getAssessmentTopicReference(1)).thenReturn(assessmentTopicReference);
        assessmentMasterDataService.updateTopicReference(1, topicReferencesRequest);
        verify(topicService).updateTopicReference(assessmentTopicReference);
    }

    @Test
    void shouldUpdateParameterReference() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(Rating.valueOf("TWO"));

        AssessmentParameter parameter = new AssessmentParameter();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter, Rating.TWO, "new reference");
        parameter.setReferences(Collections.singleton(assessmentParameterReference));
        when(parameterService.getParameter(1)).thenReturn(Optional.of(parameter));

        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setParameter(1);
        referencesRequest.setReference("UPDATE REFERENCE");
        when(parameterService.getAssessmentParameterReference(1)).thenReturn(assessmentParameterReference);
        assessmentMasterDataService.updateParameterReference(1, referencesRequest);
        verify(parameterService).updateParameterReference(assessmentParameterReference);
    }

    @Test
    void shouldGetUserAssessmentCategories() {
        Integer assessmentId = 1;
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("category1");
        category.setActive(true);
        List<AssessmentModule> assessmentModules = new ArrayList<>();
        AssessmentModule module = new AssessmentModule();
        module.setActive(true);
        module.setModuleName("module1");
        module.setModuleId(1);
        module.setCategory(category);
        Set<AssessmentModule> validModules = Collections.singleton(module);
        category.setModules(validModules);
        assessmentModules.add(module);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topicName", module, true, "");
        Set<AssessmentTopic> validTopics = Collections.singleton(assessmentTopic);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("parameterName");
        assessmentParameter.setActive(true);
        assessmentParameter.setTopic(assessmentTopic);
        Question question=new Question();
        question.setQuestionStatus(PUBLISHED);
        assessmentParameter.setQuestions(Collections.singleton(question));
        Set<AssessmentParameter> validParameters = Collections.singleton(assessmentParameter);
        module.setTopics(validTopics);
        assessmentTopic.setParameters(validParameters);

        when(userAssessmentModuleService.findModuleByAssessment(assessmentId)).thenReturn(assessmentModules);

        List<AssessmentCategory> actualAssessmentCategories = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);

        assertEquals(1, actualAssessmentCategories.size());
        assertEquals("category1", actualAssessmentCategories.get(0).getCategoryName());
    }

    @Test
    void getUserAssessmentCategories() {
        Integer assessmentId = 1;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Set<AssessmentModule> assessmentModuleSet = new HashSet<>();
        List<AssessmentModule> assessmentModules1 = new ArrayList<>();

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule1 = new AssessmentModule();
        assessmentModule1.setModuleId(1);
        assessmentModule1.setActive(true);
        assessmentModule1.setCategory(assessmentCategory);
        assessmentModuleSet.add(assessmentModule1);
        assessmentCategory.setModules(assessmentModuleSet);
        assessmentModules1.add(assessmentModule1);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topicName", assessmentModule1, true, "");
        Set<AssessmentTopic> validTopics = Collections.singleton(assessmentTopic);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question=new Question();
        question.setQuestionStatus(PUBLISHED);
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("parameterName");
        assessmentParameter.setActive(true);
        assessmentParameter.setTopic(assessmentTopic);
        assessmentParameter.setQuestions(Collections.singleton(question));
        Set<AssessmentParameter> validParameters = Collections.singleton(assessmentParameter);
        assessmentModule1.setTopics(validTopics);
        assessmentTopic.setParameters(validParameters);


        when(userAssessmentModuleService.findModuleByAssessment(assessmentId)).thenReturn(assessmentModules1);


        assessmentMasterDataService.getUserAssessmentCategories(assessmentId);

        verify(userAssessmentModuleService).findModuleByAssessment(assessmentId);

    }

    @Test
    void deleteTopicReference() {
        doNothing().when(topicService).deleteTopicReference(1);

        assessmentMasterDataService.deleteTopicReference(1);

        verify(topicService).deleteTopicReference(1);
    }

    @Test
    void deleteParameterReference() {
        doNothing().when(parameterService).deleteParameterReference(1);

        assessmentMasterDataService.deleteParameterReference(1);

        verify(parameterService).deleteParameterReference(1);
        ;
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheTopicIsAlreadyPresent() {
        AssessmentTopicRequest assessmentTopicRequest = new AssessmentTopicRequest();
        assessmentTopicRequest.setTopicName("topic");
        assessmentTopicRequest.setModule(1);
        assessmentTopicRequest.setActive(true);
        assessmentTopicRequest.setComments("comments");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topic", assessmentModule, true, "");
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentTopics(assessmentTopicRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheParameterIsAlreadyPresent() {
        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setParameterName("parameter");
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setActive(true);
        assessmentParameterRequest.setComments("comments");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        AssessmentParameter assessmentParameter = new AssessmentParameter(1, "parameter", assessmentTopic, null, null, true, new Date(), new Date(), "", 1);
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentParameter(assessmentParameterRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheParameterReferenceIsAlreadyPresent() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference");
        parameterReferencesRequest.setRating(Rating.ONE);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setReferences(Collections.singleton(new AssessmentParameterReference(assessmentParameter, Rating.ONE, "reference")));
        assessmentParameter.setParameterId(1);
        when(parameterService.getParameter(assessmentParameter.getParameterId())).thenReturn(Optional.of(assessmentParameter));

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentParameterReference(parameterReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheTopicReferenceIsAlreadyPresent() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference");
        topicReferencesRequest.setRating(Rating.ONE);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setReferences(Collections.singleton(new AssessmentTopicReference(assessmentTopic, Rating.ONE, "reference")));
        assessmentTopic.setTopicId(1);
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.createAssessmentTopicReference(topicReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedTopicNameIsAlreadyPresent() {
        AssessmentTopicRequest assessmentTopicRequest = new AssessmentTopicRequest();
        assessmentTopicRequest.setTopicName("topic");
        assessmentTopicRequest.setModule(1);
        assessmentTopicRequest.setActive(true);
        assessmentTopicRequest.setComments("comments");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        AssessmentTopic assessmentTopic = new AssessmentTopic(1, "topic", assessmentModule, true, "");
        AssessmentTopic assessmentTopic1 = new AssessmentTopic(2, "topic2", assessmentModule, true, "");
        when(moduleService.getModule(assessmentModule.getModuleId())).thenReturn(assessmentModule);
        when(topicService.getTopic(assessmentTopic1.getTopicId())).thenReturn(Optional.of(assessmentTopic1));
        Set<AssessmentTopic> assessmentTopics = new HashSet<>();
        assessmentTopics.add(assessmentTopic);
        assessmentTopics.add(assessmentTopic1);
        assessmentModule.setTopics(assessmentTopics);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateTopic(2, assessmentTopicRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedParameterNameIsAlreadyPresent() {
        AssessmentParameterRequest assessmentParameterRequest = new AssessmentParameterRequest();
        assessmentParameterRequest.setParameterName("parameter");
        assessmentParameterRequest.setTopic(1);
        assessmentParameterRequest.setActive(true);
        assessmentParameterRequest.setComments("comments");

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        AssessmentParameter assessmentParameter = new AssessmentParameter(1, "parameter", null, null, null, true, new Date(), new Date(), "", 1);
        AssessmentParameter assessmentParameter1 = new AssessmentParameter(2, "new parameter", null, null, null, true, new Date(), new Date(), "", 1);

        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));
        when(parameterService.getParameter(assessmentParameter1.getParameterId())).thenReturn(Optional.of(assessmentParameter1));
        Set<AssessmentParameter> parameters = new HashSet<>();
        parameters.add(assessmentParameter);
        parameters.add(assessmentParameter1);
        assessmentTopic.setParameters(parameters);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateParameter(2, assessmentParameterRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedParameterReferenceIsAlreadyPresent() {
        ParameterReferencesRequest parameterReferencesRequest = new ParameterReferencesRequest();
        parameterReferencesRequest.setParameter(1);
        parameterReferencesRequest.setReference("reference1");
        parameterReferencesRequest.setRating(Rating.TWO);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter, Rating.ONE, "reference");
        assessmentParameterReference.setReferenceId(1);
        AssessmentParameterReference assessmentParameterReference1 = new AssessmentParameterReference(assessmentParameter, Rating.TWO, "reference1");
        assessmentParameterReference1.setReferenceId(2);
        Set<AssessmentParameterReference> assessmentParameterReferences = new HashSet<>();
        assessmentParameterReferences.add(assessmentParameterReference);
        assessmentParameterReferences.add(assessmentParameterReference1);
        assessmentParameter.setReferences(assessmentParameterReferences);
        assessmentParameter.setParameterId(1);
        when(parameterService.getParameter(assessmentParameter.getParameterId())).thenReturn(Optional.of(assessmentParameter));
        when(parameterService.getAssessmentParameterReference(assessmentParameterReference.getReferenceId())).thenReturn(assessmentParameterReference);

        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateParameterReference(1, parameterReferencesRequest));
    }

    @Test
    void shouldThrowDuplicateRecordExceptionWhenTheUpdatedTopicReferenceIsAlreadyPresent() {
        TopicReferencesRequest topicReferencesRequest = new TopicReferencesRequest();
        topicReferencesRequest.setTopic(1);
        topicReferencesRequest.setReference("reference1");
        topicReferencesRequest.setRating(Rating.TWO);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic, Rating.ONE, "reference");
        assessmentTopicReference.setReferenceId(1);
        AssessmentTopicReference assessmentTopicReference1 = new AssessmentTopicReference(assessmentTopic, Rating.TWO, "reference1");
        assessmentTopicReference1.setReferenceId(2);
        assessmentTopic.setTopicId(1);
        Set<AssessmentTopicReference> assessmentTopicReferences = new HashSet<>();
        assessmentTopicReferences.add(assessmentTopicReference);
        assessmentTopicReferences.add(assessmentTopicReference1);
        assessmentTopic.setReferences(assessmentTopicReferences);
        when(topicService.getTopic(assessmentTopic.getTopicId())).thenReturn(Optional.of(assessmentTopic));
        when(topicService.getAssessmentTopicReference(assessmentTopicReference.getReferenceId())).thenReturn(assessmentTopicReference);


        assertThrows(DuplicateRecordException.class, () -> assessmentMasterDataService.updateTopicReference(1, topicReferencesRequest));
    }

    @Test
    void shouldGetAssessedModules() {
        Integer assessmentId = 1;
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, Active, created1, updated1);

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic1);

        ParameterLevelId parameterLevelId = new ParameterLevelId();
        parameterLevelId.setAssessment(assessment1);
        parameterLevelId.setParameter(assessmentParameter);
        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        parameterLevelRating.setRating(1);

        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment1);
        topicLevelId.setTopic(assessmentTopic);
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(2);

        Integer expectedValue = moduleService1.getAssessedModules(Collections.singletonList(topicLevelRating), Collections.singletonList(parameterLevelRating));

        Integer actualResponse = 1;

        assertEquals(expectedValue, actualResponse);
    }

    @Test
    void shouldGetAssessedCategory() {
        Integer assessmentId = 1;
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, Active, created1, updated1);

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic1);

        ParameterLevelId parameterLevelId = new ParameterLevelId();
        parameterLevelId.setAssessment(assessment1);
        parameterLevelId.setParameter(assessmentParameter);
        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        parameterLevelRating.setRating(1);

        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment1);
        topicLevelId.setTopic(assessmentTopic);
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(2);


        Integer expectedResponse = assessmentMasterDataService.getAssessedCategory(Collections.singletonList(topicLevelRating), Collections.singletonList(parameterLevelRating));
        Integer actualResponse = 1;

        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    void shouldReturnTrueWhenUserAssessmentModuleExists() {
        Date created = new Date(2022 - 7 - 13);
        Date updated = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(1, "It", "industry", "domain", 3);
        Assessment assessment = new Assessment(1, "assessmentName", "Client Assessment", organisation, AssessmentStatus.Active, created, updated);

        UserAssessmentModule userAssessmentModule = new UserAssessmentModule();

        AssessmentModule assessmentModule = new AssessmentModule();
        AssessmentModuleId assessmentModuleId = new AssessmentModuleId();
        assessmentModuleId.setAssessment(assessment);
        assessmentModuleId.setModule(assessmentModule);
        userAssessmentModule.setAssessmentModuleId(assessmentModuleId);
        userAssessmentModule.setModule(assessmentModule);
        userAssessmentModule.setAssessment(assessment);

        when(userAssessmentModuleService.existsById(assessmentModuleId)).thenReturn(true);


        Assertions.assertTrue(assessmentMasterDataService.isModuleSelectedByUser(assessment, assessmentModule));

    }

}

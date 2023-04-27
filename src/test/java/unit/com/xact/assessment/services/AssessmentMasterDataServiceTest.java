/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AssessmentCategoryRequest;
import com.xact.assessment.dtos.AssessmentModuleRequest;
import com.xact.assessment.dtos.CategoryDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.exceptions.DuplicateRecordException;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ModuleRepository;
import com.xact.assessment.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.xact.assessment.dtos.ContributorQuestionStatus.DRAFT;
import static com.xact.assessment.dtos.ContributorQuestionStatus.PUBLISHED;
import static com.xact.assessment.models.AccessControlRoles.AUTHOR;
import static com.xact.assessment.models.AccessControlRoles.Admin;
import static com.xact.assessment.models.AssessmentStatus.Active;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AssessmentMasterDataServiceTest {

    private final CategoryService categoryService = mock(CategoryService.class);
    private final ModuleService moduleService = mock(ModuleService.class);
    private final ModuleRepository moduleRepository = mock(ModuleRepository.class);
    private final UserAssessmentModuleService userAssessmentModuleService = mock(UserAssessmentModuleService.class);
    private final ModuleService moduleService1 = new ModuleService(moduleRepository);
    private final AccessControlService accessControlService = mock(AccessControlService.class);
    private final ModuleContributorService moduleContributorService = mock(ModuleContributorService.class);
    private final AssessmentMasterDataService assessmentMasterDataService = new AssessmentMasterDataService(categoryService, moduleService, userAssessmentModuleService, accessControlService, moduleContributorService);

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
    void shouldGetContributorMasterData() {
        User user = new User();
        user.setUserId("hello@thoughtworks.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("hello@thoughtworks.com");
        user.setUserInfo(userInfo);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("Category");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setCategory(category);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic");
        assessmentTopic.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter");
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(DRAFT);
        question.setParameter(assessmentParameter);

        AssessmentModule assessmentModule1 = new AssessmentModule();
        assessmentModule1.setModuleId(2);
        assessmentModule1.setModuleName("Module 2");
        assessmentModule1.setCategory(category);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setTopicName("Topic");
        assessmentTopic1.setModule(assessmentModule1);

        AssessmentParameter assessmentParameter1 = new AssessmentParameter();
        assessmentParameter1.setParameterId(2);
        assessmentParameter1.setParameterName("Parameter");
        assessmentParameter1.setTopic(assessmentTopic1);

        Question question1 = new Question();
        question1.setQuestionId(2);
        question1.setQuestionText("question1");
        question1.setQuestionStatus(PUBLISHED);
        question1.setParameter(assessmentParameter1);

        assessmentParameter.setQuestions(Collections.singleton(question));
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        assessmentParameter1.setQuestions(Collections.singleton(question1));
        assessmentTopic1.setParameters(Collections.singleton(assessmentParameter1));
        assessmentModule1.setTopics(Collections.singleton(assessmentTopic1));

        category.setModules(Set.of(assessmentModule, assessmentModule1));

        ModuleContributor moduleContributor1 = new ModuleContributor();
        ContributorId contributorId1 = new ContributorId();
        contributorId1.setModule(assessmentModule);
        contributorId1.setUserEmail("hello@thoughtworks.com");
        moduleContributor1.setContributorId(contributorId1);
        moduleContributor1.setContributorRole(ContributorRole.AUTHOR);

        ModuleContributor moduleContributor2 = new ModuleContributor();
        ContributorId contributorId2 = new ContributorId();
        contributorId2.setModule(assessmentModule1);
        contributorId2.setUserEmail("hello@thoughtworks.com");
        moduleContributor2.setContributorId(contributorId2);
        moduleContributor2.setContributorRole(ContributorRole.REVIEWER);

        when(moduleContributorService.getContributorsByEmail("hello@thoughtworks.com")).thenReturn(Set.of(moduleContributor1, moduleContributor2));
        when(accessControlService.getAccessControlRolesByEmail("hello@thoughtworks.com")).thenReturn(Optional.of(Admin));

        List<CategoryDto> categoryDtoList =assessmentMasterDataService.getMasterDataByRole(user,AUTHOR.toString());
        assertEquals(1,categoryDtoList.size());

    }

    @Test
    void shouldGetAdminMasterData() {
        User user = new User();
        user.setUserId("hello@thoughtworks.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("hello@thoughtworks.com");
        user.setUserInfo(userInfo);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("Category");

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setCategory(category);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Topic");
        assessmentTopic.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter");
        assessmentParameter.setTopic(assessmentTopic);

        Question question = new Question();
        question.setQuestionId(1);
        question.setQuestionText("question");
        question.setQuestionStatus(PUBLISHED);
        question.setParameter(assessmentParameter);

        assessmentParameter.setQuestions(Collections.singleton(question));
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        category.setModules(Collections.singleton(assessmentModule));

        ModuleContributor moduleContributor = new ModuleContributor();
        when(moduleContributorService.getContributorsByEmail("hello@thoughtworks.com")).thenReturn(Collections.singleton(moduleContributor));
        when(accessControlService.getAccessControlRolesByEmail("hello@thoughtworks.com")).thenReturn(Optional.of(Admin));
        when(categoryService.getCategoriesSortedByUpdatedDate()).thenReturn(Collections.singletonList(category));

        List<CategoryDto> categoryDtoList =assessmentMasterDataService.getMasterDataByRole(user,Admin.toString());
        assertEquals(1,categoryDtoList.size());

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

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AdminController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);
    AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final Authentication authentication = Mockito.mock(Authentication.class);

    AdminController adminController = new AdminController(assessmentMasterDataService, assessmentService);

    @Test
    void createAssessmentCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("This is a category");
        categoryRequest.setComments("comment");
        categoryRequest.setActive(false);

        AssessmentCategory category = new AssessmentCategory("hello", false, "");
        when(assessmentMasterDataService.createAssessmentCategory(categoryRequest)).thenReturn(category);

        HttpResponse<CategoryDto> categoryHttpResponse = adminController.createAssessmentCategory(categoryRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), categoryHttpResponse.getStatus());
    }


    @Test
    void createAssessmentModule() {
        AssessmentModuleRequest moduleRequest = new AssessmentModuleRequest();
        moduleRequest.setModuleName("This is a module");
        moduleRequest.setActive(false);
        AssessmentCategory category = new AssessmentCategory(1, "categoryName", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", category, true, "");
        when(assessmentMasterDataService.createAssessmentModule(moduleRequest)).thenReturn(assessmentModule);
        HttpResponse<ModuleResponse> actualResponse = adminController.createAssessmentModule(moduleRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Hello this is a topic");
        topicRequest.setActive(false);
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("Hello this is a topic");
        assessmentTopic.setActive(false);
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));

        when(assessmentMasterDataService.createAssessmentTopics(topicRequest)).thenReturn(assessmentTopic);
        HttpResponse<TopicResponse> actualResponse = adminController.createTopic(topicRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Parameter");
        parameterRequest.setActive(false);

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        when(assessmentMasterDataService.createAssessmentParameter(parameterRequest)).thenReturn(assessmentParameter);

        HttpResponse<ParameterResponse> actualResponse = adminController.createParameter(parameterRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

//    @Test
//    void createAssessmentQuestions() {
//        QuestionRequest questionRequest = new QuestionRequest();
//        questionRequest.setQuestionText("Test");
//        questionRequest.setParameter(1);
//        User user = new User();
//        String userEmail = "hello@thoughtworks.com";
//        UserInfo userInfo = new UserInfo();
//        userInfo.setEmail(userEmail);
//        user.setUserInfo(userInfo);
//
//        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
//        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
//        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
//        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
//        Question question = new Question("Text", parameter);
//
//        question.setQuestionStatus(ContributorQuestionStatus.PUBLISHED);
//
//        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
//        when(assessmentMasterDataService.createAssessmentQuestion(userEmail,questionRequest)).thenReturn(question);
//
//        HttpResponse<QuestionResponse> actualResponse = adminController.createQuestion(questionRequest, authentication);
//        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
//    }

    @Test
    void createAssessmentTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("references");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setTopic(1);

        when(assessmentMasterDataService.createAssessmentTopicReference(referencesRequest)).thenReturn(new AssessmentTopicReference(new AssessmentTopic(), Rating.FIVE, "reference"));

        HttpResponse<AssessmentTopicReferenceDto> actualResponse = adminController.createTopicReference(referencesRequest, authentication);
        assertEquals(actualResponse.getStatus(), HttpResponse.ok().getStatus());
    }

    @Test
    void createParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("References");
        referencesRequest.setRating(Rating.FIVE);
        referencesRequest.setParameter(1);

        when(assessmentMasterDataService.createAssessmentParameterReference(referencesRequest)).thenReturn(new AssessmentParameterReference(new AssessmentParameter(),Rating.FIVE,"reference"));

        HttpResponse<AssessmentParameterReferenceDto> actualResponse = adminController.createParameterReference(referencesRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void updateCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("category");
        Integer categoryId = 1;
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("this is a category");

        when(assessmentMasterDataService.getCategory(1)).thenReturn(assessmentCategory);
        when(assessmentMasterDataService.updateCategory(assessmentCategory, categoryRequest)).thenReturn(assessmentCategory);

        HttpResponse<CategoryDto> actualResponse = adminController.updateCategory(categoryId, categoryRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus().getCode(), actualResponse.getStatus().getCode());
    }

    @Test
    void shouldUpdateModule() {
        AssessmentModuleRequest moduleRequest = new AssessmentModuleRequest();
        moduleRequest.setModuleName("Module");
        Integer moduleId = 1;
        AssessmentCategory category = new AssessmentCategory(1, "categoryName", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", category, true, "");
        when(assessmentMasterDataService.updateModule(1, moduleRequest)).thenReturn(assessmentModule);


        HttpResponse actualResponse = adminController.updateModule(moduleId, moduleRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Module");
        Integer topicId = 1;
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("this is a module");
        assessmentTopic.setModule(new AssessmentModule("hello", new AssessmentCategory("hello", false, ""), false, ""));


        when(assessmentMasterDataService.updateTopic(1, topicRequest)).thenReturn(assessmentTopic);

        HttpResponse actualResponse = adminController.updateTopic(topicId, topicRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Module");

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter assessmentParameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).comments("").build();


        when(assessmentMasterDataService.updateParameter(assessmentParameter.getParameterId(), parameterRequest)).thenReturn(assessmentParameter);

        HttpResponse actualResponse = adminController.updateParameter(assessmentParameter.getParameterId(), parameterRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }


    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello");

        when(assessmentMasterDataService.updateTopicReference(referenceId, referencesRequest)).thenReturn(new AssessmentTopicReference(new AssessmentTopic(), Rating.FIVE, "reference"));

        HttpResponse actualResponse = adminController.updateTopicReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        parameterReference.setReference("Hello");

        when(assessmentMasterDataService.updateParameterReference(referenceId,referencesRequest)).thenReturn(new AssessmentParameterReference(new AssessmentParameter(),Rating.FIVE,"reference"));
        HttpResponse actualResponse = adminController.updateParameterReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldDeleteTopicReference() {
        Integer referenceId = 10;

        HttpResponse actualResponse = adminController.deleteTopicReference(referenceId, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(assessmentMasterDataService).deleteTopicReference(referenceId);
    }

    @Test
    void shouldDeleteParameterReference() {
        Integer referenceId = 10;

        HttpResponse actualResponse = adminController.deleteParameterReference(referenceId,authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
        verify(assessmentMasterDataService).deleteParameterReference(referenceId);
    }

    @Test
    void shouldGetTheTotalAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 01);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created1, updated1);
        Assessment assessment2 = new Assessment(2, "Name", "Client Assessment", organisation, AssessmentStatus.Completed, created2, updated2);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";


        when(assessmentService.getTotalAssessments(startDate, endDate)).thenReturn(assessments);

        HttpResponse actualResponse = adminController.getAssessmentsCount(startDate, endDate, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

        verify(assessmentService).getTotalAssessments(startDate, endDate);

    }

    @Test
    void shouldGetTheTotalActiveAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 01);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created1, updated1);
        Assessment assessment2 = new Assessment(2, "Name", "Client Assessment", organisation, AssessmentStatus.Completed, created2, updated2);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";
        when(assessmentService.getTotalAssessments(startDate, endDate)).thenReturn(assessments);

        HttpResponse<AdminAssessmentResponse> actualResponse = adminController.getAssessmentsCount(startDate, endDate, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldGetTheTotalCompleteAssessmentCount() throws ParseException {
        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Date created2 = new Date(2022 - 6 - 01);
        Date updated2 = new Date(2022 - 6 - 11);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created1, updated1);
        Assessment assessment2 = new Assessment(2, "Name", "Client Assessment", organisation, AssessmentStatus.Completed, created2, updated2);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);
        assessments.add(assessment2);

        String startDate = "2022-10-13";
        String endDate = "2022-05-13";

        when(assessmentService.getTotalAssessments(startDate, endDate)).thenReturn(assessments);

        HttpResponse actualResponse = adminController.getAssessmentsCount(startDate, endDate, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}

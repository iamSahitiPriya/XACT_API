/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AssessmentMasterDataController;
<<<<<<< HEAD
import com.xact.assessment.dtos.AssessmentCategoryDto;
import com.xact.assessment.models.AssessmentCategory;
=======
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< HEAD
=======
import static org.mockito.Mockito.verify;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
import static org.mockito.Mockito.when;

class AssessmentMasterDataControllerTest {

    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);

    AssessmentMasterDataController assessmentMasterDataController = new AssessmentMasterDataController(assessmentMasterDataService);

    @Test
    void getAssessmentMasterData() {
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryId(3);
        category.setCategoryName("My category");
        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(assessmentMasterDataService.getAllCategories()).thenReturn(allCategories);

        HttpResponse<List<AssessmentCategoryDto>> assessmentMasterDataResponse = assessmentMasterDataController.getAssessmentMasterData();

        AssessmentCategoryDto firstAssessmentCategory = assessmentMasterDataResponse.body().get(0);
        assertEquals(firstAssessmentCategory.getCategoryId(), category.getCategoryId());
        assertEquals(firstAssessmentCategory.getCategoryName(), category.getCategoryName());
    }
<<<<<<< HEAD
=======

    @Test
    void createAssessmentCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("This is a category");
        categoryRequest.setComments("comment");
        categoryRequest.setActive(false);

        HttpResponse<AssessmentCategory> categoryHttpResponse = assessmentMasterDataController.createAssessmentCategory(categoryRequest);
        assertEquals(HttpResponse.ok().getStatus(), categoryHttpResponse.getStatus());
    }

    @Test
    void createAssessmentModule() {
        AssessmentModuleRequest moduleRequest = new AssessmentModuleRequest();
        moduleRequest.setModuleName("This is a module");
        moduleRequest.setActive(false);
        List<AssessmentModuleRequest> moduleRequests = Collections.singletonList(moduleRequest);

        HttpResponse<AssessmentModule> actualResponse = assessmentMasterDataController.createAssessmentModule(moduleRequests);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void createAssessmentTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Hello this is a topic");
        topicRequest.setActive(false);
        List<AssessmentTopicRequest> assessmentTopicRequests = Collections.singletonList(topicRequest);

        HttpResponse<AssessmentTopic> actualResponse = assessmentMasterDataController.createTopics(assessmentTopicRequests);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Parameter");
        parameterRequest.setActive(false);
        List<AssessmentParameterRequest> assessmentParameterRequests = Collections.singletonList(parameterRequest);

        HttpResponse<AssessmentParameter> actualResponse = assessmentMasterDataController.createParameters(assessmentParameterRequests);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentQuestions() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("Test");
        List<QuestionRequest> questionRequests = Collections.singletonList(questionRequest);

        HttpResponse<Question> actualResponse = assessmentMasterDataController.createQuestions(questionRequests);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("references");
        List<TopicReferencesRequest> referencesRequests = Collections.singletonList(referencesRequest);

        HttpResponse<AssessmentTopicReference> actualResponse = assessmentMasterDataController.createTopicReference(referencesRequests);
        assertEquals(actualResponse.getStatus(), HttpResponse.ok().getStatus());
    }

    @Test
    void createParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("References");
        List<ParameterReferencesRequest> referencesRequests = Collections.singletonList(referencesRequest);

        HttpResponse<AssessmentParameterReference> actualResponse = assessmentMasterDataController.createParameterReferences(referencesRequests);

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

        HttpResponse actualResponse = assessmentMasterDataController.updateCategory(categoryId, categoryRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateModule() {
        AssessmentModuleRequest moduleRequest = new AssessmentModuleRequest();
        moduleRequest.setModuleName("Module");
        Integer moduleId = 1;
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("this is a module");

        HttpResponse actualResponse = assessmentMasterDataController.updateModule(moduleId, moduleRequest);

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

        HttpResponse actualResponse = assessmentMasterDataController.updateTopic(topicId, topicRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Module");
        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("this is a module");

        HttpResponse actualResponse = assessmentMasterDataController.updateParameter(parameterId, parameterRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("question");
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionText("this is a question");

        HttpResponse actualResponse = assessmentMasterDataController.updateQuestion(questionId, questionRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello");

        HttpResponse actualResponse = assessmentMasterDataController.updateTopicReference(referenceId, referencesRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
    @Test
    void shouldUpdateParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        parameterReference.setReference("Hello");

        HttpResponse actualResponse = assessmentMasterDataController.updateParameterReference(referenceId, referencesRequest);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
}

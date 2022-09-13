package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AdminController;
import com.xact.assessment.controllers.AssessmentMasterDataController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    AssessmentMasterDataService assessmentMasterDataService = Mockito.mock(AssessmentMasterDataService.class);
    private final Authentication authentication = Mockito.mock(Authentication.class);

    AdminController adminController = new AdminController(assessmentMasterDataService);

    @Test
    void createAssessmentCategory() {
        AssessmentCategoryRequest categoryRequest = new AssessmentCategoryRequest();
        categoryRequest.setCategoryName("This is a category");
        categoryRequest.setComments("comment");
        categoryRequest.setActive(false);

        HttpResponse<AssessmentCategory> categoryHttpResponse = adminController.createAssessmentCategory(categoryRequest, authentication);
        assertEquals(HttpResponse.ok().getStatus(), categoryHttpResponse.getStatus());
    }

    @Test
    void createAssessmentModule() {
        AssessmentModuleRequest moduleRequest = new AssessmentModuleRequest();
        moduleRequest.setModuleName("This is a module");
        moduleRequest.setActive(false);
        List<AssessmentModuleRequest> moduleRequests = Collections.singletonList(moduleRequest);

        HttpResponse<AssessmentModule> actualResponse = adminController.createAssessmentModule(moduleRequests,authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void createAssessmentTopic() {
        AssessmentTopicRequest topicRequest = new AssessmentTopicRequest();
        topicRequest.setTopicName("Hello this is a topic");
        topicRequest.setActive(false);
        List<AssessmentTopicRequest> assessmentTopicRequests = Collections.singletonList(topicRequest);

        HttpResponse<AssessmentTopic> actualResponse = adminController.createTopics(assessmentTopicRequests, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentParameter() {
        AssessmentParameterRequest parameterRequest = new AssessmentParameterRequest();
        parameterRequest.setParameterName("Parameter");
        parameterRequest.setActive(false);
        List<AssessmentParameterRequest> assessmentParameterRequests = Collections.singletonList(parameterRequest);

        HttpResponse<AssessmentParameter> actualResponse = adminController.createParameters(assessmentParameterRequests, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentQuestions() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("Test");
        List<QuestionRequest> questionRequests = Collections.singletonList(questionRequest);

        HttpResponse<Question> actualResponse = adminController.createQuestions(questionRequests, authentication);
        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void createAssessmentTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("references");
        List<TopicReferencesRequest> referencesRequests = Collections.singletonList(referencesRequest);

        HttpResponse<AssessmentTopicReference> actualResponse = adminController.createTopicReference(referencesRequests, authentication);
        assertEquals(actualResponse.getStatus(), HttpResponse.ok().getStatus());
    }

    @Test
    void createParameterReferences() {
        ParameterReferencesRequest referencesRequest = new ParameterReferencesRequest();
        referencesRequest.setReference("References");
        List<ParameterReferencesRequest> referencesRequests = Collections.singletonList(referencesRequest);

        HttpResponse<AssessmentParameterReference> actualResponse = adminController.createParameterReferences(referencesRequests, authentication);

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

        HttpResponse actualResponse = adminController.updateCategory(categoryId, categoryRequest, authentication);

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

        HttpResponse actualResponse = adminController.updateTopic(topicId, topicRequest, authentication);

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

        HttpResponse actualResponse = adminController.updateParameter(parameterId, parameterRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateQuestion() {
        QuestionRequest questionRequest = new QuestionRequest();
        questionRequest.setQuestionText("question");
        Integer questionId = 1;
        Question question = new Question();
        question.setQuestionText("this is a question");

        HttpResponse actualResponse = adminController.updateQuestion(questionId, questionRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateTopicReferences() {
        TopicReferencesRequest referencesRequest = new TopicReferencesRequest();
        referencesRequest.setReference("reference");
        Integer referenceId = 1;
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello");

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

        HttpResponse actualResponse = adminController.updateParameterReference(referenceId, referencesRequest, authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}

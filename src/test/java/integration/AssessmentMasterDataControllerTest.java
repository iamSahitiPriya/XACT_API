/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import io.micronaut.http.HttpRequest;
import com.xact.assessment.repositories.*;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.reactive.resource.HttpResource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class AssessmentMasterDataControllerTest {

    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ModuleRepository moduleRepository;

    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

    @Inject
    AssessmentParameterRepository assessmentParameterRepository;

    @Inject
    AssessmentParameterReferenceRepository assessmentParameterReferenceRepository;

    @Inject
    AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;

    @MockBean(CategoryRepository.class)
    CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
    }


    @MockBean(ModuleRepository.class)
    ModuleRepository moduleRepository() {
        return mock(ModuleRepository.class);
    }

    @MockBean(AssessmentTopicRepository.class)
    AssessmentTopicRepository topicRepository() {
        return mock(AssessmentTopicRepository.class);
    }
    @MockBean(AssessmentParameterRepository.class)
    AssessmentParameterRepository parameterRepository() {
        return mock(AssessmentParameterRepository.class);
    }
    @MockBean(AssessmentParameterReferenceRepository.class)
    AssessmentParameterReferenceRepository parameterReferenceRepository() {
        return mock(AssessmentParameterReferenceRepository.class);
    }
    @MockBean(AssessmentTopicReferenceRepository.class)
    AssessmentTopicReferenceRepository topicReferenceRepository() {
        return mock(AssessmentTopicReferenceRepository.class);
    }


    @Test
    void testGetMasterDataCategoryResponse() throws IOException {

        AssessmentCategory category = getAssessmentCategory();

        List<AssessmentCategory> allCategories = Collections.singletonList(category);
        when(categoryRepository.findAll()).thenReturn(allCategories);
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-master-data-category-response.json");

        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment-master-data/categories")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, userResponse);

    }

    private AssessmentCategory getAssessmentCategory() {
        Set<AssessmentModule> modules = new HashSet<>();
        Set<AssessmentTopic> topics = new HashSet<>();
        Set<AssessmentTopicReference> topicReferences = new HashSet<>();
        Set<AssessmentParameterReference> parameterReferences = new HashSet<>();
        Set<AssessmentParameter> parameters = new HashSet<>();
        Set<Question> questions = new HashSet<>();


        AssessmentCategory category = new AssessmentCategory();
        AssessmentModule module = new AssessmentModule();
        AssessmentTopic topic = new AssessmentTopic();
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        Question question = new Question();


        category.setCategoryId(3);
        category.setCategoryName("My category");
        category.setActive(true);
        category.setModules(modules);


        module.setModuleId(3);
        module.setModuleName("My module");
        module.setCategory(category);
        module.setTopics(topics);
        module.setActive(true);
        modules.add(module);


        topic.setTopicId(5);
        topic.setTopicName("My topic");
        topic.setModule(module);
        topic.setReferences(topicReferences);
        topic.setParameters(parameters);

        topic.setActive(true);
        topics.add(topic);

        topicReference.setReferenceId(1);
        topicReference.setRating(Rating.ONE);
        topicReference.setReference("One Reference topic");
        topicReference.setTopic(topic);
        topicReferences.add(topicReference);

        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("My parameter");
        assessmentParameter.setTopic(topic);
        assessmentParameter.setQuestions(questions);
        assessmentParameter.setReferences(parameterReferences);

        assessmentParameter.setActive(true);
        parameters.add(assessmentParameter);

        parameterReference.setReferenceId(1);
        parameterReference.setRating(Rating.ONE);
        parameterReference.setReference("One Reference parameter");
        parameterReference.setParameter(assessmentParameter);
        parameterReferences.add(parameterReference);

        question.setParameter(assessmentParameter);
        question.setQuestionId(1);
        question.setQuestionText("My question");
        questions.add(question);

        return category;
    }

    @Test
    void shouldCreateCategory() throws IOException {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("Hello");
        assessmentCategory.setActive(false);

        when(categoryRepository.save(assessmentCategory)).thenReturn(assessmentCategory);

        String dataRequest = resourceFileUtil.getJsonString("dto/set-category-reponse.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/category", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


    }

    @Test
    void shouldCreateModule() throws IOException {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setActive(false);
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);
        assessmentCategory.setCategoryName("Hello");
        assessmentCategory.setActive(false);

        when(moduleRepository.save(assessmentModule)).thenReturn(assessmentModule);
        when(categoryRepository.findCategoryById(1)).thenReturn(assessmentCategory);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-module-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/modules", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void shouldCreateTopic() throws IOException {
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("This is a module");
        assessmentTopic.setActive(false);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("Module");
        assessmentModule.setActive(false);

        when(moduleRepository.findByModuleId(1)).thenReturn(assessmentModule);
        when(assessmentTopicRepository.save(assessmentTopic)).thenReturn(assessmentTopic);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/topics", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

    @Test
    void shouldCreateParameter() throws IOException {
        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("This is a module");
        assessmentTopic.setActive(false);

        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterId(1);
        parameter.setParameterName("parameter");

        when(assessmentTopicRepository.findByTopicId(1)).thenReturn(assessmentTopic);
        when(assessmentParameterRepository.save(parameter)).thenReturn(parameter);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/topics", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void shouldCreateTopicReference() throws IOException {
        AssessmentTopicReference topicReference = new AssessmentTopicReference();
        topicReference.setReference("Hello this is a reference");
        topicReference.setReferenceId(1);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("This is a module");
        assessmentTopic.setActive(false);

        when(assessmentTopicRepository.findById(1)).thenReturn(Optional.of(assessmentTopic));
        when(assessmentTopicReferenceRepository.save(topicReference)).thenReturn(topicReference);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-topicReference-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/topicReferences", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
    }

    @Test
    void shouldCreateParameterReference() throws IOException {
        AssessmentParameterReference parameterReference = new AssessmentParameterReference();
        parameterReference.setReference("Hello this is a reference");
        parameterReference.setReferenceId(1);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("This is a module");
        assessmentParameter.setActive(false);

        when(assessmentParameterRepository.findById(1)).thenReturn(Optional.of(assessmentParameter));
        when(assessmentParameterReferenceRepository.save(parameterReference)).thenReturn(parameterReference);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameterReference-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/assessment-master-data/parameterReferences", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

    }

}

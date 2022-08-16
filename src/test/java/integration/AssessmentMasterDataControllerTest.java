/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import io.micronaut.http.HttpRequest;
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

    @MockBean(CategoryRepository.class)
    CategoryRepository categoryRepository() {
        return mock(CategoryRepository.class);
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


}

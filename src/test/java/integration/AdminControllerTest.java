/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.utils.ResourceFileUtil;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class AdminControllerTest {

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

    @Inject
    QuestionRepository questionRepository;

    @Inject
    AccessControlRepository accessControlRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        String userEmail = "dummy@test.com";
        AccessControlList accessControlList = new AccessControlList(userEmail, AccessControlRoles.Admin);
        accessControlRepository.save(accessControlList);
    }

    @AfterEach
    public void afterEach() {
        accessControlRepository.deleteAll();
        entityManager.flush();
    }


    @Test
    void shouldCreateCategory() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-category-reponse.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/categories", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentCategory> assessmentCategories = categoryRepository.findCategoriesSortedByUpdatedDate();
        AssessmentCategory assessmentCategory = assessmentCategories.get(0);

        assertEquals("Software", assessmentCategories.get(0).getCategoryName());

        categoryRepository.delete(assessmentCategory);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateModule() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-module-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/modules", dataRequest)
                .bearerAuth("anything"));

        List<AssessmentModule> assessmentModules = (List<AssessmentModule>) moduleRepository.findAll();
        AssessmentModule assessmentModule = assessmentModules.stream().filter(assessmentModule1 -> assessmentModule1.getModuleName().equals("ModuleName2")).findFirst().get();

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
        assertEquals("ModuleName2", assessmentModule.getModuleName());

        moduleRepository.delete(assessmentModule);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateTopic() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/topics", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentTopic> assessmentTopics = (List<AssessmentTopic>) assessmentTopicRepository.findAll();
        Optional<AssessmentTopic> assessmentTopic = assessmentTopics.stream().filter(assessmentTopic1 -> assessmentTopic1.getTopicName().equals("Software1234")).findAny();

        assertEquals("Software1234", assessmentTopic.get().getTopicName());

        assessmentTopicRepository.delete(assessmentTopic.get());
        entityManager.getTransaction().commit();

    }

    @Test
    void shouldCreateParameter() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/parameters", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentParameter> assessmentParameters = (List<AssessmentParameter>) assessmentParameterRepository.findAll();
        Optional<AssessmentParameter> assessmentParameter = assessmentParameters.stream().filter(assessmentParameter1 -> assessmentParameter1.getParameterName().equals("Software1234")).findAny();

        assertEquals("Software1234", assessmentParameter.get().getParameterName());

        assessmentParameterRepository.delete(assessmentParameter.get());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateTopicReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name 1 Example");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleName("Module Name 1 Example");
        assessmentModule.setCategory(assessmentCategory);
        assessmentModule.setActive(true);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        categoryRepository.save(assessmentCategory);
        moduleRepository.save(assessmentModule);
        assessmentTopicRepository.save(assessmentTopic);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();


        String dataRequest = "[{" + "\"topic\"" + ":" + assessmentTopic.getTopicId() + "," + "\"rating\"" + ":" + "\"TWO\"" + "," + "\"reference\"" + ":" + "\"This is a reference\"" + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/topic-references", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


        Set<AssessmentTopicReference> assessmentTopicReferences = assessmentTopicRepository.findByTopicId(assessmentTopic.getTopicId()).getReferences();

        entityManager.getTransaction().begin();
        for (AssessmentTopicReference assessmentTopicReference : assessmentTopicReferences) {
            if (assessmentTopicReference.getRating().equals(Rating.TWO)) {
                assertEquals("This is a reference", assessmentTopicReference.getReference());
                assessmentTopicReferenceRepository.deleteById(assessmentTopicReference.getReferenceId());
            }
        }
        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        moduleRepository.deleteById(assessmentModule.getModuleId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateParameterReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name Example_1");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleName("Module Name Example_1");
        assessmentModule.setCategory(assessmentCategory);
        assessmentModule.setActive(true);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name Example_1");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterName("Parameter Name Example_1");
        assessmentParameter.setTopic(assessmentTopic);
        assessmentParameter.setActive(true);

        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));
        categoryRepository.save(assessmentCategory);
        moduleRepository.save(assessmentModule);
        assessmentTopicRepository.save(assessmentTopic);
        assessmentParameterRepository.save(assessmentParameter);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "[{" + "\"parameter\"" + ":" + assessmentParameter.getParameterId() + "," + "\"rating\"" + ":" + "\"TWO\"" + "," + "\"reference\"" + ":" + "\"This is a reference\"" + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/parameter-references", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        Set<AssessmentParameterReference> assessmentParameterReference = assessmentParameterRepository.findByParameterId(assessmentParameter.getParameterId()).getReferences();
        entityManager.getTransaction().begin();
        for (AssessmentParameterReference assessmentParameterReference1 : assessmentParameterReference) {
            if (assessmentParameterReference1.getRating().equals(Rating.TWO)) {
                assertEquals("This is a reference", assessmentParameterReference1.getReference());
                assessmentParameterReferenceRepository.deleteById(assessmentParameterReference1.getReferenceId());
            }
        }

        assessmentParameterRepository.deleteById(assessmentParameter.getParameterId());
        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        moduleRepository.deleteById(assessmentModule.getModuleId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
        entityManager.getTransaction().commit();

    }

    @Test
    void getAllCategories() {
        var actualResult = client.toBlocking().exchange(HttpRequest.GET("/v1/categories?role=Admin")
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), actualResult.getStatus());
    }

    @Test
    void shouldUpdateCategory() {
        AssessmentCategory assessmentCategory = new AssessmentCategory("new category", true, "");

        categoryRepository.save(assessmentCategory);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"categoryId\"" + ":" + assessmentCategory.getCategoryId() + "," + "\"categoryName\"" + ":" + "\"modified category\"" + "," + "\"isActive\"" + ":" + "\"true\"" + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/categories/" + assessmentCategory.getCategoryId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        AssessmentCategory assessmentCategory1 = categoryRepository.findCategoryById(assessmentCategory.getCategoryId());

        assertEquals("modified category", assessmentCategory1.getCategoryName());

        categoryRepository.delete(assessmentCategory1);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldUpdateModule() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);

        boolean expectedResult = !assessmentModule.getIsActive();


        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "[{" + "\"moduleName\"" + ":" + "\"" + assessmentModule.getModuleName() + "\"" + "," + "\"isActive\"" + ":" + expectedResult + "," + "\"category\"" + ":" + assessmentModule.getCategory().getCategoryId() + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/modules/" + assessmentModule.getModuleId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        AssessmentModule assessmentModule1 = moduleRepository.findByModuleId(assessmentModule.getModuleId());

        assertEquals(expectedResult, assessmentModule1.getIsActive());

        moduleRepository.update(assessmentModule);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldUpdateTopic() {
        AssessmentTopic assessmentTopic = assessmentTopicRepository.findByTopicId(1);

        boolean expectedResult = !assessmentTopic.getIsActive();


        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "[{" + "\"topicName\"" + ":" + "\"" + assessmentTopic.getTopicName() + "\"" + "," + "\"isActive\"" + ":" + expectedResult + "," + "\"module\"" + ":" + assessmentTopic.getModule().getModuleId() + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/topics/" + assessmentTopic.getTopicId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        AssessmentTopic assessmentTopic1 = assessmentTopicRepository.findByTopicId(assessmentTopic.getTopicId());

        assertEquals(expectedResult, assessmentTopic1.getIsActive());

        assessmentTopicRepository.update(assessmentTopic);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameter assessmentParameter = assessmentParameterRepository.findByParameterId(1);

        boolean expectedResult = !assessmentParameter.getIsActive();


        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "[{" + "\"parameterName\"" + ":" + "\"" + assessmentParameter.getParameterName() + "\"" + "," + "\"isActive\"" + ":" + expectedResult + "," + "\"topic\"" + ":" + assessmentParameter.getTopic().getTopicId() + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/parameters/" + assessmentParameter.getParameterId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        AssessmentParameter assessmentParameter1 = assessmentParameterRepository.findByParameterId(assessmentParameter.getParameterId());

        assertEquals(expectedResult, assessmentParameter1.getIsActive());

        assessmentParameterRepository.update(assessmentParameter);
        entityManager.getTransaction().commit();
    }

//    @Test
//    void shouldUpdateQuestion() {
//        Optional<Question> question = questionRepository.findById(1);
//        Question question1 = question.get();
//
//        String questionText = question1.getQuestionText();
//
//        entityManager.getTransaction().commit();
//        entityManager.clear();
//        entityManager.close();
//
//        String dataRequest = "{" + "\"questionId\"" + ":" + question1.getQuestionId() + "," + "\"questionText\"" + ":" + "\"Updated Question\"" + "," + "\"parameter\"" + ":" + question1.getParameter().getParameterId() + "}";
//
//        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/questions/" + question1.getQuestionId(), dataRequest)
//                .bearerAuth("anything"));
//
//        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
//
//        entityManager.getTransaction().begin();
//        Optional<Question> question2 = questionRepository.findById(1);
//        Question question3 = question2.get();
//
//        assertEquals("Updated Question", question3.getQuestionText());
//
//        question3.setQuestionText(questionText);
//
//        questionRepository.update(question3);
//        entityManager.getTransaction().commit();
//    }

    @Test
    void shouldUpdateTopicReferences() {
        Optional<AssessmentTopicReference> assessmentTopicReference = assessmentTopicReferenceRepository.findById(1);
        AssessmentTopicReference assessmentTopicReference1 = assessmentTopicReference.get();


        String reference = assessmentTopicReference1.getReference();

        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"referenceId\"" + ":" + assessmentTopicReference1.getReferenceId() + "," + "\"reference\"" + ":" + "\"Updated Reference\"" + "," + "\"topic\"" + ":" + assessmentTopicReference1.getTopic().getTopicId() + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/topic-references/" + assessmentTopicReference1.getReferenceId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        Optional<AssessmentTopicReference> assessmentTopicReference2 = assessmentTopicReferenceRepository.findById(1);
        AssessmentTopicReference assessmentTopicReference3 = assessmentTopicReference2.get();

        assertEquals("Updated Reference", assessmentTopicReference3.getReference());

        assessmentTopicReference3.setReference(reference);

        assessmentTopicReferenceRepository.update(assessmentTopicReference1);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldUpdateParameterReferences() {
        Optional<AssessmentParameterReference> assessmentParameterReference = assessmentParameterReferenceRepository.findById(1);
        AssessmentParameterReference assessmentParameterReference1 = assessmentParameterReference.get();


        String reference = assessmentParameterReference1.getReference();

        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"referenceId\"" + ":" + assessmentParameterReference1.getReferenceId() + "," + "\"reference\"" + ":" + "\"Updated Reference\"" + "," + "\"parameter\"" + ":" + assessmentParameterReference1.getParameter().getParameterId() + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/admin/parameter-references/" + assessmentParameterReference1.getReferenceId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        Optional<AssessmentParameterReference> assessmentParameterReference2 = assessmentParameterReferenceRepository.findById(1);
        AssessmentParameterReference assessmentParameterReference3 = assessmentParameterReference2.get();

        assertEquals("Updated Reference", assessmentParameterReference3.getReference());

        assessmentParameterReference3.setReference(reference);

        assessmentParameterReferenceRepository.update(assessmentParameterReference1);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldDeleteTopicReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name 1 Example");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleName("Module Name 1 Example");
        assessmentModule.setCategory(assessmentCategory);
        assessmentModule.setActive(true);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(assessmentTopic,Rating.FIVE,"New Reference");

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        categoryRepository.save(assessmentCategory);
        moduleRepository.save(assessmentModule);
        assessmentTopicRepository.save(assessmentTopic);
        assessmentTopicReferenceRepository.save(assessmentTopicReference);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/admin/topic-references/" + assessmentTopicReference.getReferenceId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


        Set<AssessmentTopicReference> assessmentTopicReferences = assessmentTopicRepository.findByTopicId(assessmentTopic.getTopicId()).getReferences();

        assertEquals(0,assessmentTopicReferences.size());

        entityManager.getTransaction().begin();

        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        moduleRepository.deleteById(assessmentModule.getModuleId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldDeleteParameterReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name 1 Example");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleName("Module Name 1 Example");
        assessmentModule.setCategory(assessmentCategory);
        assessmentModule.setActive(true);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterName("Parameter Name 1 Example");
        assessmentParameter.setTopic(assessmentTopic);
        assessmentParameter.setActive(true);

        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(assessmentParameter,Rating.FIVE,"New Reference");

        assessmentCategory.setModules(Collections.singleton(assessmentModule));
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));

        categoryRepository.save(assessmentCategory);
        moduleRepository.save(assessmentModule);
        assessmentTopicRepository.save(assessmentTopic);
        assessmentParameterRepository.save(assessmentParameter);

        assessmentParameterReferenceRepository.save(assessmentParameterReference);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/admin/parameter-references/" + assessmentParameterReference.getReferenceId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


        Set<AssessmentParameterReference> assessmentParameterReferences = assessmentParameterRepository.findByParameterId(assessmentParameter.getParameterId()).getReferences();

        assertEquals(0,assessmentParameterReferences.size());

        entityManager.getTransaction().begin();

        assessmentParameterRepository.deleteById(assessmentParameter.getParameterId());
        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        moduleRepository.deleteById(assessmentModule.getModuleId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());

        entityManager.getTransaction().commit();
    }
}

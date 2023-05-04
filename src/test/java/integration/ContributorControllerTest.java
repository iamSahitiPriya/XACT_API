/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.ContributorQuestionStatus;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.xact.assessment.dtos.ContributorRole.AUTHOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class ContributorControllerTest {
    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    ModuleRepository moduleRepository;
    @Inject
    QuestionRepository questionRepository;
    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
    @Inject
    ModuleContributorRepository moduleContributorRepository;

    @Inject
    UserQuestionRepository userQuestionRepository;

    @Inject
    AssessmentParameterRepository assessmentParameterRepository;

    @Inject
    AccessControlRepository accessControlRepository;
    @Inject
    AssessmentTopicRepository assessmentTopicRepository;

    @Inject
    AssessmentParameterReferenceRepository assessmentParameterReferenceRepository;

    @Inject
    AssessmentTopicReferenceRepository assessmentTopicReferenceRepository;

    @Inject
    EntityManager entityManager;
    @BeforeEach
    public void beforeEach() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
    }

    @AfterEach
    public void afterEach() {
        questionRepository.deleteAll();
        accessControlRepository.deleteAll();
        moduleContributorRepository.deleteAll();
        entityManager.flush();
    }

    @Test
    void shouldGetContributorQuestionResponse() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();


        String expectedResponse = "{\"contributorModuleData\":[{\"moduleId\":1,\"moduleName\":\"Architecture Quality\",\"categoryName\":\"Software engineering\",\"categoryId\":1,\"topics\":[{\"topicId\":" + question.getParameter().getTopic().getTopicId() + ",\"topicName\":" + "\"" + question.getParameter().getTopic().getTopicName() + "\"" + ",\"parameters\":[{\"parameterId\":" + question.getParameter().getParameterId() + ",\"parameterName\":" + "\"" + question.getParameter().getParameterName() + "\"" + ",\"questions\":[{\"questionId\":" + question.getQuestionId() + ",\"question\":\"new user question\",\"status\":\"DRAFT\"}]}]}]}]}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/contributor/questions?role=Author").bearerAuth("anything"), String.class);
        Assertions.assertEquals(expectedResponse, actualResponse);

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{\n" +
                "    \"questionId\":[" + question.getQuestionId() + "],\n" +
                "    \"comments\":\"comments\"\n" +
                "}";

        String expectedResponse = "{\"questionId\":[" + question.getQuestionId() + "],\"comments\":\"comments\",\"status\":\"SENT_FOR_REVIEW\"}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.PATCH("/v1/contributor/modules/1/questions?status=SENT_FOR_REVIEW", dataRequest).bearerAuth("anything"), String.class);

        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldDeleteContributorQuestion() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var actualResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/contributor/questions/" + question.getQuestionId()).bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldUpdateContributorQuestion() {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        Question question = new Question();
        question.setQuestionText("new user question");
        question.setQuestionStatus(ContributorQuestionStatus.DRAFT);
        question.setParameter(assessmentModule.getTopics().stream().toList().get(0).getParameters().stream().toList().get(0));

        questionRepository.save(question);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();


        String expectedResponse = "{\"questionId\":" + question.getQuestionId() + ",\"questionText\":\"new question text\",\"parameter\":" + question.getParameter().getParameterId() + ",\"status\":\"DRAFT\"}";
        String actualResponse = client.toBlocking().retrieve(HttpRequest.PATCH("/v1/contributor/questions/" + question.getQuestionId(), "new question text").bearerAuth("anything"), String.class);


        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldCreateTopic() throws IOException {
        AssessmentModule assessmentModule = moduleRepository.findByModuleId(1);
        ContributorId contributorId = new ContributorId(assessmentModule, "dummy@test.com");
        ModuleContributor moduleContributor = ModuleContributor.builder().contributorId(contributorId).contributorRole(AUTHOR).build();
        moduleContributorRepository.deleteAll();
        moduleContributorRepository.save(moduleContributor);
        String dataRequest = resourceFileUtil.getJsonString("dto/set-topic-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/contributor/topics", dataRequest)
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

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/contributor/parameters", dataRequest)
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

        AssessmentModule assessmentModule =moduleRepository.findByModuleId(1);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        categoryRepository.save(assessmentCategory);
        assessmentTopicRepository.save(assessmentTopic);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();


        String dataRequest = "[{" + "\"topic\"" + ":" + assessmentTopic.getTopicId() + "," + "\"rating\"" + ":" + "\"TWO\"" + "," + "\"reference\"" + ":" + "\"This is a reference\"" + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/contributor/topic-references", dataRequest)
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
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateParameterReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name Example_1");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule =moduleRepository.findByModuleId(1);


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

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/contributor/parameter-references", dataRequest)
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
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
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

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/contributor/topics/" + assessmentTopic.getTopicId(), dataRequest)
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

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/contributor/parameters/" + assessmentParameter.getParameterId(), dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        entityManager.getTransaction().begin();
        AssessmentParameter assessmentParameter1 = assessmentParameterRepository.findByParameterId(assessmentParameter.getParameterId());

        assertEquals(expectedResult, assessmentParameter1.getIsActive());

        assessmentParameterRepository.update(assessmentParameter);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldUpdateTopicReferences() {
        Optional<AssessmentTopicReference> assessmentTopicReference = assessmentTopicReferenceRepository.findById(1);
        AssessmentTopicReference assessmentTopicReference1 = assessmentTopicReference.get();


        String reference = assessmentTopicReference1.getReference();

        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"referenceId\"" + ":" + assessmentTopicReference1.getReferenceId() + "," + "\"reference\"" + ":" + "\"Updated Reference\"" + "," + "\"topic\"" + ":" + assessmentTopicReference1.getTopic().getTopicId() + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/contributor/topic-references/" + assessmentTopicReference1.getReferenceId(), dataRequest)
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
        AssessmentModule assessmentModule =moduleRepository.findByModuleId(1);
        assessmentParameterReference1.getParameter().getTopic().setModule(assessmentModule);

        String reference = assessmentParameterReference1.getReference();

        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        String dataRequest = "{" + "\"referenceId\"" + ":" + assessmentParameterReference1.getReferenceId() + "," + "\"reference\"" + ":" + "\"Updated Reference\"" + "," + "\"parameter\"" + ":" + assessmentParameterReference1.getParameter().getParameterId() + "}";

        var saveResponse = client.toBlocking().exchange(HttpRequest.PUT("/v1/contributor/parameter-references/" + assessmentParameterReference1.getReferenceId(), dataRequest)
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

        AssessmentModule assessmentModule =moduleRepository.findByModuleId(1);


        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference(Rating.FIVE, "New Reference",assessmentTopic);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentCategory.setModules(Collections.singleton(assessmentModule));

        categoryRepository.save(assessmentCategory);
        assessmentTopicRepository.save(assessmentTopic);
        assessmentTopicReferenceRepository.save(assessmentTopicReference);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/contributor/topic-references/" + assessmentTopicReference.getReferenceId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


        Set<AssessmentTopicReference> assessmentTopicReferences = assessmentTopicRepository.findByTopicId(assessmentTopic.getTopicId()).getReferences();

        assertEquals(0, assessmentTopicReferences.size());

        entityManager.getTransaction().begin();

        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldDeleteParameterReference() {
        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryName("Category Name 1 Example");
        assessmentCategory.setActive(true);

        AssessmentModule assessmentModule =moduleRepository.findByModuleId(1);


        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicName("Topic Name 1 Example");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterName("Parameter Name 1 Example");
        assessmentParameter.setTopic(assessmentTopic);
        assessmentParameter.setActive(true);

        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(Rating.FIVE, "New Reference",assessmentParameter);

        assessmentCategory.setModules(Collections.singleton(assessmentModule));
        assessmentModule.setTopics(Collections.singleton(assessmentTopic));
        assessmentTopic.setParameters(Collections.singleton(assessmentParameter));

        categoryRepository.save(assessmentCategory);
        assessmentTopicRepository.save(assessmentTopic);
        assessmentParameterRepository.save(assessmentParameter);

        assessmentParameterReferenceRepository.save(assessmentParameterReference);
        entityManager.getTransaction().commit();
        entityManager.clear();
        entityManager.close();

        var saveResponse = client.toBlocking().exchange(HttpRequest.DELETE("/v1/contributor/parameter-references/" + assessmentParameterReference.getReferenceId())
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());


        Set<AssessmentParameterReference> assessmentParameterReferences = assessmentParameterRepository.findByParameterId(assessmentParameter.getParameterId()).getReferences();

        assertEquals(0, assessmentParameterReferences.size());

        entityManager.getTransaction().begin();

        assessmentParameterRepository.deleteById(assessmentParameter.getParameterId());
        assessmentTopicRepository.deleteById(assessmentTopic.getTopicId());
        categoryRepository.deleteById(assessmentCategory.getCategoryId());

        entityManager.getTransaction().commit();
    }

}

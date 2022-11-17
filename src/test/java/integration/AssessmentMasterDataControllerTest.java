/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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
    }

    @Test
    void testGetMasterDataCategoryResponse() throws IOException {
        String categoryDto = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment-master-data/1/categories/all")
                .bearerAuth("anything"), String.class);

        String categories = resourceFileUtil.getJsonString("dto/all-categories.json");
        assertEquals(categories, categoryDto);
    }

    @Test
    void testGetSelectedCategoryResponse() throws IOException {
        String categoryDto = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment-master-data/1/categories")
                .bearerAuth("anything"), String.class);

        assertEquals("{}", categoryDto);
    }

    @Test
    void shouldCreateCategory() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-category-reponse.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/categories", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentCategory> assessmentCategories = categoryRepository.findCategories();
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
        AssessmentModule assessmentModule = assessmentModules.stream().filter(assessmentModule1 -> assessmentModule1.getModuleName().equals("Module1")).findFirst().get();

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
        assertEquals("Module1", assessmentModule.getModuleName());

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
        Optional<AssessmentTopic> assessmentTopic = assessmentTopics.stream().filter(assessmentTopic1 -> assessmentTopic1.getTopicName().equals("Software123")).findFirst();

        assertEquals("Software123", assessmentTopic.get().getTopicName());

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
        Optional<AssessmentParameter> assessmentParameter = assessmentParameters.stream().filter(assessmentParameter1 -> assessmentParameter1.getParameterName().equals("Software123")).findAny();

        assertEquals("Software123", assessmentParameter.get().getParameterName());

        assessmentParameterRepository.delete(assessmentParameter.get());
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateTopicReference() throws IOException {
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

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/topicReferences", dataRequest)
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
    void shouldCreateParameterReference() throws IOException {
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

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/parameterReferences", dataRequest)
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
}

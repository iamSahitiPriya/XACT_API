/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.*;
import com.xact.assessment.services.UserAuthService;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    UserAuthService userAuthService;


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
        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/assessment-master-data/categories")
                .bearerAuth("anything"), String.class);

        assertNotEquals(userResponse,null);
    }

    @Test
    void shouldCreateCategory() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-category-reponse.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/categories", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
        List<AssessmentCategory> assessmentCategories = categoryRepository.findCategories();
        AssessmentCategory assessmentCategory = assessmentCategories.get(0);
        categoryRepository.delete(assessmentCategory);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateModule() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-module-response.json");
        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/modules", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentModule> assessmentModules = (List<AssessmentModule>) moduleRepository.findAll();
        AssessmentModule assessmentModule = assessmentModules.get(assessmentModules.size()-1);
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
        AssessmentTopic assessmentTopic= assessmentTopics.get(assessmentTopics.size()-1);
        assessmentTopicRepository.delete(assessmentTopic);
        entityManager.getTransaction().commit();

    }

    @Test
    void shouldCreateParameter() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameter-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/parameters", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        List<AssessmentParameter> assessmentParameters = (List<AssessmentParameter>) assessmentParameterRepository.findAll();
        AssessmentParameter assessmentParameter = assessmentParameters.get(assessmentParameters.size()-1);
        assessmentParameterRepository.delete(assessmentParameter);
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateTopicReference() throws IOException {
        String dataRequest = "[{" + "\"topic\"" + ":" + 56 + "," + "\"rating\"" + ":" + "\"TWO\"" + "," + "\"reference\"" + ":" + "\"This is a reference\"" + "}]";

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/topicReferences", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());
        Set<AssessmentTopicReference> assessmentTopicReference =  assessmentTopicRepository.findByTopicId(56).getReferences();
        for(AssessmentTopicReference assessmentTopicReference1 : assessmentTopicReference) {
            assessmentTopicReferenceRepository.delete(assessmentTopicReference1);
        }
        entityManager.getTransaction().commit();
    }

    @Test
    void shouldCreateParameterReference() throws IOException {
        String dataRequest = resourceFileUtil.getJsonString("dto/set-parameterReference-response.json");

        var saveResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/parameterReferences", dataRequest)
                .bearerAuth("anything"));

        assertEquals(HttpResponse.ok().getStatus(), saveResponse.getStatus());

        Set<AssessmentParameterReference> assessmentParameterReference =  assessmentParameterRepository.findByParameterId(52).getReferences();
        for(AssessmentParameterReference assessmentParameterReference1 : assessmentParameterReference) {
            assessmentParameterReferenceRepository.delete(assessmentParameterReference1);
        }
        entityManager.getTransaction().commit();

    }

}

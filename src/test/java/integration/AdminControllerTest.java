/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package integration;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRequest;
import com.xact.assessment.dtos.ContributorRole;
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
    ModuleContributorRepository moduleContributorRepository;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    public void beforeEach() {
        String userEmail = "dummy@test.com";
        AccessControlList accessControlList = new AccessControlList(userEmail, AccessControlRoles.PRIMARY_ADMIN);
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
    void shouldSaveModuleContributor() {
        Integer moduleId = 1;
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setUserEmail("abc@thoughtworks.com");
        contributorDto.setRole(ContributorRole.AUTHOR);
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(moduleId);
        ModuleContributor moduleContributor = new ModuleContributor();
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(assessmentModule);
        contributorId.setUserEmail(contributorDto.getUserEmail());
        moduleContributor.setContributorId(contributorId);
        moduleContributor.setContributorRole(contributorDto.getRole());
        ContributorRequest contributorRequest=new ContributorRequest();
        contributorRequest.setContributors(Collections.singletonList(contributorDto));

        HttpResponse<ContributorDto> actualResponse = client.toBlocking().exchange(HttpRequest.POST("/v1/admin/modules/" + moduleId + "/contributors",contributorRequest ).bearerAuth("anything"));

        assertEquals(actualResponse.getStatus(),HttpResponse.ok().getStatus());
    }
}

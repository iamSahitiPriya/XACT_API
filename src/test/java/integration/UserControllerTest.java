package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import com.xact.assessment.utils.ResourceFileUtil;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class UserControllerTest {

    @Inject
    @Client("/")
    HttpClient client; //

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    AccessControlRepository accessControlRepository;

    @Inject
    UsersAssessmentsService usersAssessmentsService;

    @Inject
    AssessmentService assessmentService;

    @Inject
    EntityManager entityManager;


    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @AfterEach
    public void afterEach() {
        usersAssessmentsRepository.deleteAll();
        assessmentRepository.deleteAll();
        accessControlRepository.deleteAll();
    }

    @Test
    void shouldGetUserRole() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("new");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("new");
        assessment.setOrganisation(organisation);
        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(userEmail, assessment);

        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        AccessControlList accessControlList = new AccessControlList(userEmail, AccessControlRoles.Admin);

        assessmentRepository.save(assessment);
        accessControlRepository.save(accessControlList);
        usersAssessmentsRepository.save(assessmentUser);
        entityManager.getTransaction().commit();
        entityManager.clear();


        String expectedResponse = resourceFileUtil.getJsonString("dto/get-user-role-response.json");
        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/users/roles")
                .bearerAuth("anything"), String.class);
        assertEquals(expectedResponse, userResponse);
    }

}

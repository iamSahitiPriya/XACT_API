package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.repositories.AssessmentRepository;
import com.xact.assessment.repositories.OrganisationRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class UserControllerTest {
    private ModelMapper mapper = new ModelMapper();


    @Inject
    @Client("/")
    HttpClient client; //

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;

    @Inject
    AssessmentRepository assessmentRepository;

    @Inject
    OrganisationRepository organisationRepository;

    @Inject
    AccessControlRepository accessControlRepository;

    @Inject
    UsersAssessmentsService usersAssessmentsService;

    @Inject
    AssessmentService assessmentService;


    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();



    @Transactional
    @Test
    void shouldGetUserRole() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("new");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("new");
        assessment.setOrganisation(organisation);
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);

        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);
        AccessControlList accessControlList = new AccessControlList(userEmail,AccessControlRoles.Admin);

        assessmentRepository.save(assessment);
        accessControlRepository.save(accessControlList);
        usersAssessmentsRepository.save(assessmentUsers);

       List<AccessControlList> accessControlLists = (List<AccessControlList>) accessControlRepository.findAll();
        System.out.println("In testcases ==========" +accessControlLists.size());



        String expectedResponse = resourceFileUtil.getJsonString("dto/get-user-role-response.json");
        AccessControlRoles userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/users/roles")
                .bearerAuth("anything"),AccessControlRoles.class);
        System.out.println(userResponse);
        assertEquals(expectedResponse, userResponse);
    }

    @Test
    void returnAssessment() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("new");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);
        Organisation organisation = new Organisation();
        organisation.setSize(5);
        organisation.setIndustry("new");
        organisation.setDomain("new");
        organisation.setOrganisationName("new");
        assessment.setOrganisation(organisation);

        assessmentRepository.save(assessment);
    }
}

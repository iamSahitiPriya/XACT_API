package integration;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentUsers;
import com.xact.assessment.models.UserId;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.mockito.Mockito;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class AssessmentControllerTest {

    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;
    private final Authentication authentication = Mockito.mock(Authentication.class);

    @MockBean(UsersAssessmentsRepository.class)
    UsersAssessmentsRepository usersAssessmentsRepository() {
        return mock(UsersAssessmentsRepository.class);
    }


    void testGetAssessmentResponse() throws IOException {

        String userEmail = "test@email.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessmentUsers.setUserId(userId);
        assessment.setAssessmentId(23L);
        assessment.setAssessmentName("Mocked Assessment");


        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(singletonList(assessmentUsers));
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-assessments-response.json");

        String assessmentResponse = client.toBlocking()
                .retrieve(HttpRequest.GET("/v1/assessments/").header("Authentication", "Bearer XNJSJBJDBJBD"), String.class);
        assertEquals(expectedResponse, assessmentResponse);

    }


}

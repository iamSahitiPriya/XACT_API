package integration;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AccessControlRepository;
import com.xact.assessment.repositories.CategoryRepository;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class UserControllerTest {
    private ModelMapper mapper = new ModelMapper();

    @Inject
    @Client("/")
    HttpClient client; //

    @Inject
    UsersAssessmentsRepository usersAssessmentsRepository;

    @Inject
    AccessControlRepository accessControlRepository;
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);

    @MockBean(UsersAssessmentsRepository.class)
    UsersAssessmentsRepository usersAssessmentsRepository() {
        return mock(UsersAssessmentsRepository.class);
    }


    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
    @MockBean(AccessControlRepository.class)
    AccessControlRepository accessControlRepository() {
        return mock(AccessControlRepository.class);
    }


    @Test
    void shouldGetUserRole() throws IOException {
        String userEmail = "dummy@test.com";
        Assessment assessment = new Assessment();
        AssessmentUsers assessmentUsers = new AssessmentUsers();
        UserId userId = new UserId(userEmail, assessment);
        assessment.setAssessmentId(23);
        assessmentUsers.setUserId(userId);


        when(usersAssessmentsRepository.findByUserEmail(userEmail)).thenReturn(singletonList(assessmentUsers));
        when(accessControlRepository.getAccessControlRolesByEmail(userEmail)).thenReturn(Optional.of(AccessControlRoles.valueOf("Admin")));
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-user-role-response.json");

        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/users/roles")
                .bearerAuth("anything"),String.class);
        assertEquals(expectedResponse, userResponse);
    }
}

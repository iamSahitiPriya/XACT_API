package integration;

import com.xact.assessment.clients.UserClient;
import com.xact.assessment.repositories.UsersAssessmentsRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import unit.com.xact.assessment.models.*;

import java.io.IOException;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class UserControllerTest {

    @Inject
    @Client("/")
    HttpClient client; //

    ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

    @Inject
    UserClient userClient;

    @MockBean(UserClient.class)
    UserClient userClient() {
        return mock(UserClient.class);
    }


    @Test
    void testGetUserResponse() throws IOException {

        String userEmail = "dummy@test.com";
        Profile profile = new Profile();
        profile.setFirstName("firstName");
        profile.setLastName("lastName");
        profile.setEmail(userEmail);
        User user = new User("active",profile);

        when(userClient.getActiveUser(userEmail)).thenReturn(user);
        String expectedResponse = resourceFileUtil.getJsonString("dto/get-user-response.json");

        String userResponse = client.toBlocking().retrieve(HttpRequest.GET("/v1/users/dummy@test.com")
                .bearerAuth("anything"), String.class);

        assertEquals(expectedResponse, userResponse);

    }


}

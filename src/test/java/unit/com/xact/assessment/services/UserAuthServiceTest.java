package unit.com.xact.assessment.services;

import com.xact.assessment.clients.UserClient;
import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserAuthServiceTest {


    private Authentication authentication = mock(Authentication.class);
    private UserClient userClient = mock(UserClient.class);
    private UserAuthService userAuthService = new UserAuthService(userClient);


    @Test
    void getLoggedInUser() {

        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", "dummy@thoughtworks.com");
        when(authentication.getAttributes()).thenReturn(authMap);

        User loggedInUser = userAuthService.getLoggedInUser(authentication);

        assertEquals("dummy@thoughtworks.com", loggedInUser.getUserEmail());
    }

    @Test
    void getActiveUserByEmail() {

        String email = "test@test.com";

        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setFirstName("testf");
        profile.setLastName("testl");
        User mockedUser = new User("Active", profile);
        when(userClient.getActiveUser(email)).thenReturn(mockedUser);

        User user = userAuthService.getActiveUser(email);

        assertEquals(email, user.getUserEmail());
    }
}

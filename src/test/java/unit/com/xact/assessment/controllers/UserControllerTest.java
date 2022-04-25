package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.UserController;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;
import unit.com.xact.assessment.models.Profile;
import unit.com.xact.assessment.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserAuthService userAuthService = mock(UserAuthService.class);
    private final UserController userController = new UserController(userAuthService);


    @Test
    void testGetUserbyEmail() {
        String email = "test@test.com";

        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setFirstName("testf");
        profile.setLastName("testl");
        User user = new User("Active", profile);
        when(userAuthService.getActiveUser(email)).thenReturn(user);

        HttpResponse<UserDto> response = userController.getUserByEmail(email);


        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(response.body().getEmail(), email);
        assertEquals(response.body().getFirstName(), "testf");
        assertEquals(response.body().getLastName(), "testl");

    }

}

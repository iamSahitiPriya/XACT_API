package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.UserController;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

 class UserControllerTest {
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final UserController userController = new UserController(assessmentService,userAuthService);

    @Test
    void shouldGetTheUserRole() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);
        when(assessmentService.getUserRole(userEmail)).thenReturn(Optional.of(AccessControlRoles.valueOf("Admin")));

        HttpResponse<Optional<AccessControlRoles>> accessControlRolesHttpResponse = userController.getRole(authentication);

        assertEquals(HttpResponse.ok().getStatus(),accessControlRolesHttpResponse.getStatus());

    }
}

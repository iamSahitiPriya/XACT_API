package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.UserController;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final UserController userController = new UserController(assessmentService, userAuthService);

    @Test
    void shouldGetTheUserRole() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(assessmentService.getUserRole(userEmail)).thenReturn(Optional.of(AccessControlRoles.valueOf("Admin")));
        when(assessmentService.getContributorRoles(userEmail)).thenReturn(Collections.singletonList(ContributorRole.valueOf("Author")));

        HttpResponse<List<AccessControlRoles>> accessControlRolesHttpResponse = userController.getRole(authentication);

        assertEquals(HttpResponse.ok().getStatus(), accessControlRolesHttpResponse.getStatus());

    }

    @Test
    void shouldLogin() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        doNothing().when(userAuthService).login("someHeader", user);

        assertDoesNotThrow(() -> userController.login("someHeader", authentication));
    }
}
/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.UserController;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.services.AccessControlService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);
    private final AccessControlService accessControlService = Mockito.mock(AccessControlService.class);
    private final UserController userController = new UserController(accessControlService, userAuthService);

    @Test
    void shouldGetTheUserRole() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);
        ModuleContributor moduleContributor = new ModuleContributor();
        moduleContributor.setContributorRole(ContributorRole.AUTHOR);
        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(accessControlService.getAccessControlRolesByEmail(userEmail)).thenReturn(Optional.of(AccessControlRoles.valueOf("PRIMARY_ADMIN")));

        when(userAuthService.getContributorRoles(userEmail)).thenReturn(Collections.singleton(moduleContributor));

        HttpResponse<Set<AccessControlRoles>> accessControlRolesHttpResponse = userController.getRole(authentication);

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

    @Test
    void shouldGetUserLoginRoles() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("def@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");

        when(userAuthService.getUsers()).thenReturn(Collections.singletonList(userInfo));
        HttpResponse<List<UserInfoDto>> actualResponse = userController.getUsers(authentication);

        assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }
}

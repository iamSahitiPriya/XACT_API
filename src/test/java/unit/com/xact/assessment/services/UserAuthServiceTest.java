/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.client.UserInfoClient;
import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.AccessControlRoles;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.repositories.UserRepository;
import com.xact.assessment.services.AccessControlService;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAuthServiceTest {


    private final Authentication authentication = mock(Authentication.class);
    private final UserInfoClient userInfoClient = mock(UserInfoClient.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    AccessControlService accessControlService = Mockito.mock(AccessControlService.class);

    private final ModuleContributorService moduleContributorService = mock(ModuleContributorService.class);
    private final UserAuthService userAuthService = new UserAuthService(userInfoClient, userRepository, moduleContributorService, accessControlService);


    @Test
    void getCurrentUserWhenNotLoggedIn() {

        String email = "dummy@thoughtworks.com";
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", email);

        when(authentication.getAttributes()).thenReturn(authMap);
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        User loggedInUser = userAuthService.getCurrentUser(authentication);

        assertEquals("dummy@thoughtworks.com", loggedInUser.getUserEmail());
        assertNull(loggedInUser.getUserInfo().getFirstName());
        assertNull(loggedInUser.getUserInfo().getLastName());
        assertNull(loggedInUser.getUserInfo().getLocale());
    }

    @Test
    void getCurrentUserWhenLoggedIn() {

        String email = "dummy@thoughtworks.com";
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", email);
        UserInfo userInfo = new UserInfo(email, "FirstName", "LastName", "en_US");


        when(authentication.getAttributes()).thenReturn(authMap);
        when(userRepository.findById(email)).thenReturn(Optional.of(userInfo));

        User loggedInUser = userAuthService.getCurrentUser(authentication);

        assertEquals("dummy@thoughtworks.com", loggedInUser.getUserEmail());
        assertEquals("FirstName", loggedInUser.getUserInfo().getFirstName());
        assertEquals("LastName", loggedInUser.getUserInfo().getLastName());
        assertEquals("en_US", loggedInUser.getUserInfo().getLocale());
    }

    @Test
    void saveUserDetailsFirstTimeLogin() {

        String email = "dummy@thoughtworks.com";
        User currentUser = new User();
        currentUser.setUserInfo(new UserInfo(email, null, null, null));
        UserInfoDto userInfoDto = new UserInfoDto(email, "FirstName", "LastName", "en_US");
        UserInfo userInfo = new ModelMapper().map(userInfoDto, UserInfo.class);
        when(userRepository.existsById(email)).thenReturn(false);
        when(userInfoClient.getUserInfo("some header")).thenReturn(userInfoDto);
        when(userRepository.save(userInfo)).thenReturn(userInfo);

        userAuthService.login("some header", currentUser);

        assertEquals("dummy@thoughtworks.com", currentUser.getUserEmail());
        verify(userRepository).save(userInfo);
    }

    @Test
    void dontSaveUserDetailsSecondTimeLogin() {

        String email = "dummy@thoughtworks.com";
        User currentUser = new User();
        currentUser.setUserInfo(new UserInfo(email, null, null, null));
        UserInfoDto userInfoDto = new UserInfoDto(email, "FirstName", "LastName", "en_US");
        UserInfo userInfo = new ModelMapper().map(userInfoDto, UserInfo.class);
        when(userRepository.existsById(email)).thenReturn(true);

        userAuthService.login("some header", currentUser);

        assertEquals("dummy@thoughtworks.com", currentUser.getUserEmail());
        verify(userRepository, times(0)).save(userInfo);
    }

    @Test
    void shouldGetLoggedInUsers() {
        Set<String> users = new HashSet<>();
        users.add("abc@thoughtworks.com");
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("abc@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");
        when(userRepository.findByUsers(users)).thenReturn(Collections.singletonList(userInfo));

        List<UserInfo> userInfos = userAuthService.getLoggedInUsers(users);

        assertEquals(userInfos.size(), 1);
    }

    @Test
    void shouldGetAllUsers() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("abc@thoughtworks.com");
        userInfo.setFirstName("ABC");
        userInfo.setLastName("DEF");
        userInfo.setLocale("US");
        when(userRepository.findAllUsers()).thenReturn(Collections.singletonList(userInfo));

        List<UserInfo> userInfos = userAuthService.getUsers();
        assertEquals(userInfos.size(), 1);
    }

    @Test
    void shouldValidateUser() {
        String email = "dummy@thoughtworks.com";
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", email);
        User currentUser = new User();
        currentUser.setUserInfo(new UserInfo(email, null, null, null));

        when(authentication.getAttributes()).thenReturn(authMap);
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        when(accessControlService.getAccessControlRolesByEmail(email)).thenReturn(Optional.of(AccessControlRoles.PRIMARY_ADMIN));

        userAuthService.validateUser(authentication, AccessControlRoles.PRIMARY_ADMIN);
        verify(accessControlService).getAccessControlRolesByEmail(email);
    }

    @Test
    void shouldThrowExceptionIfTheUserIsInvalid() {
        String email = "dummy@thoughtworks.com";
        Map<String, Object> authMap = new HashMap<>();
        authMap.put("sub", email);
        User currentUser = new User();
        currentUser.setUserInfo(new UserInfo(email, null, null, null));

        when(authentication.getAttributes()).thenReturn(authMap);
        when(userRepository.findById(email)).thenReturn(Optional.empty());
        when(accessControlService.getAccessControlRolesByEmail(email)).thenReturn(Optional.of(AccessControlRoles.SECONDARY_ADMIN));

        assertThrows(UnauthorisedUserException.class, () -> userAuthService.validateUser(authentication, AccessControlRoles.PRIMARY_ADMIN));
    }

}

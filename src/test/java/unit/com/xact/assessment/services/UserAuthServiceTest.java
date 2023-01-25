/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.client.UserInfoClient;
import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.repositories.UserRepository;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class UserAuthServiceTest {


    private Authentication authentication = mock(Authentication.class);
    private UserInfoClient userInfoClient = mock(UserInfoClient.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private UserAuthService userAuthService = new UserAuthService(userInfoClient, userRepository);


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
        verify(userRepository,times(0)).save(userInfo);
    }

}

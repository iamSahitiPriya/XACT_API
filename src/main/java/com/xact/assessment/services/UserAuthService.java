/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.UserInfoClient;
import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.repositories.UserRepository;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.xact.assessment.constants.AppConstants.EMAIL;
import static com.xact.assessment.constants.AppConstants.USER_ID;

@Singleton
public class UserAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthService.class);

    private UserInfoClient userInfoClient;
    private UserRepository userRepository;
    public static final String ACTIVE = "Active";


    public UserAuthService(UserInfoClient userInfoClient, UserRepository userRepository) {
        this.userInfoClient = userInfoClient;
        this.userRepository = userRepository;
    }


    public User getCurrentUser(Authentication authentication) {
        String currentEmail = (String) authentication.getAttributes().get(EMAIL);
        UserInfo userInfo = userRepository.findById(currentEmail).orElse(new UserInfo());
        userInfo.setEmail(currentEmail);
        String userId = (String) authentication.getAttributes().get(USER_ID);
        LOG.info("Logged in : {} {} {} ", userId, userInfo.getFirstName(), userInfo.getLastName());
        return new User(userId, userInfo, ACTIVE);
    }


    public void login(String authenticationHeader, User currentUser) {
        if (!userRepository.existsById(currentUser.getUserEmail())) {
            UserInfoDto userInfoDto = userInfoClient.getUserInfo(authenticationHeader);
            UserInfo userInfo = new ModelMapper().map(userInfoDto, UserInfo.class);
            userRepository.save(userInfo);
        }
    }

    public UserInfo getUserInfo(String email) {
        return userRepository.findById(email).orElse(new UserInfo(email, email, null, null));
    }
}

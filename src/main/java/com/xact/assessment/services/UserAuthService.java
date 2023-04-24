/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.UserInfoClient;
import com.xact.assessment.dtos.UserInfoDto;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import com.xact.assessment.repositories.UserRepository;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static com.xact.assessment.constants.AppConstants.EMAIL;
import static com.xact.assessment.constants.AppConstants.USER_ID;

@Singleton
public class UserAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthService.class);
    public static final String DEFAULT_LAST_NAME = "";
    public static final String DEFAULT_LOCALE = "en_US";

    private final UserInfoClient userInfoClient;
    private final UserRepository userRepository;
    private final ModuleContributorService moduleContributorService;
    public static final String ACTIVE = "Active";


    public UserAuthService(UserInfoClient userInfoClient, UserRepository userRepository, ModuleContributorService moduleContributorService) {
        this.userInfoClient = userInfoClient;
        this.userRepository = userRepository;
        this.moduleContributorService = moduleContributorService;
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
        UserInfo defaultUserInfo = new UserInfo(email, email, DEFAULT_LAST_NAME, DEFAULT_LOCALE);
        return userRepository.findById(email).orElse(defaultUserInfo);
    }

    public List<UserInfo> getLoggedInUsers(Set<String> users) {
        return userRepository.findByUsers(users);
    }

    public Set<ModuleContributor> getContributorRoles(String userEmail) {
        return moduleContributorService.getContributorsByEmail(userEmail);
    }

}

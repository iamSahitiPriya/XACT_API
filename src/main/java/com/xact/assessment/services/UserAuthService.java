/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.xact.assessment.constants.AppConstants.EMAIL;
import static com.xact.assessment.constants.AppConstants.USER_ID;

@Singleton
public class UserAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthService.class);

    public static final String ACTIVE = "Active";

    public User getLoggedInUser(Authentication authentication) {

        Profile profile = new Profile();
        profile.setEmail((String) authentication.getAttributes().get(EMAIL));
        String userId = (String) authentication.getAttributes().get(USER_ID);
        LOG.info("Logged in : {}",userId);
        return new User(userId, profile, ACTIVE);
    }


}
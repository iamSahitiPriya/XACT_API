package com.xact.assessment.services;

import com.xact.assessment.models.User;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import static com.xact.assessment.constants.AuthConstants.EMAIL;

@Singleton
public class AuthService {

    public User getLoggedInUser(Authentication authentication) {
        User user = new User();
        user.setEmail((String) authentication.getAttributes().get(EMAIL));
        return user;
    }

}

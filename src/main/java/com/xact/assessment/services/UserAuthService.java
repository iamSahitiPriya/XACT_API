package com.xact.assessment.services;

import com.xact.assessment.clients.UserClient;
import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import static com.xact.assessment.constants.AuthConstants.EMAIL;

@Singleton
public class UserAuthService {

    public static final String ACTIVE = "Active";
    private UserClient userClient;

    public UserAuthService(UserClient userClient) {
        this.userClient = userClient;
    }

    public User getLoggedInUser(Authentication authentication) {

        Profile profile = new Profile();
        profile.setEmail((String) authentication.getAttributes().get(EMAIL));
        return new User(ACTIVE, profile);
    }

    public User getActiveUser(String emailId) {
        return userClient.getActiveUser(emailId);
    }

}

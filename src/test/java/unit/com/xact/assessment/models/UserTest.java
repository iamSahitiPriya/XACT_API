/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.Profile;
import com.xact.assessment.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void getUserEmail() {
        Profile profile = new Profile();
        profile.setEmail("test@test.com");
<<<<<<< HEAD
        User user = new User(profile, "Active");
=======
        User user = new User("assxsww",profile, "Active");
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

        assertEquals(user.getUserEmail(), "test@test.com");
    }
}

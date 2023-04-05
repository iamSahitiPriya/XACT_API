/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.User;
import com.xact.assessment.models.UserInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void getUserEmail() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@test.com");


        User user = new User("assxsww", userInfo, "Active");


        assertEquals("test@test.com", user.getUserEmail());
    }
}

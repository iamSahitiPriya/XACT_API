/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.models.UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserInfoTest {

    @Test
    void shouldReturnFullName() {
        UserInfo userInfo1 = new UserInfo("test@thoughtworks.com","firstName","lastName","enUS");
        UserInfo userInfo2 = new UserInfo("test@thoughtworks.com","","lastName","enUS");
        UserInfo userInfo3 = new UserInfo("test@thoughtworks.com","firstName","","enUS");
        UserInfo userInfo4 = new UserInfo("test@thoughtworks.com","","","enUS");
        UserInfo userInfo5 = new UserInfo("test@thoughtworks.com",null,null,"enUS");
        UserInfo userInfo6 = new UserInfo("test@thoughtworks.com","firstName",null,"enUS");


        Assertions.assertEquals("firstName lastName", userInfo1.getFullName());
        Assertions.assertEquals(" lastName", userInfo2.getFullName());
        Assertions.assertEquals("firstName ", userInfo3.getFullName());
        Assertions.assertEquals(" ", userInfo4.getFullName());
        Assertions.assertEquals("null null", userInfo5.getFullName());
        Assertions.assertEquals("firstName null", userInfo6.getFullName());

    }
}

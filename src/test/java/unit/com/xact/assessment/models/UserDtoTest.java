/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDtoTest {

    @Test
    void setValidEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@thoughtworks.com");

        assertEquals(userDto.getEmail(), "test@thoughtworks.com");
    }

    @Test
    void CreateValidUser() {
        UserDto userDto = new UserDto("test@thoughtworks.com", UserRole.Owner);

        assertEquals(userDto.getEmail(), "test@thoughtworks.com");
    }

}

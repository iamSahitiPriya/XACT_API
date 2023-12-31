/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.dtos.UserDto;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class UserDtoTest {

    @Test
    void setValidEmail() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@thoughtworks.com");
        assertEquals(true,userDto.isValid("^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$"));
    }

    @Test
    void CreateInValidUser() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        assertEquals(false,userDto.isValid("^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$"));
    }

}

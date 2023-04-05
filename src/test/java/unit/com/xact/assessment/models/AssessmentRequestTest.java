/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.models;

import com.xact.assessment.dtos.AssessmentRequest;
import com.xact.assessment.dtos.UserDto;
import com.xact.assessment.dtos.UserRole;
import org.junit.jupiter.api.Test;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssessmentRequestTest {


    @Test
    void validRequest() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        List<@Valid UserDto> users = new ArrayList<>();
        UserDto user = new UserDto();
        user.setRole(UserRole.Owner);
        user.setEmail("dummy@thoughtworks.com");
        users.add(user);
        assessmentRequest.setUsers(users);
        String pattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

        assertDoesNotThrow(() -> assessmentRequest.validate(pattern));
    }

    @Test
    void invalidRequest() {
        AssessmentRequest assessmentRequest = new AssessmentRequest();
        List<@Valid UserDto> users = new ArrayList<>();
        UserDto user = new UserDto();
        user.setRole(UserRole.Owner);
        user.setEmail("dummy@gmail.com");
        users.add(user);
        assessmentRequest.setUsers(users);
        String pattern = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

        assertThrows(RuntimeException.class, () -> assessmentRequest.validate(pattern));
    }
}

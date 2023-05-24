/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.AccessControlRoles;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
@Introspected
@Setter
@NoArgsConstructor
public class AccessControlRoleDto {
    private String email;
    private AccessControlRoles accessControlRoles;
    public void validate(String pattern) {
        if (email != null && !Pattern.matches(pattern,email) ) {
                throw new UnauthorisedUserException("Invalid email of user : " + email);
            }

        }
}

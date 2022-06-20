/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private final String EMAIL_REGEX = "^([_A-Za-z0-9-+]+\\.?[_A-Za-z0-9-+]+@(thoughtworks.com))$";

    public UserDto(@Email @NotBlank String email, UserRole role) {
        setEmail(email);
        setRole(role);
    }

    @Email
    @NotBlank
    private String email;
    private UserRole role;

    public void setEmail(String email) {
        if (isValid(email))
            this.email = email;
        else
            throw new RuntimeException("Invalid email address");
    }

    private boolean isValid(String email) {
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}

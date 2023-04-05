/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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

    public UserDto(@Email @NotBlank String email, UserRole role) {
        setEmail(email);
        setRole(role);
    }

    @Email
    @NotBlank
    private String email;
    private UserRole role;

    public void setEmail(String email) {
        if (email != null && !email.isBlank())
            this.email = email;
    }

    public boolean isValid(String pattern) {
        Pattern pat = Pattern.compile(pattern);
        return pat.matcher(email).matches();
    }
}

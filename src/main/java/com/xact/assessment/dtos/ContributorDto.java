/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class ContributorDto {
    private String userEmail;
    private ContributorRole role;

    public boolean isValid(String pattern) {
        Pattern emailPattern = Pattern.compile(pattern);
        return emailPattern.matcher(userEmail).matches();

    }
}

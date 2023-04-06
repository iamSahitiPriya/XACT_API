/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Introspected
@AllArgsConstructor
public class ActivityResponse {
    private Integer identifier;
    private ActivityType activityType;
    private String fullName;
    private String email;
    private String inputText;
}

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class AssessmentRequest {

    @NotBlank
    private String assessmentName;
    @NotBlank
    private String organisationName;
    @NotBlank
    private String domain;
    @NotBlank
    private String industry;
    @Min(1)
    @Max(10000000)
    @NotNull
    private Integer teamSize;

    @NotEmpty
    private List<@Valid UserDto> users;
}

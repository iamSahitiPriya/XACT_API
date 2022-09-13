/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Introspected
@Setter
public class AssessmentModuleRequest {

    @JsonProperty
    @NotBlank
    private String moduleName;

    @JsonProperty
    private Integer category ;

    @JsonProperty
    private boolean isActive;

    @JsonProperty
    private String comments;

}

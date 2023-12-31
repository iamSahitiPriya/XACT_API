/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xact.assessment.models.Rating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParameterReferencesRequest {

    @JsonProperty
    private Integer parameter;

    @JsonProperty
    private Rating rating;

    @JsonProperty
    private String reference;

}

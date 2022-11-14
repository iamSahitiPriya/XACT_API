/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Introspected
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailPayload {

    @JsonProperty(value = "assessment_id")
    private String  assessmentId;

    @JsonProperty(value = "assessment_name")
    private String assessmentName;

    @JsonProperty(value = "organisation_name")
    private String organisationName;

    @JsonProperty(value = "created_at")
    private String createdAt;
}

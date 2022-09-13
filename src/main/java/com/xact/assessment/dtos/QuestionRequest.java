/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QuestionRequest {
    @JsonProperty
    private Integer questionId;

    @JsonProperty
    private String questionText;

    @JsonProperty
    private Integer parameter;
}

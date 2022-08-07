package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentParameterRequest {
    @JsonProperty
    private Integer parameterId;
    @JsonProperty
    private String parameterName;
    @JsonProperty
    private Integer topic;
}

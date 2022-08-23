package com.xact.assessment.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentModuleRequest {

    @JsonProperty
    private String moduleName;

    @JsonProperty
    private Integer category ;

    @JsonProperty
    private boolean isActive;

    @JsonProperty
    private String comments;

}

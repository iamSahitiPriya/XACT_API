package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@Introspected
public class AssessmentTopicRequest {
    @JsonProperty
    private Integer topicId;
    @JsonProperty
    @NotBlank
    private String topicName;
    @JsonProperty
    private Integer module;
    @JsonProperty
    private boolean isActive;
    @JsonProperty
    private String comments;
}

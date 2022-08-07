package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentTopicRequest {
    @JsonProperty
    private Integer topicId;
    @JsonProperty
    private String topicName;
    @JsonProperty
    private Integer module;
}

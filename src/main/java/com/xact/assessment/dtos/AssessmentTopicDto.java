package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentTopicDto {

    private Integer topicId;
    private String topicName;
    private Integer module;
    private Set<AssessmentParameterDto> parameters;
    private Set<AssessmentTopicReferenceDto> references;
}

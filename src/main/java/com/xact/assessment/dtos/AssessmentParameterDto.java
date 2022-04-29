package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentParameterDto {

    private Integer parameterId;
    private String parameterName;
    private Integer topic;
    private Set<QuestionDto> questions;
    private Set<AssessmentParameterReferenceDto> references;
}

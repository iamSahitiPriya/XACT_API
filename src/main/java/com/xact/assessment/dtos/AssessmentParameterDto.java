/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentParameterDto implements Comparable<AssessmentParameterDto> {

    private Integer parameterId;
    private String parameterName;
    private Integer topic;
    private SortedSet<QuestionDto> questions;
    private SortedSet<AssessmentParameterReferenceDto> references;

    @Override
    public int compareTo(AssessmentParameterDto currentParameter) {
        return parameterId - currentParameter.parameterId;
    }

}

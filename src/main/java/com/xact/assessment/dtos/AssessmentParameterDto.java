/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AssessmentParameterDto implements Comparable<AssessmentParameterDto> {

    private Integer parameterId;
    private String parameterName;
    private Integer topic;
    private boolean isActive;
    private Date updatedAt;
    private String comments;
    private boolean parameterLevelReference;
    private SortedSet<QuestionDto> questions;
    private SortedSet<AssessmentParameterReferenceDto> references;

    @Override
    public int compareTo(AssessmentParameterDto currentParameter) {
        return parameterId - currentParameter.parameterId;
    }

}

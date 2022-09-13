/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AdminAssessmentResponse {
    private Integer totalAssessments;
    private Integer totalActiveAssessments;
    private Integer totalCompleteAssessments;
}

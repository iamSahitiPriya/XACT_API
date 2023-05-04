/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.models.AssessmentParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportAnswerResponse {
    private AssessmentParameter parameter;
    private String question;
    private Integer questionId;
    private String answer;
    private Integer rating;
}

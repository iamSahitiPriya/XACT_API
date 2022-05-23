/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentResponse {

    private Integer assessmentId;

    private String assessmentName;

    private String organisationName;

    private AssessmentStatusDto assessmentStatus;

    private Date updatedAt;

}

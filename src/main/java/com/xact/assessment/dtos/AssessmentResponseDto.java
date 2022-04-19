package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentResponseDto {

    private Long assessmentId;

    private String assessmentName;

    private String organisationName;

    private AssessmentStatusDto assessmentStatus;

    private Date updatedAt;

}

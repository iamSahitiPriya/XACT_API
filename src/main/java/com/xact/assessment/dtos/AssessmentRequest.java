package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class AssessmentRequest {

    private String assessmentName;
    private String organisationName;
    private String domain;
    private String industry;
    private Integer teamSize;
    private List<UserDto> users;
}

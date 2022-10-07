package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class UserAssessmentResponse {
    private List<AssessmentCategoryDto> assessmentCategories;
    private List<AssessmentCategoryDto> userAssessmentCategories;
}

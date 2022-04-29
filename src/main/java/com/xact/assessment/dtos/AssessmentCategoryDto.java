package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentCategoryDto {

    private Integer categoryId;
    private String categoryName;
    private Set<AssessmentModuleDto> modules;
}

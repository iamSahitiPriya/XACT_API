package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentCategoryRequest {
    @JsonProperty
    private int categoryId;
    @JsonProperty
    private String categoryName;
    @JsonProperty
    private boolean isActive;

}

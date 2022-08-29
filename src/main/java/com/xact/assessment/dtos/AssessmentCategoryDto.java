/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentCategoryDto implements Comparable<AssessmentCategoryDto> {

    private Integer categoryId;
    private String categoryName;
    private SortedSet<AssessmentModuleDto> modules;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentCategoryDto that = (AssessmentCategoryDto) o;
        return Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int compareTo(AssessmentCategoryDto o) {
        return o.categoryId - categoryId;
    }
}


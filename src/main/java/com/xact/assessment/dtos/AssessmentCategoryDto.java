/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AssessmentCategoryDto implements Comparable<AssessmentCategoryDto> {

    private Integer categoryId;
    private String categoryName;
    private boolean active;
    private SortedSet<AssessmentModuleDto> modules;


    @Override
    public int compareTo(AssessmentCategoryDto o) {
        return o.categoryId - categoryId;
    }
}


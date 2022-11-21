/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class CategoryDto implements Comparable<CategoryDto> {

    private Integer categoryId;
    private String categoryName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;
    private SortedSet<AssessmentModuleDto> modules;


    @Override
    public int compareTo(CategoryDto o) {
        return o.updatedAt.compareTo(updatedAt);
    }

}


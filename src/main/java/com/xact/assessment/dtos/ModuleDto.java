/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.models.AssessmentCategory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class ModuleDto implements Comparable<ModuleDto> {

    private CategoryDto category;
    private Integer moduleId ;
    private String moduleName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;

    @Override
    public int compareTo(ModuleDto moduleDto) {
        return moduleDto.updatedAt.compareTo(updatedAt);
    }
}
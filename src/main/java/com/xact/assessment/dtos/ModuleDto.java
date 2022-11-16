/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
@AllArgsConstructor
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



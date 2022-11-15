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

    private Integer moduleId ;
    private String moduleName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;
    private List<TopicDto> topics;

    @Override
    public int compareTo(ModuleDto moduleDto) {
        return moduleDto.updatedAt.compareTo(updatedAt);
    }
}



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
public class AssessmentModuleDto implements Comparable<AssessmentModuleDto> {

    private Integer moduleId;
    private String moduleName;
    private Integer category;
    private SortedSet<AssessmentTopicDto> topics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentModuleDto that = (AssessmentModuleDto) o;
        return Objects.equals(moduleId, that.moduleId);
    }


    @Override
    public int compareTo(AssessmentModuleDto currentModule) {
        return moduleId - currentModule.moduleId;
    }
}

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
@Setter
@EqualsAndHashCode
public class AssessmentModuleDto implements Comparable<AssessmentModuleDto> {

    private Integer moduleId;
    private String moduleName;
    private Integer category;
    private boolean active;
    private Date updatedAt;
    private String comments;
    private SortedSet<AssessmentTopicDto> topics;
    @Override
    public int compareTo(AssessmentModuleDto currentModule) {
        return moduleId - currentModule.moduleId;
    }

}

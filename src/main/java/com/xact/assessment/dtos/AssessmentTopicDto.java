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
public class AssessmentTopicDto implements Comparable<AssessmentTopicDto> {

    private Integer topicId;
    private String topicName;
    private Integer module;
    private Date updatedAt;
    private String comments;
    private boolean active;
    private SortedSet<AssessmentParameterDto> parameters;
    private SortedSet<AssessmentTopicReferenceDto> references;


    @Override
    public int compareTo(AssessmentTopicDto currentTopic) {
        return topicId - currentTopic.topicId;
    }

}

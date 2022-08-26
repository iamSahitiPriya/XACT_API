/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentTopicDto implements Comparable<AssessmentTopicDto> {

    private Integer topicId;
    private String topicName;
    private Integer module;
    private SortedSet<AssessmentParameterDto> parameters;
    private SortedSet<AssessmentTopicReferenceDto> references;
    private String assessmentLevel;

    @Override
    public int compareTo(AssessmentTopicDto currentTopic) {
        return topicId - currentTopic.topicId;
    }

    public AssessmentTopicDto(Integer topicId, String topicName, Integer module) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.module = module;
    }

}

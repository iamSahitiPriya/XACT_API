package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.SortedSet;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentTopicDto implements Comparable<AssessmentTopicDto> {

    private Integer topicId;
    private String topicName;
    private Integer module;
    private SortedSet<AssessmentParameterDto> parameters;
    private Set<AssessmentTopicReferenceDto> references;
    private String assessmentLevel;

    @Override
    public int compareTo(AssessmentTopicDto currentTopic) {
        return topicId - currentTopic.topicId;
    }


}

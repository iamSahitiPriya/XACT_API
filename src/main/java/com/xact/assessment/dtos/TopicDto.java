/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
public class TopicDto implements Comparable<TopicDto> {

    private Integer topicId ;
    private String topicName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;
    private List<ParameterDto> parameters;

    @Override
    public int compareTo(TopicDto topicDto) {
        return topicDto.updatedAt.compareTo(updatedAt);
    }

}


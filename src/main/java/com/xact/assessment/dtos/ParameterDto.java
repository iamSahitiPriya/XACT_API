/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@Setter
@AllArgsConstructor
public class ParameterDto implements  Comparable<ParameterDto>{

    private TopicDto topic;
    private Integer parameterId ;
    private String parameterName;
    private boolean isActive;
    private Date updatedAt;
    private String comments;

    @Override
    public int compareTo(ParameterDto parameterDto) {
        return parameterDto.updatedAt.compareTo(updatedAt);
    }

}


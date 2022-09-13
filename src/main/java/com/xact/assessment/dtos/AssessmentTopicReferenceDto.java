/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class AssessmentTopicReferenceDto implements Comparable<AssessmentTopicReferenceDto> {

    private Integer referenceId;
    private Integer topic;
    private RatingDto rating;
    private String reference;

    @Override
    public int compareTo(AssessmentTopicReferenceDto reference) {
        return reference.getRating().getValue() - rating.getValue();
    }
}

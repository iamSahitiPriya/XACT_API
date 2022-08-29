/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentTopicReferenceDto implements Comparable<AssessmentTopicReferenceDto> {

    private Integer referenceId;
    private Integer topic;
    private RatingDto rating;
    private String reference;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       AssessmentTopicReferenceDto that = (AssessmentTopicReferenceDto) o;
        return Objects.equals(referenceId, that.referenceId);
    }

    @Override
    public int compareTo(AssessmentTopicReferenceDto reference) {
        return reference.getRating().getValue() - rating.getValue();
    }
}

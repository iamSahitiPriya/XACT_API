/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AssessmentQuestionReferenceDto implements Comparable<AssessmentQuestionReferenceDto> {
    private Integer referenceId;
    private Integer question;
    private RatingDto rating;
    private String reference;

    @Override
    public int compareTo(AssessmentQuestionReferenceDto reference) {
        return reference.getRating().getValue() - rating.getValue();
    }
}

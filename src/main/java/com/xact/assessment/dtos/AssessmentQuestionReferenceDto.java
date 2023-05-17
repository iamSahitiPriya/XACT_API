/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

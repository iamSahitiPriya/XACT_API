/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AssessmentParameterReferenceDto implements Comparable<AssessmentParameterReferenceDto> {

    private Integer referenceId;
    private Integer parameter;
    private RatingDto rating;
    private String reference;

    @Override
    public int compareTo(AssessmentParameterReferenceDto reference) {
        return reference.getRating().getValue() - rating.getValue();
    }
}

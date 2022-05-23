/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class AssessmentScore {
    @Column(name = "score")
    private Double score;

    @Column(name = "recommendation")
    private String recommendation;
}

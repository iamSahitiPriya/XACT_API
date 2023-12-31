/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ParameterLevelId implements Serializable {

    @ManyToOne
    @MapsId
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "parameter_id")
    private AssessmentParameter parameter;

}

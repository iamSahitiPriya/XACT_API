/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name = "tbl_assessment_parameter_recommendation")
public class ParameterLevelRecommendation extends Recommendation {
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "parameter", referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

}

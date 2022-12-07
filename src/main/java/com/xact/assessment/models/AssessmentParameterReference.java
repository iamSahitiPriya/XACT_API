/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tbm_assessment_param_reference")
public class AssessmentParameterReference {

    @Id
    @Column(name = "reference_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer referenceId;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "parameter", referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "reference")
    private String reference;

    public AssessmentParameterReference(AssessmentParameter parameter, Rating rating, String reference) {
        this.parameter = parameter;
        this.rating = rating;
        this.reference = reference;
    }
}

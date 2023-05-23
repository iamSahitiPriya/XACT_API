/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "tbm_assessment_param_reference")
public class AssessmentParameterReference extends Reference implements Serializable {

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "parameter", referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;

    public AssessmentParameterReference(Rating rating, String reference, AssessmentParameter parameter) {
        super(rating, reference);
        this.parameter = parameter;
    }
}

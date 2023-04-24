/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Entity
@Table(name = "tbl_assessment_parameter")
public class ParameterLevelRating extends Rating implements Serializable {
    @EmbeddedId
    @AttributeOverride(name = "assessmentParameter", column = @Column(name = "parameter_id"))
    @AttributeOverride(name = "assessment", column = @Column(name = "assessment_id"))
    public ParameterLevelId parameterLevelId;

    public ParameterLevelRating(Integer rating, Date createdAt, Date updatedAt, ParameterLevelId parameterLevelId) {
        super(rating, createdAt, updatedAt);
        this.parameterLevelId = parameterLevelId;
    }
}

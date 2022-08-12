/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"questions", "references"})
@Introspected
@Entity
@Table(name = "tbm_assessment_parameter")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "parameterId")
public class AssessmentParameter {
    @Id
    @Column(name = "parameter_id", nullable = false, unique = true)
    private Integer parameterId;

    @NotNull
    @Column(name = "parameter_name")
    private String parameterName;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parameter")
    @ElementCollection()
    private Set<Question> questions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parameter")
    @ElementCollection()
    private Set<AssessmentParameterReference> references;

    @Transient
    private Integer rating;

    public Integer getRating() {
        return rating == null ? 0 : rating;
    }
}

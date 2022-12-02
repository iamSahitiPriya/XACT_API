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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @NotNull
    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "comments")
    private String comments;

    public boolean getIsActive() {
        return isActive;
    }

    @Transient
    private Integer rating;

    public Integer getRating() {
        return rating == null ? 0 : rating;
    }

    public AssessmentParameter(String parameterName, AssessmentTopic topic, boolean isActive, String comments) {
        this.parameterName = parameterName;
        this.topic = topic;
        this.isActive = isActive;
        this.comments = comments;
    }

    public AssessmentParameter(Integer parameterId,String parameterName, AssessmentTopic topic, boolean isActive, String comments) {
        this.parameterName = parameterName;
        this.topic = topic;
        this.isActive = isActive;
        this.comments = comments;
    }
}
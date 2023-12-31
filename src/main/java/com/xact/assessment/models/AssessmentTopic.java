/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"parameters", "references"})
@Introspected
@Entity
@Table(name = "tbm_assessment_topic")
public class AssessmentTopic implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id", nullable = false, unique = true)
    private Integer topicId;

    @NotNull
    @Column(name = "topic_name")
    private String topicName;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "module", referencedColumnName = "module_id")
    private AssessmentModule module;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentParameter> parameters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    @ElementCollection()
    private Set<AssessmentTopicReference> references;

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

    @Column(name="has_reference")
    private boolean isTopicLevelReference;

    public boolean getIsActive() {
        return isActive;
    }

    public Set<AssessmentParameter> getActiveParameters() {
        return parameters == null ? null : parameters.stream().filter(AssessmentParameter::getIsActive).collect(Collectors.toSet());
    }

    @Transient
    private Integer rating;

    public Integer getRating() {
        return rating == null ? 0 : rating;
    }

    public double getTopicAverage() {
        if (this.hasReferences()) {
            return this.getRating();
        }
        Double averageRating = getParameterAverageRating();
        if (averageRating == null) return 0;
        return averageRating;
    }

    private Double getParameterAverageRating() {
        double parameterSum = 0;
        int parameterCount = 0;
        double averageRating=0;
        for (AssessmentParameter assessmentParameter : this.parameters) {
            if (assessmentParameter.hasReferences()) {
                averageRating = assessmentParameter.getRating();
            } else {
                averageRating = assessmentParameter.getQuestionAverage();
            }
            parameterSum += averageRating;
            parameterCount += 1;

        }
            if (parameterSum == 0 && parameterCount == 0) {
                return null;
            }
        return parameterSum / parameterCount;
    }

    public boolean hasReferences() {
        return references != null && !references.isEmpty();
    }

    public AssessmentTopic(String topicName, AssessmentModule module, boolean isActive, String comments) {
        this.topicName = topicName;
        this.module = module;
        this.isActive = isActive;
        this.comments = comments;
    }

    public AssessmentTopic(Integer topicId, String topicName, AssessmentModule module, boolean isActive, String comments) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.module = module;
        this.isActive = isActive;
        this.comments = comments;
    }
}

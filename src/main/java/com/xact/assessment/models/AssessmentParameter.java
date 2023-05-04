/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(exclude = {"questions", "references"})
@Introspected
@Entity
@Table(name = "tbm_assessment_parameter")
public class AssessmentParameter implements Serializable {
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
    @ElementCollection()
    private Set<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parameter")
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

    public boolean hasReferences() {
        return references != null && !references.isEmpty();
    }

    public Double getQuestionAverage() {
        double questionSum = 0;
        int questionCount = 0;
        double averageRating=0;
        for (Question question : this.questions) {
            if (question.hasReferences()) {
                double questionAverageScore = question.getRating();
                if (questionAverageScore != 0) {
                    questionSum += questionAverageScore;
                    questionCount+= 1;
                }

            }
        }
        if (questionSum == 0 && questionCount == 0) {
            return averageRating;
        }
        averageRating = questionSum / questionCount;
        return averageRating;
    }
}

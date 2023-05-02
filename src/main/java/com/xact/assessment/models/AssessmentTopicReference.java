/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "tbm_assessment_topic_reference")
public class AssessmentTopicReference implements Serializable {

    @Id
    @Column(name = "reference_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer referenceId;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "reference")
    private String reference;

    public AssessmentTopicReference(AssessmentTopic topic, Rating rating, String reference) {
        this.topic = topic;
        this.rating = rating;
        this.reference = reference;
    }
}

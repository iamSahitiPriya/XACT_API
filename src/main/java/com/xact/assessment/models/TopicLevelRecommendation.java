/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name = "tbl_assessment_topic_recommendation")
public class TopicLevelRecommendation extends Recommendation {
    @NotNull
    @ManyToOne()
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;
<<<<<<< HEAD


    @Column(name = "recommendation")
    private String recommendation;


    @Column(name = "impact", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationImpact recommendationImpact;


    @Column(name = "effort", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationEffort recommendationEffort;


    @Column(name = "delivery_horizon", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationDeliveryHorizon deliveryHorizon;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public boolean hasRecommendation() {
        return (this.recommendation != null && !this.recommendation.isBlank());
    }

=======
>>>>>>> 2b32ebc ([331][Brindha | Sahiti]Refactor. implementation of recommendation by inheritance)
}

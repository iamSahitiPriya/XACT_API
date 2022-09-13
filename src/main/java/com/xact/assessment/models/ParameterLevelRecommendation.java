/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.models;


import io.micronaut.core.annotation.Introspected;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@EqualsAndHashCode
@Entity
@Table(name = "tbl_assessment_parameter_recommendation")
public class ParameterLevelRecommendation{

    @Id
    @Column(name = "recommendation_id",unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recommendationId;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "assessment", referencedColumnName = "assessment_id")
    private Assessment assessment;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "parameter", referencedColumnName = "parameter_id")
    private AssessmentParameter parameter;


    @Column(name = "recommendation")
    private String recommendation;


    @Column(name = "impact", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationImpact recommendationImpact;


    @Column(name = "effort", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationEffort recommendationEffort;


    @Column(name = "delivery_horizon", nullable = false, unique = true)
    private String deliveryHorizon;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    public  boolean hasRecommendation() {
        return ((this.recommendation != null && !this.recommendation.isBlank()) ||
                (this.recommendationEffort !=null && !this.recommendationEffort.toString().isBlank()) ||
                (this.recommendationImpact !=null && !this.recommendationImpact.toString().isBlank())||
                (this.deliveryHorizon !=null && !this.deliveryHorizon.isBlank()));
    }

}

package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tbl_assessment_topic_recommendation")
public class TopicLevelRecommendation{

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
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @Column(name = "recommendation")
    private String recommendation;

    @NotNull
    @Column(name = "impact", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationImpact recommendationImpact;

    @NotNull
    @Column(name = "effect", nullable = false)
    @Enumerated(EnumType.STRING)
    private RecommendationEffect recommendationEffect;

    @NotNull
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

    public boolean hasRecommendation() {
        return ((this.recommendation != null && !this.recommendation.isBlank()) ||
                (this.recommendationEffect !=null && !this.recommendationEffect.toString().isBlank()) ||
                (this.recommendationImpact !=null && !this.recommendationImpact.toString().isBlank())||
                (this.deliveryHorizon !=null && !this.deliveryHorizon.isBlank()));
    }
}

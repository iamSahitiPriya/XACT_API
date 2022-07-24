package com.xact.assessment.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Entity
@Table(name = "tbl_assessment_topic_recommendation")
public class TopicLevelRecommendation{

    @Id
    @Column(name = "recommendation_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recommendationId;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "assessment", referencedColumnName = "assessment_id")
    private Assessment assessment;

    @NotNull
    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "topic", referencedColumnName = "topic_id")
    private AssessmentTopic topic;

    @NotNull
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

}

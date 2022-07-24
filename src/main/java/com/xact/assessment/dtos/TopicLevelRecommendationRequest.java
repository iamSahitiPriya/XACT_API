package com.xact.assessment.dtos;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TopicLevelRecommendationRequest {
    private Integer topicId;
    private Integer recommendationId;
    private Integer assessmentId;
    private String recommendation;
    private String impact;
    private String effect;
    private String deliveryHorizon;
}

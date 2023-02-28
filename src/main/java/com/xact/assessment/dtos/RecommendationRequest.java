/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

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
public class RecommendationRequest {
    private Integer recommendationId;
    private String recommendation;
    private RecommendationImpact impact;
    private RecommendationEffort effort;
    private RecommendationDeliveryHorizon deliveryHorizon;
}

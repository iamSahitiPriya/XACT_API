/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import com.xact.assessment.models.RecommendationDeliveryHorizon;
import com.xact.assessment.models.RecommendationEffort;
import com.xact.assessment.models.RecommendationImpact;
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
public class ParameterLevelRecommendationRequest {
    private Integer recommendationId;
    private String recommendation;
    private RecommendationImpact impact;
    private RecommendationEffort effort;
    private RecommendationDeliveryHorizon deliveryHorizon;
}

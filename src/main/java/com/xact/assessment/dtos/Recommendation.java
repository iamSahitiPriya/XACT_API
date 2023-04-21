/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Recommendation {
    private Integer recommendationId;
    private String recommendation;
    private RecommendationDeliveryHorizon deliveryHorizon;
    private RecommendationImpact impact;
    private RecommendationEffort effort;
    private String categoryName;
    private Date updatedAt;
    private Integer topic;
    private Integer module;
    private Integer category;

    public static int compareByDeliveryHorizon(Recommendation recommendation1, Recommendation recommendation2) {
        return recommendation1.getDeliveryHorizon().compareTo(recommendation2.getDeliveryHorizon());
    }

    public static int compareByUpdatedTime(Recommendation recommendation1, Recommendation recommendation2) {
        return recommendation2.getUpdatedAt().compareTo(recommendation1.getUpdatedAt());
    }
}

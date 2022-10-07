/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.List;
import java.util.Objects;


@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class ParameterRatingAndRecommendation {

    private Integer parameterId;
    private Integer rating;

    @JsonProperty("parameterLevelRecommendation")
    private List<ParameterLevelRecommendationRequest> parameterLevelRecommendationRequest;


}

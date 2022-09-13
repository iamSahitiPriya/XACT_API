/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.dtos;


import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ParameterLevelAssessmentRequest {

    private List<AnswerRequest> answerRequest;
    private ParameterRatingAndRecommendation parameterRatingAndRecommendation;

}

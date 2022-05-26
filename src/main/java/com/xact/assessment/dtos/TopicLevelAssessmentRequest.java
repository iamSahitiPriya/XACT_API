package com.xact.assessment.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TopicLevelAssessmentRequest {
    @JsonProperty("parameterLevel")
    private List<ParameterLevelAssessmentRequest> parameterLevelAssessmentRequestList;
    private TopicRatingAndRecommendation topicRatingAndRecommendation;

    public boolean isRatedAtTopicLevel(){
        return topicRatingAndRecommendation != null;
    }
}


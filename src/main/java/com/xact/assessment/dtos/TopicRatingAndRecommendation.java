package com.xact.assessment.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Introspected
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TopicRatingAndRecommendation {
    private Integer topicId;
    private Integer rating;

    @JsonProperty("topicLevelRecommendation")
    private List<TopicLevelRecommendationRequest> topicLevelRecommendationRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicRatingAndRecommendation that = (TopicRatingAndRecommendation) o;
        return Objects.equals(topicId, that.topicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicId);
    }

}

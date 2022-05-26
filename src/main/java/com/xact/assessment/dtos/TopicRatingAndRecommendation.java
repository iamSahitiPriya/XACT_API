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
public class TopicRatingAndRecommendation {
    private Integer topicId;
    private Integer rating;
    private String recommendation;

    public void setRating(RatingDto ratingDto) {
        rating = ratingDto.value();
    }
}

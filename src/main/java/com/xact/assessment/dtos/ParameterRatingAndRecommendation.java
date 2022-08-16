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
public class ParameterRatingAndRecommendation {

    private Integer parameterId;
    private Integer rating;
    @JsonProperty("parameterLevelRecommendation")
    private List<ParameterLevelRecommendationRequest> parameterLevelRecommendationRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
       ParameterRatingAndRecommendation that = (ParameterRatingAndRecommendation) o;
        return Objects.equals(parameterId, that.parameterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameterId);
    }

}

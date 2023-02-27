/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterLevelRecommendationRequest;
import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.RecommendationImpact;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelRatingRepository;
import com.xact.assessment.services.ParameterLevelRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static org.mockito.Mockito.*;

class ParameterLevelRatingServiceTest {
    private ModelMapper mapper = new ModelMapper();
    private ParameterLevelRatingService parameterLevelRatingService;
    private ParameterLevelRatingRepository parameterLevelRatingRepository;

    @BeforeEach
    public void beforeEach() {
        parameterLevelRatingRepository = mock(ParameterLevelRatingRepository.class);
        parameterLevelRatingService = new ParameterLevelRatingService(parameterLevelRatingRepository);
    }

    @Test
    void shouldSaveParameterLevelRating() {
        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);

        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.HIGH);
        parameterLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelRating parameterLevelRating = mapper.map(parameterRatingAndRecommendation, ParameterLevelRating.class);
        parameterLevelRating.setParameterLevelId(parameterLevelId);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(parameterLevelRatingRepository.save(parameterLevelRating)).thenReturn(parameterLevelRating);
        parameterLevelRatingService.save(parameterLevelRating);

        verify(parameterLevelRatingRepository).save(parameterLevelRating);
    }

    @Test
    void shouldDeleteParameterLevelRating() {
        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendationRequest.setEffort(RecommendationEffort.LOW);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelRating parameterLevelRating = mapper.map(parameterRatingAndRecommendation, ParameterLevelRating.class);

        parameterLevelRating.setParameterLevelId(parameterLevelId);

        parameterLevelRatingService.save(parameterLevelRating);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        doNothing().when(parameterLevelRatingRepository).delete(parameterLevelRating);;
        parameterLevelRatingService.delete(parameterLevelRating);
        verify(parameterLevelRatingRepository).delete(parameterLevelRating);
    }
}

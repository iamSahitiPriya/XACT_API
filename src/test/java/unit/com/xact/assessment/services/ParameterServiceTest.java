/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.dtos.RecommendationImpact;
import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.services.AssessmentParameterReferenceService;
import com.xact.assessment.services.ParameterLevelRatingService;
import com.xact.assessment.services.ParameterLevelRecommendationService;
import com.xact.assessment.services.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static com.xact.assessment.dtos.RecommendationEffort.HIGH;
import static com.xact.assessment.dtos.RecommendationEffort.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ParameterServiceTest {

    private ModelMapper mapper = new ModelMapper();
    private ParameterService parameterService;
    private AssessmentParameterRepository assessmentParameterRepository;

    private ParameterLevelRatingService parameterLevelRatingService;
    private ParameterLevelRecommendationService parameterLevelRecommendationService;
    private AssessmentParameterReferenceService assessmentParameterReferenceService;

    @BeforeEach
    public void beforeEach() {
        assessmentParameterRepository = mock(AssessmentParameterRepository.class);
        parameterLevelRatingService = mock(ParameterLevelRatingService.class);
        parameterLevelRecommendationService = mock(ParameterLevelRecommendationService.class);
        assessmentParameterReferenceService = mock(AssessmentParameterReferenceService.class);
        parameterService = new ParameterService(assessmentParameterRepository, parameterLevelRatingService, parameterLevelRecommendationService, assessmentParameterReferenceService);
    }

    @Test
    void shouldGetDetailsForParticularParameterId() {
        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.findById(parameterId)).thenReturn(Optional.of(assessmentParameter));
        Optional<AssessmentParameter> actualParameter = parameterService.getParameter(parameterId);

        assertEquals(assessmentParameter.getParameterId(), actualParameter.get().getParameterId());
        assertEquals(assessmentParameter.getParameterName(), actualParameter.get().getParameterName());

    }

    @Test
    void shouldSaveParameterWhenInputsAreGiven() {
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.save(assessmentParameter)).thenReturn(assessmentParameter);
        parameterService.createParameter(assessmentParameter);

        verify(assessmentParameterRepository).save(assessmentParameter);
    }

    @Test
    void shouldUpdateParameter() {
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.update(assessmentParameter)).thenReturn(assessmentParameter);
        parameterService.updateParameter(assessmentParameter);

        verify(assessmentParameterRepository).update(assessmentParameter);
    }

    @Test()
    void shouldSaveAssessmentRatingAndRecommendationForParameterLevel() {
        Integer assessmentId = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(parameterId);

        parameterRatingAndRecommendation.setRating(1);

        RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationText("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.LOW);
        parameterLevelRecommendationRequest.setEffort(LOW);

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);


        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);


        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment);
        ParameterLevelRating parameterLevelRating = mapper.map(parameterRatingAndRecommendation, ParameterLevelRating.class);
        parameterLevelRating.setParameterLevelId(parameterLevelId);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));
        when(parameterLevelRatingService.save(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveParameterRating(parameterLevelRating);

        when(parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment,assessmentParameter)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment,parameterId);


        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRating.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendationText(), actualResponse1.getRecommendationText());

    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendationText("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendationRequest.setEffort(LOW);

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

        parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter);


        when(parameterLevelRatingService.existsById(parameterLevelRating)).thenReturn(true);
        when(parameterLevelRatingService.update(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveParameterRating(parameterLevelRating);

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));
        when(parameterLevelRecommendationService.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter)).thenReturn(parameterLevelRecommendation);

        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,parameterId);

        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRating.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendationText(), actualResponse1.getRecommendationText());

    }

    @Test
    void shouldReturnParameterAssessmentData() {
        Integer assessmentId = 1;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");
        ParameterLevelId parameterLevelId = new ParameterLevelId();
        parameterLevelId.setAssessment(assessment);
        parameterLevelId.setParameter(assessmentParameter);

        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        parameterLevelRating.setParameterLevelId(parameterLevelId);

        parameterLevelRating.setRating(2);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setRecommendationText("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon(LATER);
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);

        when(parameterLevelRatingService.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRating));

        List<ParameterLevelRating> parameterLevelRatingList = parameterService.getParameterLevelRatings(assessmentId);

        when(parameterLevelRecommendationService.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterService.getParameterRecommendations(assessmentId);

        assertEquals(parameterLevelRatingList.get(0).getRating(), parameterLevelRating.getRating());
        assertEquals(parameterLevelRecommendationList.get(0).getRecommendationText(), parameterLevelRecommendation.getRecommendationText());
    }

    @Test
    void shouldDeleteRatingForParameterLevelAssessment() {
        Integer assessmentId1 = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation1 = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation1.setParameterId(1);
        parameterRatingAndRecommendation1.setRating(1);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation1, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelRating parameterLevelRating = mapper.map(parameterRatingAndRecommendation1, ParameterLevelRating.class);
        parameterLevelRating.setParameterLevelId(parameterLevelId);

        parameterLevelRatingService.save(parameterLevelRating);

        parameterLevelRating.setRating(null);

        when(parameterLevelRatingService.existsById(parameterLevelRating)).thenReturn(true);
        parameterService.saveParameterRating(parameterLevelRating);

        verify(parameterLevelRatingService).delete(parameterLevelRating);
    }

    @Test
    void shouldDeleteRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;
        Integer parameterId = 1;
        Integer recommendationId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(recommendationId);
        parameterLevelRecommendationRequest.setRecommendationText("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendationRequest.setEffort(LOW);


        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter)).thenReturn(parameterLevelRecommendation);
        doNothing().when(parameterLevelRecommendationService).deleteById(parameterLevelRecommendationRequest.getRecommendationId());


        parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter);
        parameterLevelRecommendationService.deleteById(parameterLevelRecommendationRequest.getRecommendationId());

        verify(parameterLevelRecommendationService).deleteById(parameterLevelRecommendationRequest.getRecommendationId());
    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevelWhenRecommendationIsEAmpty() {

        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendationText("");
        parameterLevelRecommendationRequest.setDeliveryHorizon(LATER);
        parameterLevelRecommendationRequest.setImpact(RecommendationImpact.MEDIUM);
        parameterLevelRecommendationRequest.setEffort(LOW);

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

        parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter);

        when(parameterService.getParameter(parameterId)).thenReturn(Optional.of(assessmentParameter));
        when(parameterLevelRatingService.existsById(parameterLevelRating)).thenReturn(true);
        when(parameterLevelRatingService.update(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveParameterRating(parameterLevelRating);


        when(parameterLevelRecommendationService.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,assessmentParameter)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterRecommendation(parameterLevelRecommendationRequest,assessment1,parameterId);

        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRecommendation.getRecommendationText(), actualResponse1.getRecommendationText());

    }

    @Test
    void shouldGetAllParameters() {
        List<AssessmentParameter> assessmentParameters = new ArrayList<>();
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameters.add(assessmentParameter);

        when(assessmentParameterRepository.findAll()).thenReturn(assessmentParameters);
        parameterService.getAllParameters();

        verify(assessmentParameterRepository).findAll();

    }
}

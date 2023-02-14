package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterLevelRecommendationRequest;
import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
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

import static com.xact.assessment.models.RecommendationEffort.HIGH;
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

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other text");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");

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


        when(parameterLevelRatingService.save(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelRating);

        when(parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);


        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRating.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

    }

    @Test
    void shouldUpdateAssessmentRatingAndRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;

        Integer parameterId = 1;

        ParameterRatingAndRecommendation parameterRatingAndRecommendation = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation.setParameterId(1);
        parameterRatingAndRecommendation.setRating(1);

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");

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

        parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation);


        when(parameterLevelRatingService.existsById(parameterLevelRating)).thenReturn(true);
        when(parameterLevelRatingService.update(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelRating);


        when(parameterLevelRecommendationService.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);

        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRating.getParameterLevelId(), actualResponse.getParameterLevelId());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

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
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon("some dummy text");
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);

        when(parameterLevelRatingService.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRating));

        List<ParameterLevelRating> parameterLevelRatingList = parameterService.getParameterAssessmentData(assessmentId);

        when(parameterLevelRecommendationService.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterService.getAssessmentParameterRecommendationData(assessmentId);

        assertEquals(parameterLevelRatingList.get(0).getRating(), parameterLevelRating.getRating());
        assertEquals(parameterLevelRecommendationList.get(0).getRecommendation(), parameterLevelRecommendation.getRecommendation());
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
        parameterService.saveRatingAndRecommendation(parameterLevelRating);

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

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(recommendationId);
        parameterLevelRecommendationRequest.setRecommendation("some text");
        parameterLevelRecommendationRequest.setDeliveryHorizon("some other teext");
        parameterLevelRecommendationRequest.setImpact("HIGH");
        parameterLevelRecommendationRequest.setEffort("LOW");


        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        when(parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        doNothing().when(parameterLevelRecommendationService).deleteById(parameterLevelRecommendationRequest.getRecommendationId());


        parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation);
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

        ParameterLevelRecommendationRequest parameterLevelRecommendationRequest = new ParameterLevelRecommendationRequest();
        parameterLevelRecommendationRequest.setRecommendationId(1);
        parameterLevelRecommendationRequest.setRecommendation("");
        parameterLevelRecommendationRequest.setDeliveryHorizon("");
        parameterLevelRecommendationRequest.setImpact("");
        parameterLevelRecommendationRequest.setEffort("");

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

        parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation);


        when(parameterLevelRatingService.existsById(parameterLevelRating)).thenReturn(true);
        when(parameterLevelRatingService.update(parameterLevelRating)).thenReturn(parameterLevelRating);
        ParameterLevelRating actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelRating);


        when(parameterLevelRecommendationService.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelRating.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

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
    @Test
    void shouldGetListOfParametersInDescOrder() {
        List<AssessmentParameter> assessmentParameters = new ArrayList<>();
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameters.add(assessmentParameter);

        when(assessmentParameterRepository.listOrderByUpdatedAtDesc()).thenReturn(assessmentParameters);
        parameterService.getParameters();

        verify(assessmentParameterRepository).listOrderByUpdatedAtDesc();

    }
}
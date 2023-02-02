package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ParameterLevelRecommendationRequest;
import com.xact.assessment.dtos.ParameterRatingAndRecommendation;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.AssessmentParameterReferenceRepository;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.services.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

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

    private ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private  ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    private  AssessmentParameterReferenceRepository assessmentParameterRRepository;

    @BeforeEach
    public void beforeEach() {
        assessmentParameterRepository = mock(AssessmentParameterRepository.class);
        parameterLevelAssessmentRepository=mock(ParameterLevelAssessmentRepository.class);
        parameterLevelRecommendationRepository=mock(ParameterLevelRecommendationRepository.class);
        assessmentParameterRRepository=mock(AssessmentParameterReferenceRepository.class);
        parameterService = new ParameterService(assessmentParameterRepository, parameterLevelAssessmentRepository, parameterLevelRecommendationRepository, assessmentParameterRRepository);
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
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);


        when(parameterLevelAssessmentRepository.save(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelAssessment);

        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);


        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
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
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);

        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);


        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelAssessment);


        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationRepository.update(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 =parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelAssessment.getParameterLevelId(), actualResponse.getParameterLevelId());
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

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessment.setRating(2);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setParameter(assessmentParameter);
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setRecommendation("some recommendation");
        parameterLevelRecommendation.setDeliveryHorizon("some dummy text");
        parameterLevelRecommendation.setRecommendationImpact(RecommendationImpact.LOW);

        when(parameterLevelAssessmentRepository.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelAssessment));

        List<ParameterLevelAssessment> parameterLevelAssessmentList = parameterService.getParameterAssessmentData(assessmentId);

        when(parameterLevelRecommendationRepository.findByAssessment(assessmentId)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<ParameterLevelRecommendation> parameterLevelRecommendationList=parameterService.getAssessmentParameterRecommendationData(assessmentId);

        assertEquals(parameterLevelAssessmentList.get(0).getRating(), parameterLevelAssessment.getRating());
        assertEquals(parameterLevelRecommendationList.get(0).getRecommendation(), parameterLevelRecommendation.getRecommendation());
    }

    @Test
    void shouldDeleteRatingForParameterLevelAssessment(){
        Integer assessmentId1 = 1;
        ParameterRatingAndRecommendation parameterRatingAndRecommendation1 = new ParameterRatingAndRecommendation();
        parameterRatingAndRecommendation1.setParameterId(1);
        parameterRatingAndRecommendation1.setRating(1);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessmentId1);

        ParameterLevelId parameterLevelId = mapper.map(parameterRatingAndRecommendation1, ParameterLevelId.class);
        parameterLevelId.setAssessment(assessment1);
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation1, ParameterLevelAssessment.class);
        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);

        parameterLevelAssessment.setRating(null);

        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        parameterService.saveRatingAndRecommendation(parameterLevelAssessment);

        verify(parameterLevelAssessmentRepository).delete(parameterLevelAssessment);
    }
    @Test
    void shouldDeleteRecommendationForParameterLevel() {

        Integer assessmentId1 = 1;
        Integer parameterId = 1;
        Integer recommendationId=1;

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

        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        doNothing().when(parameterLevelRecommendationRepository).deleteById(parameterLevelRecommendationRequest.getRecommendationId());


        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);
        parameterLevelRecommendationRepository.deleteById(parameterLevelRecommendationRequest.getRecommendationId());

        verify(parameterLevelRecommendationRepository).deleteById(parameterLevelRecommendationRequest.getRecommendationId());
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
        ParameterLevelAssessment parameterLevelAssessment = mapper.map(parameterRatingAndRecommendation, ParameterLevelAssessment.class);

        parameterLevelAssessment.setParameterLevelId(parameterLevelId);

        parameterLevelAssessmentRepository.save(parameterLevelAssessment);


        ParameterLevelRecommendation parameterLevelRecommendation = mapper.map(parameterLevelRecommendationRequest, ParameterLevelRecommendation.class);
        parameterLevelRecommendation.setAssessment(assessment1);
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(parameterId);
        parameterLevelRecommendation.setParameter(assessmentParameter);

        parameterLevelRecommendationRepository.save(parameterLevelRecommendation);


        when(parameterLevelAssessmentRepository.existsById(parameterLevelId)).thenReturn(true);
        when(parameterLevelAssessmentRepository.update(parameterLevelAssessment)).thenReturn(parameterLevelAssessment);
        ParameterLevelAssessment actualResponse = parameterService.saveRatingAndRecommendation(parameterLevelAssessment);


        when(parameterLevelRecommendationRepository.existsById(parameterLevelRecommendationRequest.getRecommendationId())).thenReturn(true);
        when(parameterLevelRecommendationRepository.update(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);
        ParameterLevelRecommendation actualResponse1 = parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);

        assertEquals(parameterLevelAssessment.getRating(), actualResponse.getRating());
        assertEquals(parameterLevelRecommendation.getRecommendation(), actualResponse1.getRecommendation());

    }
}
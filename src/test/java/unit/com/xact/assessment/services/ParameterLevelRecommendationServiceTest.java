package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelRecommendationRepository;
import com.xact.assessment.services.ParameterLevelRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.NOW;
import static com.xact.assessment.dtos.RecommendationImpact.LOW;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ParameterLevelRecommendationServiceTest {

    private ParameterLevelRecommendationRepository parameterLevelRecommendationRepository;
    private ParameterLevelRecommendationService parameterLevelRecommendationService;
    private static final ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void beforeEach(){
       parameterLevelRecommendationRepository= mock(ParameterLevelRecommendationRepository.class);
        parameterLevelRecommendationService = new ParameterLevelRecommendationService(parameterLevelRecommendationRepository);
    }

    @Test
    void shouldSaveRecommendation() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentName("mocked assessment");
        assessment.setAssessmentPurpose("Client Assessment");
        assessment.setAssessmentDescription("description");
        assessment.setAssessmentStatus(AssessmentStatus.Completed);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("text");
        assessmentParameter.setTopic(new AssessmentTopic());
        assessmentParameter.setActive(true);

       RecommendationRequest parameterLevelRecommendationRequest = new RecommendationRequest(null,"text",LOW, RecommendationEffort.LOW,NOW);
        ParameterLevelRecommendation parameterLevelRecommendation = modelMapper.map(parameterLevelRecommendationRequest,ParameterLevelRecommendation.class);
        when(parameterLevelRecommendationRepository.save(parameterLevelRecommendation)).thenReturn(parameterLevelRecommendation);

        parameterLevelRecommendationService.saveParameterLevelRecommendation(parameterLevelRecommendationRequest,assessment,assessmentParameter);

        verify(parameterLevelRecommendationRepository).save(any(ParameterLevelRecommendation.class));

    }
}

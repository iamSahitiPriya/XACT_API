package unit.com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.services.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterServiceTest {
    private ParameterService parameterService;
    private AssessmentParameterRepository assessmentParameterRepository;

    @BeforeEach
    public void beforeEach() {
        assessmentParameterRepository = mock(AssessmentParameterRepository.class);
        parameterService = new ParameterService(assessmentParameterRepository);
    }

    @Test
    public void shouldGetDetailsForParticularParameterId() {
        Integer parameterId = 1;
        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("Parameter Name");

        when(assessmentParameterRepository.findById(parameterId)).thenReturn(Optional.of(assessmentParameter));
        Optional<AssessmentParameter> actualParameter = parameterService.getParameter(parameterId);

        assertEquals(assessmentParameter.getParameterId(), actualParameter.get().getParameterId());
        assertEquals(assessmentParameter.getParameterName(), actualParameter.get().getParameterName());

    }

}

package unit.com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.services.ParameterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
<<<<<<< HEAD
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
=======
import static org.mockito.Mockito.*;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148

class ParameterServiceTest {
    private ParameterService parameterService;
    private AssessmentParameterRepository assessmentParameterRepository;

    @BeforeEach
    public void beforeEach() {
        assessmentParameterRepository = mock(AssessmentParameterRepository.class);
        parameterService = new ParameterService(assessmentParameterRepository);
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

<<<<<<< HEAD
=======
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
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
}

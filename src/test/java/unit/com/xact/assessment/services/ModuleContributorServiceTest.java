package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.repositories.ModuleContributorRepository;
import com.xact.assessment.services.ModuleContributorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class ModuleContributorServiceTest {
    private ModuleContributorRepository moduleContributorRepository;

    private ModuleContributorService moduleContributorService;

    @BeforeEach
    public void beforeEach() {
        moduleContributorRepository = mock(ModuleContributorRepository.class);
        moduleContributorService = new ModuleContributorService(moduleContributorRepository);

    }

    @Test
    void shouldGetModulesByRole() {
        AssessmentModule assessmentModule=new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");

        when(moduleContributorRepository.findByRole("smss@thoughtworks.com", ContributorRole.Author)).thenReturn(Collections.singletonList(assessmentModule));

        moduleContributorService.getModuleByRole("smss@thoughtworks.com",ContributorRole.Author);

        verify(moduleContributorRepository).findByRole("smss@thoughtworks.com",ContributorRole.Author);
    }

    @Test
    void shouldGetRoleByModuleAndUserEmail() {
        AssessmentModule assessmentModule=new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");
        String userEmail="smss@thoughtworks.com";

        when(moduleContributorRepository.findRole(assessmentModule.getModuleId(),userEmail)).thenReturn(ContributorRole.Author);

        moduleContributorService.getRole(assessmentModule.getModuleId(),userEmail);

        verify(moduleContributorRepository).findRole(assessmentModule.getModuleId(),userEmail);
    }
}

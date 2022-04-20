package unit.com.xact.services;

import com.xact.assessment.models.Organisation;
import com.xact.assessment.repositories.OrganisationRepository;
import com.xact.assessment.services.OrganisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrganisationServiceTest {

    private OrganisationRepository organisationRepository;
    private OrganisationService organisationService;

    @BeforeEach
    public void beforeEach() {
        organisationRepository = mock(OrganisationRepository.class);
        organisationService = new OrganisationService(organisationRepository);

    }

    @Test
    public void shouldAddOrganisation() {
        Organisation organisation = new Organisation();
        organisation.setOrganisationId(1L);
        organisation.setOrganisationName("test-organisation");
        organisation.setDomain("test-domain");
        organisation.setIndustry("test-industry");
        organisation.setSize(10);

        when(organisationRepository.save(organisation)).thenReturn(organisation);

        Organisation actualSavedOrganisation = organisationService.createOrganisation(organisation);

        assertEquals(organisation, actualSavedOrganisation);

        verify(organisationRepository).save(organisation);


    }
}

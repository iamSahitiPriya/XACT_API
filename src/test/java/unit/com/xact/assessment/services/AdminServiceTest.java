/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.services.AdminService;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.ModuleContributorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class AdminServiceTest {

    ModuleContributorService moduleContributorService = Mockito.mock(ModuleContributorService.class);

    AssessmentMasterDataService assessmentMasterDataService=Mockito.mock(AssessmentMasterDataService.class);

    AssessmentService assessmentService=Mockito.mock(AssessmentService.class);

    private final AdminService adminService = new AdminService(moduleContributorService, assessmentMasterDataService, userAuthService, assessmentService, accessControlService);

    @Test
    void shouldSaveModuleContributor() {
        Integer moduleId = 1;
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setUserEmail("abc@thoughtworks.com");
        contributorDto.setRole(ContributorRole.AUTHOR);
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(moduleId);
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(assessmentModule);
        contributorId.setUserEmail(contributorDto.getUserEmail());

        doNothing().when(moduleContributorService).saveContributors(moduleId, Collections.singletonList(contributorDto));
        adminService.saveContributors(moduleId, Collections.singletonList(contributorDto));

        verify(moduleContributorService).saveContributors(moduleId, Collections.singletonList(contributorDto));

    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.services.AdminService;
import com.xact.assessment.services.ModuleContributorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

class AdminServiceTest {

    ModuleContributorService moduleContributorService = Mockito.mock(ModuleContributorService.class);

    private final AdminService adminService = new AdminService(moduleContributorService);

    @Test
    void shouldSaveModuleContributor() {
        Integer moduleId = 1;
        ContributorDto contributorDto = new ContributorDto();
        contributorDto.setUserEmail("abc@thoughtworks.com");
        contributorDto.setRole(ContributorRole.AUTHOR);
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(moduleId);
        ModuleContributor moduleContributor = new ModuleContributor();
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(assessmentModule);
        contributorId.setUserEmail(contributorDto.getUserEmail());
        moduleContributor.setContributorId(contributorId);
        moduleContributor.setContributorRole(contributorDto.getRole());

        when(moduleContributorService.saveContributor(moduleId, contributorDto)).thenReturn(moduleContributor);
        List<ContributorDto> actualResponse = adminService.saveContributor(moduleId, Collections.singletonList(contributorDto));

        Assertions.assertEquals(actualResponse.get(0).getRole(), contributorDto.getRole());

    }
}

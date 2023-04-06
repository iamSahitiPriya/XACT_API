/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.repositories.ModuleContributorRepository;
import com.xact.assessment.services.ModuleContributorService;
import com.xact.assessment.services.ModuleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ModuleContributorServiceTest {
    private ModuleContributorRepository moduleContributorRepository;

    private ModuleContributorService moduleContributorService;

    private ModuleService moduleService;

    @BeforeEach
    public void beforeEach() {
        moduleContributorRepository = mock(ModuleContributorRepository.class);
        moduleService = mock(ModuleService.class);
        moduleContributorService = new ModuleContributorService(moduleContributorRepository, moduleService);

    }

    @Test
    void shouldGetModulesByRole() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");

        when(moduleContributorRepository.findByRole("smss@thoughtworks.com", ContributorRole.AUTHOR)).thenReturn(Collections.singletonList(assessmentModule));

        moduleContributorService.getModulesByRole("smss@thoughtworks.com", ContributorRole.AUTHOR);

        verify(moduleContributorRepository).findByRole("smss@thoughtworks.com", ContributorRole.AUTHOR);
    }

    @Test
    void shouldGetRoleByModuleAndUserEmail() {
        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("module");
        String userEmail = "smss@thoughtworks.com";

        when(moduleContributorRepository.findRole(assessmentModule.getModuleId(), userEmail)).thenReturn(Optional.of(ContributorRole.AUTHOR));

        moduleContributorService.getRole(assessmentModule.getModuleId(), userEmail);

        verify(moduleContributorRepository).findRole(assessmentModule.getModuleId(), userEmail);
    }

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

        when(moduleContributorRepository.save(any(ModuleContributor.class))).thenReturn(moduleContributor);
        when(moduleService.getModule(moduleId)).thenReturn(assessmentModule);

        ModuleContributor actualResponse = moduleContributorService.saveContributor(moduleId, contributorDto);

        Assertions.assertEquals(moduleContributor.getContributorRole(),actualResponse.getContributorRole());
    }
}

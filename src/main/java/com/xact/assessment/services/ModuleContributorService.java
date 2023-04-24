/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.models.ModuleContributor;
import com.xact.assessment.repositories.ModuleContributorRepository;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class ModuleContributorService {
    private final ModuleContributorRepository moduleContributorRepository;
    private final ModuleService moduleService;

    public static final String EMAIL_PATTERN = "^(.+)@thoughtworks\\.com$";


    public ModuleContributorService(ModuleContributorRepository moduleContributorRepository, ModuleService moduleService) {
        this.moduleContributorRepository = moduleContributorRepository;
        this.moduleService = moduleService;
    }

    public List<AssessmentModule> getModulesByRole(String userEmail, ContributorRole contributorRole) {
        return moduleContributorRepository.findByRole(userEmail, contributorRole);
    }

    public Set<ContributorRole> getContributorRolesByEmail(String userEmail) {
        return moduleContributorRepository.findRolesByEmail(userEmail);
    }

    public Optional<ContributorRole> getRole(Integer moduleId, String userEmail) {
        return moduleContributorRepository.findRole(moduleId, userEmail);
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        List<ModuleContributor> moduleContributors = new ArrayList<>();
        for (ContributorDto contributor : contributors) {
            ModuleContributor moduleContributor = getModuleContributor(moduleId, contributor);
            moduleContributors.add(moduleContributor);
        }
        moduleContributorRepository.deleteByModuleId(moduleId);
        moduleContributorRepository.saveAll(moduleContributors);
    }


    private ModuleContributor getModuleContributor(Integer moduleId, ContributorDto contributorDto) {
        ContributorId contributorId = new ContributorId();
        contributorId.setModule(moduleService.getModule(moduleId));
        contributorId.setUserEmail(contributorDto.getUserEmail());
        ModuleContributor moduleContributor = new ModuleContributor();
        moduleContributor.setContributorId(contributorId);
        moduleContributor.setContributorRole(contributorDto.getRole());
        return moduleContributor;
    }

}

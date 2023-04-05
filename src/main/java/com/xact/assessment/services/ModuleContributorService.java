/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.repositories.ModuleContributorRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class ModuleContributorService {
    private final ModuleContributorRepository moduleContributorRepository;

    public ModuleContributorService(ModuleContributorRepository moduleContributorRepository) {
        this.moduleContributorRepository = moduleContributorRepository;
    }

    public List<AssessmentModule> getModulesByRole(String userEmail, ContributorRole contributorRole) {
        return moduleContributorRepository.findByRole(userEmail, contributorRole);
    }

    public List<ContributorRole> getContributorRolesByEmail(String userEmail) {
        return moduleContributorRepository.findRolesByEmail(userEmail);
    }

    public Optional<ContributorRole> getRole(Integer moduleId, String userEmail) {
        return moduleContributorRepository.findRole(moduleId, userEmail);
    }
}

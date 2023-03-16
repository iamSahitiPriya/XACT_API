package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.repositories.ModuleContributorRepository;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ModuleContributorService {
    private final ModuleContributorRepository moduleContributorRepository;

    public ModuleContributorService(ModuleContributorRepository moduleContributorRepository) {
        this.moduleContributorRepository = moduleContributorRepository;
    }

    public List<AssessmentModule> getModuleByRole(String userEmail, ContributorRole contributorRole) {
        return moduleContributorRepository.findByRole(userEmail,contributorRole);
    }
}

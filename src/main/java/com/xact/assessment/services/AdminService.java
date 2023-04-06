/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.models.ModuleContributor;
import jakarta.inject.Singleton;

@Singleton
public class AdminService {
    private final ModuleContributorService moduleContributorService;


    public AdminService(ModuleContributorService moduleContributorService) {
        this.moduleContributorService = moduleContributorService;
    }

    public ContributorDto saveContributor(Integer moduleId, ContributorDto contributorDto) {
        ModuleContributor moduleContributor = moduleContributorService.saveContributor(moduleId, contributorDto);
        ContributorDto contributorResponse = new ContributorDto();
        if (moduleContributor.getContributorId() != null) {
            contributorResponse.setUserEmail(moduleContributor.getContributorId().getUserEmail());
            contributorResponse.setRole(moduleContributor.getContributorRole());
        }
        return contributorResponse;
    }
}

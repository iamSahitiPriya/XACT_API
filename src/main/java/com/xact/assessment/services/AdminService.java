/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import jakarta.inject.Singleton;

import java.util.List;


@Singleton
public class AdminService {
    private final ModuleContributorService moduleContributorService;


    public AdminService(ModuleContributorService moduleContributorService) {
        this.moduleContributorService = moduleContributorService;
    }

    public void saveContributors(Integer moduleId, List<ContributorDto> contributors) {
        moduleContributorService.saveContributors(moduleId,contributors);
    }

}

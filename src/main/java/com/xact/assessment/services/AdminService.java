/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ContributorDto;
import com.xact.assessment.exceptions.UnauthorisedUserException;
import com.xact.assessment.models.ModuleContributor;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


@Singleton
public class AdminService {
    public static final String PATTERN = "^(.+)@thoughtworks\\.com$";
    private final ModuleContributorService moduleContributorService;


    public AdminService(ModuleContributorService moduleContributorService) {
        this.moduleContributorService = moduleContributorService;
    }

    public List<ContributorDto> saveContributor(Integer moduleId, List<ContributorDto> contributors) {
        List<ContributorDto> contributorResponse = new ArrayList<>();
        for (ContributorDto contributor : contributors) {
            if (isEmailValid(contributor.getUserEmail()) && (!moduleContributorService.isAlreadyAContributor(contributors, contributor))) {
                ContributorDto response = new ContributorDto();
                ModuleContributor moduleContributor = moduleContributorService.saveContributor(moduleId, contributor);
                response.setUserEmail(moduleContributor.getContributorId().getUserEmail());
                response.setRole(moduleContributor.getContributorRole());
                contributorResponse.add(response);
            } else {
                throw new UnauthorisedUserException("Invalid Request");
            }
        }
        return contributorResponse;
    }

    private boolean isEmailValid(String userEmail) {
        return Pattern.matches(PATTERN, userEmail);
    }
}

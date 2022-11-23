/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.Account;
import com.xact.assessment.repositories.AccountRepository;
import com.xact.assessment.utils.ResourceFileUtil;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AccountService {
    private final AccountRepository accountRepository;
    private final ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
    private final ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);


    public AccountService( AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public List<Account> readAccounts() throws IOException {
        String accounts = resourceFileUtil.getJsonString("localData/tbm_accounts.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(accounts, new TypeReference<ArrayList<Account>>() {
        });
    }

    public void saveAccountDetails(AccountResponse accountResponse) {
        if (accountResponse.getNextPageUrl() == null)
            accountResponse.getContent().remove(accountResponse.getContent().size() - 1);
        List<Account> accounts = accountResponse.getContent();
        save(accounts);
    }

    public void save(List<Account> accounts) {
        LOGGER.info("Saving account details");
        accountRepository.updateAll(accounts);
    }


    public List<OrganisationResponse> getOrganisation(String organisationName) {
        List<Account> accounts = accountRepository.findAccount(organisationName);

        List<OrganisationResponse> responses = new ArrayList<>();
        accounts.forEach(account -> {
            OrganisationResponse organisationResponse = modelMapper.map(account, OrganisationResponse.class);
            responses.add(organisationResponse);
        });

        responses.sort((a, b) -> {
            String nameA = a.getName().toLowerCase();
            int indexA = nameA.indexOf(organisationName.toLowerCase());
            String nameB = b.getName().toLowerCase();
            int indexB = nameB.indexOf(organisationName.toLowerCase());
            return indexA != indexB ? Integer.compare(indexA, indexB) : nameA.compareTo(nameB);
        });

        return responses;

    }
}

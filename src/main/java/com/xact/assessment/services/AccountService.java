/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.AccountClient;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.Accounts;
import com.xact.assessment.repositories.AccountRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;

import java.util.*;

@Singleton
public class AccountService {

    private final AccountClient accountClient;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    public AccountService(AccountClient accountClient, AccountRepository accountRepository) {
        this.accountClient = accountClient;
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedDelay = "4h")
    public AccountResponse fetchOrganisationDetails() {
        fetchAccounts();
        return null;
    }

    private void fetchAccounts() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("status", "active");
        AccountResponse accountResponse = new AccountResponse();
        do {
            accountResponse = accountClient.getOrganisationDetails(parameters);
            parameters.put("offset", accountResponse.getContent().get(accountResponse.getNumberOfElements() - 1).getId());
            saveAccountDetails(accountResponse.getContent());
        } while (accountResponse.getNextPageUrl() != null);
    }

    public void saveAccountDetails(List<Accounts> accountsList) {
        accountsList.remove(accountsList.size() - 1);
        accountRepository.updateAll(accountsList);

    }


    public List<OrganisationResponse> getOrganisation(String organisationName) {
        List<Accounts> accounts = accountRepository.findAccount(organisationName);
        List<OrganisationResponse> responses = new ArrayList<>();
        accounts.forEach(account -> {
            OrganisationResponse organisationResponse = modelMapper.map(account, OrganisationResponse.class);
            responses.add(organisationResponse);
        });
        return responses;

    }
}

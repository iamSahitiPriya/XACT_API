/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.AccountClient;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.Account;
import com.xact.assessment.repositories.AccountRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class AccountService {
    private final AccountClient accountClient;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TokenService tokenService;
    private final AppConfig appConfig;
    private static  final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountClient accountClient, AccountRepository accountRepository, TokenService tokenService, AppConfig appConfig) {
        this.accountClient = accountClient;
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.appConfig = appConfig;
    }

    @Scheduled(fixedDelay = "4h")
    public AccountResponse fetchOrganisationDetails() {
        LOGGER.info("Fetching account details");
        fetchAccounts();
        return null;
    }

    private void fetchAccounts() {
        Map<String, String> parameters = new HashMap<>();
        String token = "Bearer "+ tokenService.getToken(appConfig.getScope());
        parameters.put("status", "active");
        AccountResponse accountResponse;
        do {
            accountResponse = accountClient.getOrganisationDetails(parameters, token);
            parameters.put("offset", accountResponse.getContent().get(accountResponse.getNumberOfElements() - 1).getId());
            saveAccountDetails(accountResponse);
        } while (accountResponse.getNextPageUrl() != null);
    }

    public void saveAccountDetails(AccountResponse accountResponse) {
        if (accountResponse.getNextPageUrl() == null)
            accountResponse.getContent().remove(accountResponse.getContent().size() - 1);

        accountRepository.updateAll(accountResponse.getContent());
    }


    public List<OrganisationResponse> getOrganisation(String organisationName) {
        List<Account> accounts = accountRepository.findAccount(organisationName);
        List<OrganisationResponse> responses = new ArrayList<>();
        accounts.forEach(account -> {
            OrganisationResponse organisationResponse = modelMapper.map(account, OrganisationResponse.class);
            responses.add(organisationResponse);
        });
        return responses;

    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.AccountClient;
import com.xact.assessment.config.AccountConfig;
import com.xact.assessment.config.TokenConfig;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.Account;
import com.xact.assessment.repositories.AccountRepository;
import com.xact.assessment.utils.ResourceFileUtil;
import io.micronaut.context.env.Environment;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

@Singleton
public class AccountService {
    private final AccountClient accountClient;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final TokenService tokenService;
    private final TokenConfig tokenConfig;
    private final AccountConfig accountConfig;
    private  final ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Inject
    Environment environment;

    public AccountService(AccountClient accountClient, AccountRepository accountRepository, TokenService tokenService, TokenConfig tokenConfig, AccountConfig accountConfig, Environment environment) {
        this.accountClient = accountClient;
        this.accountRepository = accountRepository;
        this.tokenService = tokenService;
        this.tokenConfig = tokenConfig;
        this.accountConfig = accountConfig;
        this.environment = environment;
    }

    @Scheduled(fixedDelay = "${account.delay}")
    public void fetchOrganisationDetails() throws IOException {
        LOGGER.info("Fetching account details");
        Set<String> environmentActiveNames = environment.getActiveNames();
        if (environmentActiveNames.contains("local")) {
            List<Account> accounts = readAccounts();
            save(accounts);

        } else {
            fetchAccounts();
        }
    }

    public List<Account> readAccounts() throws IOException {
        String accounts = resourceFileUtil.getJsonString("localData/tbm_accounts.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(accounts, new TypeReference<ArrayList<Account>>() {
        });
    }

    private void fetchAccounts() {
        Map<String, String> parameters = new HashMap<>();
        String token = "Bearer " + tokenService.getToken(accountConfig.getScope());
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
        List<Account> accounts = accountResponse.getContent();
        save(accounts);
    }

    private void save(List<Account> accounts) {
        accountRepository.updateAll(accounts);
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

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.AccountClient;
import com.xact.assessment.config.AccountConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.Account;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class AccountSchedulerService {

    private final AccountClient accountClient;
    private final TokenService tokenService;
    private final ProfileConfig profileConfig;
    private final AccountConfig accountConfig;
    private final  AccountService accountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSchedulerService.class);

    public AccountSchedulerService(AccountClient accountClient, TokenService tokenService, ProfileConfig profileConfig, AccountConfig accountConfig, AccountService accountService) {
        this.accountClient = accountClient;
        this.tokenService = tokenService;
        this.profileConfig = profileConfig;
        this.accountConfig = accountConfig;
        this.accountService = accountService;
    }

    @Scheduled(fixedDelay = "${account.delay}")
    public void fetchOrganisationDetails() throws IOException {
        LOGGER.info("Fetching account details");
        if ("local".equals(profileConfig.getType()) || "test".equals(profileConfig.getType())) {
            List<Account> accounts = accountService.readAccounts();
            accountService.save(accounts);
        } else {
            fetchAccounts();
        }
    }

    private void fetchAccounts() {
        Map<String, String> parameters = new HashMap<>();
        String token = "Bearer " + tokenService.getToken(accountConfig.getScope());
        parameters.put("status", "active");
        AccountResponse accountResponse;
        do {
            accountResponse = accountClient.getOrganisationDetails(parameters, token);
            parameters.put("offset", accountResponse.getContent().get(accountResponse.getNumberOfElements() - 1).getId());
            accountService.saveAccountDetails(accountResponse);
        } while (accountResponse.getNextPageUrl() != null);
    }

}

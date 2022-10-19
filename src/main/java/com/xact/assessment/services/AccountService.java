/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.controllers.AccountClient;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.Accounts;
import com.xact.assessment.repositories.AccountRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

import java.util.*;

@Singleton
public class AccountService {

    private final AccountClient accountClient;
    private final AccountRepository accountRepository;
    private static int count = 0;


    public AccountService(AccountClient accountClient, AccountRepository accountRepository) {
        this.accountClient = accountClient;
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedDelay = "5s")
    public AccountResponse fetchOrganisationDetails() {
        String urlPath = "";
        urlPath = "/api/accounts";
        Map<String,String> parameters = new HashMap<>();
        parameters.put("status","active");
        AccountResponse accountResponsePublisher = new AccountResponse();
        do {
           accountResponsePublisher = accountClient.getOrganisationDetails(parameters);
            System.out.println(accountResponsePublisher.getNextPageUrl()+" "+Arrays.toString(accountResponsePublisher.getNextPageUrl().split("\\?")));
            String[] values = (accountResponsePublisher.getNextPageUrl().split("\\?"))[1].split("&");
            for(String value:values){
                parameters.put(value.split("=")[0],value.split("=")[1]);
            }
            saveAccountDetails(accountResponsePublisher.getContent());
            System.out.println(urlPath + ""+parameters.get("offset"));
        }while(accountResponsePublisher.getNextPageUrl() != null);
//        return accountClient.getOrganisationDetails(urlPath,parameters);
        return null;
    }
    public void saveAccountDetails(List<Accounts> accountsList){
        accountRepository.saveAll(accountsList);

    }



}

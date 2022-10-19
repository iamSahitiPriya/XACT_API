/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.controllers.AccountClient;
import com.xact.assessment.dtos.AccountResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

@Singleton
public class AccountService {

    private final AccountClient accountClient;


    public AccountService(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public Publisher<AccountResponse> fetchOrganisationDetails() {
        return accountClient.getOrganisationDetails();
    }


}

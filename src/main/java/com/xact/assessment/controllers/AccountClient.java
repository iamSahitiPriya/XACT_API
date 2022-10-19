/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.Accounts;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

@Client("https://api.thoughtworks.net/account")
public interface AccountClient {

    @Get(value="/api/accounts")
    AccountResponse getOrganisationDetails(@Body Map<String,String> parameters);




}

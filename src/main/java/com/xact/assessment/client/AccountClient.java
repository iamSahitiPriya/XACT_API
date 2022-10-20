/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.Accounts;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

@Client("https://api.thoughtworks.net/account")
public interface AccountClient {

    @Get(value="/api/accounts{?args*}")
    AccountResponse getOrganisationDetails(@QueryValue Map<String, String> args);




}

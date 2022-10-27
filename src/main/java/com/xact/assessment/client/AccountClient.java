/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.client;

import com.xact.assessment.dtos.AccountResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Map;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

@Client("https://api.thoughtworks.net/account")
public interface AccountClient {

    @Get(value="/api/accounts{?args*}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    AccountResponse getOrganisationDetails(@QueryValue Map<String, String> args, @Header(AUTHORIZATION) String authorisation);

}

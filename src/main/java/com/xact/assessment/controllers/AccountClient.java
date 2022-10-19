/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.models.Accounts;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

import java.util.List;

@Client("https://api.thoughtworks.net")
public interface AccountClient {

    @Get( value="/account/api/accounts?status=active&limit=2")
    Publisher <AccountResponse> getOrganisationDetails();




}

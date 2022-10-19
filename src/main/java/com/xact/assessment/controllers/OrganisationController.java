/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.services.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Security;

@Controller("/v1")
public class OrganisationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);
    private final AccountService accountService;


    public OrganisationController(AccountService accountService) {

        this.accountService = accountService;
    }

    @Get(uri = "/accounts",produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<Publisher<AccountResponse>> fetchOrganisationName() {
        System.out.println("calling");
        return HttpResponse.ok(accountService.fetchOrganisationDetails());
    }
}

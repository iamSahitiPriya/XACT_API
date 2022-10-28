/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.services.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/v1")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private final AccountService accountService;


    public AccountController(AccountService accountService) {

        this.accountService = accountService;
    }
    @Get(value = "/account/{name}",produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<List<OrganisationResponse>> fetchOrganisationName(@PathVariable("name") String organisationName, Authentication authentication) {
        LOGGER.info("Get Organisation details: {}",organisationName);
        List<OrganisationResponse> organisation = accountService.getOrganisation(organisationName);
        return HttpResponse.ok(organisation);
    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AccountController;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.services.AccountService;
import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    private final AccountService accountService = mock(AccountService.class);
    private final AccountController accountController = new AccountController(accountService);

    @Test
    void fetchOrganisationName() {
        OrganisationResponse organisationResponse = new OrganisationResponse("A12", "tw");
        OrganisationResponse organisationResponse1 = new OrganisationResponse("A13", "QW");
        List<OrganisationResponse> organisationResponseList = new ArrayList<>();
        organisationResponseList.add(organisationResponse);
        organisationResponseList.add(organisationResponse1);
        String name = "a";
        when(accountService.getOrganisation(name)).thenReturn(organisationResponseList);

        HttpResponse<List<OrganisationResponse>> actualResponse = accountController.fetchOrganisationName(name);

        Assertions.assertEquals(2, actualResponse.body().size());
    }
}

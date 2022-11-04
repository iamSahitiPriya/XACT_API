/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AccountController;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.services.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountControllerTest {

    private final AccountService  accountService= mock(AccountService.class);
    private final AccountController accountController = new AccountController(accountService);
    private final Authentication authentication = Mockito.mock(Authentication.class);

    @Test
    void fetchOrganisationName() {
        OrganisationResponse organisationResponse = new OrganisationResponse("A12","tw");
        OrganisationResponse organisationResponse1 = new OrganisationResponse("A13","QW");
        List <OrganisationResponse> organisationResponseList = new ArrayList<>();
        organisationResponseList.add(organisationResponse);
        organisationResponseList.add(organisationResponse1);
        Map<String, String> name = new HashMap<>();
        name.put("name","a");
        when(accountService.getOrganisation("a")).thenReturn(organisationResponseList);

        HttpResponse<List<OrganisationResponse>> actualResponse = accountController.fetchOrganisationName(authentication,name);

        Assertions.assertEquals(actualResponse.body().size(),2);
    }
}

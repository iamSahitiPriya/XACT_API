/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.client.AccountClient;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.models.Accounts;
import com.xact.assessment.repositories.AccountRepository;
import com.xact.assessment.services.AccountService;
import com.xact.assessment.services.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;

public class AccountServiceTest {
    AccountClient accountClient = mock(AccountClient.class);
    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService;
    TokenService tokenService = mock(TokenService.class);

    public AccountServiceTest() {
        this.accountService = new AccountService(accountClient,accountRepository, tokenService);
    }

    @Test
    void fetchAccounts() {
        Accounts account = new Accounts("A1","TW","Finance");
        Accounts anotherAccount = new Accounts("B2","TWI","Assess");
        Map<String, String> parameters = new HashMap<>();
        String scope = "account.read.internal";
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("",1,"abc","",new Date());
        String token = "Bearer "+accessTokenResponse.getAccess_token();
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccess_token());
        parameters.put("status", "active");
        List <Accounts> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(anotherAccount);
        AccountResponse accountResponse = new AccountResponse("ABC123",null,2,accounts);
        Mockito.when(accountClient.getOrganisationDetails(parameters,token)).thenReturn(accountResponse);

        accountService.fetchOrganisationDetails();

        verify(accountRepository).updateAll(accounts);
    }

    @Test
    void getOrganisation() {
        List <Accounts> accounts = new ArrayList<>();
        accounts.add(new Accounts("A1","TW","Finance"));
        when(accountRepository.findAccount("A")).thenReturn(accounts);

        List<OrganisationResponse> organisationResponseList = accountService.getOrganisation("A");

        Assertions.assertEquals(organisationResponseList.size(),1);


    }
}

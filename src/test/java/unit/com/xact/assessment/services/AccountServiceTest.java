/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.client.AccountClient;
import com.xact.assessment.config.AccountConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.models.Account;
import com.xact.assessment.repositories.AccountRepository;
import com.xact.assessment.services.AccountService;
import com.xact.assessment.services.TokenService;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;

class AccountServiceTest {
    AccountClient accountClient = mock(AccountClient.class);
    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService;
    TokenService tokenService = mock(TokenService.class);
    AccountConfig accountConfig = mock(AccountConfig.class);
    ProfileConfig profileConfig = mock(ProfileConfig.class);

    Environment environment = mock(Environment.class);

    public AccountServiceTest() {
        this.accountService = new AccountService(accountClient, accountRepository, tokenService, profileConfig, accountConfig, environment);
    }

    @Test
    void fetchAccounts() throws IOException {
        Account account = new Account("A1", "TW", "Finance");
        Account anotherAccount = new Account("B2", "TWI", "Assess");
        Map<String, String> parameters = new HashMap<>();
        String scope = "account.read.internal";
        when(accountConfig.getScope()).thenReturn(scope);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("", 1, "abc", "", new Date());
        String token = "Bearer " + accessTokenResponse.getAccessToken();
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccessToken());
        parameters.put("status", "active");
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(anotherAccount);
        AccountResponse accountResponse = new AccountResponse("ABC123", null, 2, accounts);
        Mockito.when(accountClient.getOrganisationDetails(parameters, token)).thenReturn(accountResponse);
        Set<String> set = new HashSet<>();
        set.add("dev");
        when(environment.getActiveNames()).thenReturn(set);

        accountService.fetchOrganisationDetails();

        verify(accountRepository).updateAll(accounts);
    }

    @Test
    void getOrganisation() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("A1", "TW", "Finance"));
        when(accountRepository.findAccount("A")).thenReturn(accounts);

        List<OrganisationResponse> organisationResponseList = accountService.getOrganisation("A");

        Assertions.assertEquals(1, organisationResponseList.size());


    }

    @Test
    void fetchAccountDetailForLocalEnv() throws IOException {
        when(profileConfig.getType()).thenReturn("local");
        List<Account> accountList = accountService.readAccounts();

        accountService.fetchOrganisationDetails();

        verify(accountRepository).updateAll(accountList);

    }
}
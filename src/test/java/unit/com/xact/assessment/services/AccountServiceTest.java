/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.AccountResponse;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.models.Account;
import com.xact.assessment.repositories.AccountRepository;
import com.xact.assessment.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class AccountServiceTest {
    AccountRepository accountRepository = mock(AccountRepository.class);
    AccountService accountService;

    public AccountServiceTest() {
        this.accountService = new AccountService(accountRepository);
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
    void saveOrganisationDetails() {
        Account account = new Account("A1", "TW", "Finance");
        Account anotherAccount = new Account("B2", "TWI", "Assess");
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(anotherAccount);
        AccountResponse accountResponse = new AccountResponse("ABC123", null, 2, accounts);

        accountService.saveAccountDetails(accountResponse);

        verify(accountRepository).updateAll(accounts);
    }

    @Test
    void saveAccount() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("Aab1", "Aab", "Finance"));
        accounts.add(new Account("Aba1", "Aba", "Finance"));
        accounts.add(new Account("zzba1", "zzba", "Finance"));
        accounts.add(new Account("aaa1", "aaa", "Finance"));
        when(accountRepository.findAccount("A")).thenReturn(accounts);

        List<OrganisationResponse> organisationResponseList = accountService.getOrganisation("A");

        Assertions.assertEquals(4, organisationResponseList.size());
        Assertions.assertEquals("aaa", organisationResponseList.get(0).getName());
        Assertions.assertEquals("Aab", organisationResponseList.get(1).getName());
        Assertions.assertEquals("Aba", organisationResponseList.get(2).getName());
        Assertions.assertEquals("zzba", organisationResponseList.get(3).getName());
    }

    @Test
    void readAccounts() throws IOException {

        List<Account> accounts = accountService.readAccounts();

        Assertions.assertEquals(16770,accounts.size());
    }
}

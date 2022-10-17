/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.Accounts;
import jakarta.inject.Singleton;

@Singleton
public class AccountService {
//    private AccountRepository accountRepository;

    public AccountService() {
//        this.accountRepository = accountRepository;
    }

    public void saveAccount(Accounts account) {
        System.out.println(account.getId() + " " + account.getName() + " " + account.getIndustry());
//        accountRepository.save(account);
    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.config.AppConfig;
import com.xact.assessment.client.AccessTokenClient;
import com.xact.assessment.dtos.AccountResponse;
import jakarta.inject.Singleton;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TokenService {
    private final AppConfig appConfig;
    private final AccessTokenClient accessTokenClient;

    public TokenService(AppConfig appConfig, AccessTokenClient accessTokenClient) {
        this.appConfig = appConfig;
        this.accessTokenClient = accessTokenClient;
    }

    public AccountResponse getToken() {
        String userName = appConfig.getUserName();
        String password = appConfig.getUserPassword();
        String authentication = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
        Map<String,String> body = new HashMap<>();
        body.put("grant_type","client_credentials");
        body.put("scope","account.read.internal");
        AccountResponse accountResponse = accessTokenClient.getAccessToken(authentication,body);
        return accountResponse;
    }
}

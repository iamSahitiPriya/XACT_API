/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.AccessTokenClient;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.models.AccessTokenResponse;
import jakarta.inject.Singleton;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class TokenService {
    private final static  int SECONDS = 1000;
    private  static  final Map<String,AccessTokenResponse> tokenMap = new HashMap<>();
    private final AppConfig appConfig;
    private final AccessTokenClient accessTokenClient;


    public TokenService(AppConfig appConfig, AccessTokenClient accessTokenClient) {
        this.appConfig = appConfig;
        this.accessTokenClient = accessTokenClient;
    }

    public String getToken(String scope) {
        AccessTokenResponse accessToken = tokenMap.get(scope);
        if(isTokenValid(accessToken)) {
            return tokenMap.get("account.read.internal").getAccess_token();
        }
        else {
            String userName = appConfig.getUserName();
            String password = appConfig.getUserPassword();
            String authentication = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "client_credentials");
            body.put("scope", scope);
            AccessTokenResponse accessTokenResponse = accessTokenClient.getAccessToken(authentication, body);
            accessTokenResponse.setCreatedTime(new Date());
            tokenMap.put("account.read.internal",accessTokenResponse);
            return accessTokenResponse.getAccess_token();
        }
    }

    private boolean isTokenValid(AccessTokenResponse accessToken) {
        return (accessToken != null) && ((new Date().getTime() / SECONDS) - (accessToken.getCreatedTime().getTime() / SECONDS) < 60);
    }

}

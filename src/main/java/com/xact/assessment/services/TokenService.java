/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.client.AccessTokenClient;
import com.xact.assessment.config.TokenConfig;
import com.xact.assessment.models.AccessTokenResponse;
import jakarta.inject.Singleton;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Singleton
public class TokenService {
    private static final int SECONDS = 1000;
    private static final Map<String, AccessTokenResponse> tokenMap = new HashMap<>();
    private final TokenConfig tokenConfig;
    private final AccessTokenClient accessTokenClient;


    public TokenService(TokenConfig tokenConfig, AccessTokenClient accessTokenClient) {
        this.tokenConfig = tokenConfig;
        this.accessTokenClient = accessTokenClient;
    }

    public String getToken(String scope) {
        AccessTokenResponse accessToken = tokenMap.get(scope);
        if (isTokenValid(accessToken)) {
            return tokenMap.get(scope).getAccessToken();
        } else {
            String userName = tokenConfig.getUsername();
            String password = tokenConfig.getPassword();
            String authentication = "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes());
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", tokenConfig.getGrantType());
            body.put("scope", scope);
            AccessTokenResponse accessTokenResponse = accessTokenClient.getAccessToken(authentication, body);
            accessTokenResponse.setCreatedTime(new Date());
            tokenMap.put(scope, accessTokenResponse);
            return accessTokenResponse.getAccessToken();
        }
    }

    private boolean isTokenValid(AccessTokenResponse accessToken) {
        return (accessToken != null) && ((new Date().getTime() / SECONDS) - (accessToken.getCreatedTime().getTime() / SECONDS) < accessToken.getExpiresIn() - 60);
    }

}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.client.AccessTokenClient;
import com.xact.assessment.config.TokenConfig;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.services.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenServiceTest {
    AccessTokenClient accessTokenClient = mock(AccessTokenClient.class);
    TokenConfig tokenConfig = mock(TokenConfig.class);
    TokenService tokenService;

    public TokenServiceTest() {
        this.tokenService = new TokenService(tokenConfig,accessTokenClient);

    }

    @Test
    void shouldFetchAccessToken() {
        Map<String,String> body = new HashMap<>();
        String scope = "account.read.internal";
        String auth = "Basic " + Base64.getEncoder().encodeToString(("username" + ":" + "password").getBytes());
        body.put("grant_type","client_credentials");
        body.put("scope","account.read.internal");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("",1,"abc","",new Date());
        when(tokenConfig.getUsername()).thenReturn("username");
        when(tokenConfig.getPassword()).thenReturn("password");
        when(tokenConfig.getGrantType()).thenReturn("client_credentials");
        when(accessTokenClient.getAccessToken(auth,body)).thenReturn(accessTokenResponse);

        String token = tokenService.getToken(scope);

        Assertions.assertNotNull(token);
    }

}

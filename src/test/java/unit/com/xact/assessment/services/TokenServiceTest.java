package unit.com.xact.assessment.services;

import com.xact.assessment.client.AccessTokenClient;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.services.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokenServiceTest {
    AccessTokenClient accessTokenClient = mock(AccessTokenClient.class);
    AppConfig appConfig = mock(AppConfig.class);
    TokenService tokenService;

    public TokenServiceTest() {
        this.tokenService = new TokenService(appConfig,accessTokenClient);

    }

    @Test
    void shouldFetchAccessToken() {
        Map<String,String> body = new HashMap<>();
        String auth = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=";
        body.put("grant_type","client_credentials");
        body.put("scope","account.read.internal");
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("",1000,"abc","");
        when(accessTokenClient.getAccessToken(auth,body)).thenReturn(accessTokenResponse);
        when(appConfig.getUserName()).thenReturn("username");
        when(appConfig.getUserPassword()).thenReturn("password");

        AccessTokenResponse accessTokenResponse1 = tokenService.getToken();

        Assertions.assertEquals(accessTokenResponse1.getAccess_token(),"abc");
    }
}

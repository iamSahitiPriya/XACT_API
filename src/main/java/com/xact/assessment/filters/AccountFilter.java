/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.filters;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.controllers.AssessmentController;
import com.xact.assessment.controllers.OrganisationController;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import lombok.SneakyThrows;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Filter("/account/**")
public class AccountFilter implements HttpClientFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    private final AppConfig appConfig;

    public AccountFilter( AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @SneakyThrows
    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        LOGGER.info("Fetching Accounts. {}",request);
        Map<String,String> body = new HashMap<>();
        body.put("grant_type","client_credentials");
        body.put("scope","account.read.internal");

        String token = getAccessToken();

        return chain.proceed(request.bearerAuth(token));
    }

    @SneakyThrows
    public String getAccessToken() throws IOException, ParseException {
        String username = appConfig.getUserName();
        String password = appConfig.getUserPassword();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder output;
        String urlParameters = "grant_type=client_credentials&scope=account.read.internal";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        String type = "application/x-www-form-urlencoded";

        URL weburl=new URL("https://thoughtworks.okta.com/oauth2/aus1fjygi70z7ZtVB0h8/v1/token");
        HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
        String authenticationType = "Basic ";
        String method = "POST";
        setProperty(username, password, postDataLength, type, conn, authenticationType, method);

        try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write( postData );
        }

        output = getResponseBody(stringBuilder, conn);


        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(output.toString());

        return(json.get("access_token")).toString();
    }

    private void setProperty(String username, String password, int postDataLength, String type, HttpURLConnection conn, String authenticationType, String method) throws ProtocolException, ProtocolException {
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setRequestProperty("Authorization",
                authenticationType + Base64.getEncoder().encodeToString(
                        (username + ":" + password).getBytes()));
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);

        conn.setRequestMethod(method);

        conn.setRequestProperty("Content-Type", type);
        conn.setRequestProperty("charset", "utf-8");

        conn.setUseCaches(false);
    }

    private StringBuilder getResponseBody(StringBuilder stringBuilder, HttpURLConnection conn) throws IOException, InterruptedException {
        String output;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        return stringBuilder;
    }

}

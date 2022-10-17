/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.models.Accounts;
import com.xact.assessment.services.AccountService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.scheduling.annotation.Scheduled;
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
import java.util.Base64;
import java.util.List;

@Controller("/v1")
public class OrganisationController {
    private final AccountService accountService;
    private final AppConfig appConfig;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    public OrganisationController(AccountService accountService, AppConfig appConfig) {
        this.accountService = accountService;
        this.appConfig = appConfig;
    }
//    @Get(value = "/org/{orgName}", produces = MediaType.APPLICATION_JSON)
//    @Secured(SecurityRule.IS_AUTHENTICATED)
//    public HttpResponse organisationName(@PathVariable("orgName")String orgName, Authentication authentication) throws ParseException {
//        List<OrganisationResponse> result = getOrganisationName();
//        return HttpResponse.ok(result);
//
//    }
    @Scheduled(fixedDelay = "10s")
    public void getOrganisationName() throws ParseException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder output;
        URL weburl;
        JSONObject json;
        String method = "GET";
        String type = "application/json";
        String authenticationType = "Bearer ";
        String url = "https://api.thoughtworks.net/account/api/accounts?status=active&limit=2";
        weburl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
        setProperty(method, type, authenticationType, conn);
        output = getResponseBody(stringBuilder, conn);

        JSONParser parser = new JSONParser();
        json = getJsonObject(output, parser);
        List<JSONObject> jsonObjects = (List<JSONObject>) parser.parse(String.valueOf(json.get("content")));
        saveAccount(jsonObjects);
    }

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

    private void setProperty(String username, String password, int postDataLength, String type, HttpURLConnection conn, String authenticationType, String method) throws ProtocolException {
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

    private void setProperty(String method, String type, String authenticationType, HttpURLConnection conn) throws IOException, ParseException {
        String token = getAccessToken();

        conn.setRequestProperty("Authorization",
                authenticationType + token);
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);

        conn.setRequestMethod(method);

        conn.setRequestProperty("Content-Type", type);
        conn.setRequestProperty("charset", "utf-8");
        conn.setUseCaches(false);
    }


    private JSONObject getJsonObject(StringBuilder output, JSONParser parser) throws ParseException {
        JSONObject json;
        json = (JSONObject) parser.parse(output.toString());
        return json;
    }

    private StringBuilder getResponseBody(StringBuilder stringBuilder, HttpURLConnection conn) throws IOException {
        String output;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }
        return stringBuilder;
    }

    private void saveAccount(List<JSONObject> jsonObjects) {
        for(JSONObject jsonObject : jsonObjects) {
            Accounts account = new Accounts(jsonObject.get("id").toString(),
                    jsonObject.get("name").toString(),jsonObject.get("industry").toString());
            accountService.saveAccount(account);
        }
    }
}

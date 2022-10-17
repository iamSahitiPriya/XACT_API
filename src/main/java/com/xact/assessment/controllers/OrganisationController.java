package com.xact.assessment.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.xact.assessment.config.AppConfig;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.services.OrganisationService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Controller("/v1")
public class OrganisationController {
    private final OrganisationService organisationService;
    private final AppConfig appConfig;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    private final HttpClient httpClient;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrganisationController(OrganisationService organisationService, @Client("https://api.thoughtworks.net") HttpClient httpClient, AppConfig appConfig) {
        this.organisationService = organisationService;
        this.httpClient = httpClient;
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
    List<OrganisationResponse> getOrganisationName() throws ParseException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String output;

        URL weburl=new URL("https://api.thoughtworks.net/account/api/accounts?limit=1");
        HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
        String token = getAccessToken();
        conn.setRequestProperty("Authorization",
                "Bearer " + token);
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("charset", "utf-8");

        conn.setUseCaches(false);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(stringBuilder.toString());
        List<JSONObject> jsonObjects = (List<JSONObject>) parser.parse(String.valueOf(json.get("content")));
        System.out.println(jsonObjects.get(0).get("name"));
        return null;
    }


    public String getAccessToken() throws IOException, ParseException {
        String username = appConfig.getUserName();
        String password = appConfig.getUserPassword();
        StringBuilder stringBuilder = new StringBuilder();
        String output;
        String urlParameters = "grant_type=client_credentials&scope=account.read.internal";
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;

        URL weburl=new URL("https://thoughtworks.okta.com/oauth2/aus1fjygi70z7ZtVB0h8/v1/token");
        HttpURLConnection conn = (HttpURLConnection) weburl.openConnection();
        conn.setRequestProperty("Authorization",
                "Basic " + Base64.getEncoder().encodeToString(
                        (username + ":" + password).getBytes()));
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
        conn.setUseCaches(false);

        try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write( postData );
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        while ((output = bufferedReader.readLine()) != null) {
            stringBuilder.append(output);
        }

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(stringBuilder.toString());
        return(json.get("access_token")).toString();
    }
}

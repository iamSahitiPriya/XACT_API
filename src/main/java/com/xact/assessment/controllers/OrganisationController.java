package com.xact.assessment.controllers;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import com.xact.assessment.dtos.OrganisationResponse;
import com.xact.assessment.services.OrganisationService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller("/v1")
public class OrganisationController {
    private final OrganisationService organisationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationController.class);

    private final HttpClient httpClient;
    private final ModelMapper modelMapper = new ModelMapper();

    public OrganisationController(OrganisationService organisationService, @Client("https://jsonplaceholder.typicode.com") HttpClient httpClient) {
        this.organisationService = organisationService;
        this.httpClient = httpClient;
    }
    @Get(value = "/org/{orgName}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse organisationName(@PathVariable("orgName")String orgName, Authentication authentication) throws ParseException {
        List<OrganisationResponse> result = getOrgName();
        return HttpResponse.ok(result);

    }
    @Scheduled(fixedDelay = "10s")
    List<OrganisationResponse> getOrgName() throws ParseException {
        HttpRequest<?> httpRequest = HttpRequest.GET("/todos/1");
        String result = httpClient.toBlocking().retrieve(httpRequest);
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(result);
        organisationService.getName();
        return null;
    }

}

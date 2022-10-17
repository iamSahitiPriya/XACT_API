package com.xact.assessment.controllers;


import com.xact.assessment.dtos.OrgResponse;
import com.xact.assessment.services.OrganisationNameService;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

@Introspected
@Controller("/v1/organisation")
public class OrganisationNameController {
    private final OrganisationNameService organisationNameService;


    public OrganisationNameController(OrganisationNameService organisationNameService) {
        this.organisationNameService = organisationNameService;
    }

    @Get(value="/{name}",produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<OrgResponse> getOrganisationName(@PathVariable("name") String orgName, Authentication authentication){
        OrgResponse orgResponse=new OrgResponse();
         orgResponse.setNames(organisationNameService.getName(orgName));
      return HttpResponse.ok(orgResponse);
    }
}

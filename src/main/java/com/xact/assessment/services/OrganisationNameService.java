package com.xact.assessment.services;

import com.xact.assessment.repositories.OrganisationNameRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class OrganisationNameService {
    private final OrganisationNameRepository organisationNameRepository;

    public OrganisationNameService(OrganisationNameRepository organisationNameRepository) {
        this.organisationNameRepository = organisationNameRepository;
    }

    public List<String> getName(String orgName) {
       List<String>orgNames =organisationNameRepository.fetchOrgName(orgName);
       return orgNames.stream().filter(name -> name.toLowerCase().contains(orgName)).collect(Collectors.toList());
    }

}

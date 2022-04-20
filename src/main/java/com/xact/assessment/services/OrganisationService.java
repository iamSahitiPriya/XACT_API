package com.xact.assessment.services;

import com.xact.assessment.models.Organisation;
import com.xact.assessment.repositories.OrganisationRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;


@Singleton
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Transactional
    public Organisation createOrganisation(Organisation organisation) {
        return organisationRepository.save(organisation);
    }
}
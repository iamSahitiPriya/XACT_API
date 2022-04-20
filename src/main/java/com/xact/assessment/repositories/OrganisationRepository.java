package com.xact.assessment.repositories;

import com.xact.assessment.models.Organisation;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

}

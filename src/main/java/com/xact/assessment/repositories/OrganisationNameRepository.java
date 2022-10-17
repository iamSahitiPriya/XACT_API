package com.xact.assessment.repositories;

import com.xact.assessment.models.OrganisationName;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface OrganisationNameRepository extends CrudRepository<OrganisationName ,Integer> {

    @Executable
    @Query("select tlo.organisationName from OrganisationName tlo")
    List<String> fetchOrgName(@Parameter("orgName") String orgName);

}

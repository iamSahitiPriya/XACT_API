package com.xact.assessment.repositories;

import com.xact.assessment.dtos.ContributorRole;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.ContributorId;
import com.xact.assessment.models.ModuleContributor;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ModuleContributorRepository extends CrudRepository<ModuleContributor, ContributorId> {
    @Executable
    @Query("SELECT moduleContributor.contributorId.module FROM ModuleContributor moduleContributor WHERE moduleContributor.contributorId.userEmail=:userEmail and moduleContributor.contributorRole=:contributorRole")
    List<AssessmentModule> findByRole(String userEmail, ContributorRole contributorRole);
}

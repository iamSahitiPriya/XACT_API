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
import java.util.Optional;

@Repository
public interface ModuleContributorRepository extends CrudRepository<ModuleContributor, ContributorId> {
    @Executable
    @Query("SELECT moduleContributor.contributorId.module FROM ModuleContributor moduleContributor WHERE moduleContributor.contributorId.userEmail=:userEmail and moduleContributor.contributorRole=:contributorRole")
    List<AssessmentModule> findByRole(String userEmail, ContributorRole contributorRole);

    @Executable
    @Query("SELECT contributor.contributorRole FROM ModuleContributor contributor WHERE contributor.contributorId.userEmail=:userEmail")
    List<ContributorRole> findRolesByEmail(String userEmail);

    @Executable
    @Query("Select moduleContributor.contributorRole FROM ModuleContributor moduleContributor where moduleContributor.contributorId.module.moduleId=:moduleId and moduleContributor.contributorId.userEmail=:userEmail")
    Optional<ContributorRole> findRole(Integer moduleId, String userEmail);

}

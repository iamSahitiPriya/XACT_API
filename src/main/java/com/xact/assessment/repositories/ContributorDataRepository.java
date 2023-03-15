package com.xact.assessment.repositories;

import com.xact.assessment.models.ContributorData;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ContributorDataRepository extends CrudRepository<ContributorData, Integer> {

    @Executable
    @Query("SELECT contributorData FROM ContributorData contributorData WHERE contributorData.parameter.topic.module.author=:userEmail")
    List<ContributorData> findByAuthor(String userEmail);
}

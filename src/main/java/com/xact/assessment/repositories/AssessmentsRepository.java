package com.xact.assessment.repositories;

import com.xact.assessment.models.Assessment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AssessmentsRepository extends CrudRepository<Assessment, Long> {

}

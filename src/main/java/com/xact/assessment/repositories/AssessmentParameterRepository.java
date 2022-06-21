package com.xact.assessment.repositories;


import com.xact.assessment.models.AssessmentParameter;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AssessmentParameterRepository extends CrudRepository<AssessmentParameter, Integer>{
}

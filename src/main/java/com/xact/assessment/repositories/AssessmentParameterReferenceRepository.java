package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentParameterReference;
import io.micronaut.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentParameterReferenceRepository extends CrudRepository<AssessmentParameterReference, Integer> {

}

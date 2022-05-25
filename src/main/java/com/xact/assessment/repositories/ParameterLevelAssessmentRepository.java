package com.xact.assessment.repositories;

import com.xact.assessment.models.ParameterLevelAssessment;
import com.xact.assessment.models.ParameterLevelId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ParameterLevelAssessmentRepository extends CrudRepository<ParameterLevelAssessment, ParameterLevelId> {
}

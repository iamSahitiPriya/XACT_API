/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;


import com.xact.assessment.models.AssessmentParameter;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AssessmentParameterRepository extends CrudRepository<AssessmentParameter, Integer> {

    @Executable
    @Query("SELECT parameter FROM AssessmentParameter parameter WHERE parameter.parameterId=:parameterId")
    AssessmentParameter findByParameterId(Integer parameterId);


}

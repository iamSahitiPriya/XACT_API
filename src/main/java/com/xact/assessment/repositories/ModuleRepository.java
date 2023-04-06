/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ModuleRepository extends CrudRepository<AssessmentModule, Integer> {


    @Executable
    @Query("SELECT module FROM AssessmentModule module WHERE module.moduleId=:moduleId")
    AssessmentModule findByModuleId(Integer moduleId);


}

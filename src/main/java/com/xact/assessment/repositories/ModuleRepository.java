/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<AssessmentModule, Integer> {
    @Executable
    @Query("SELECT pla FROM AssessmentModule pla WHERE pla.category.categoryId=:categoryId")
    List<AssessmentModule> findByCategory(@Parameter("categoryId") Integer categoryId);

    @Executable
    @Query("SELECT module FROM AssessmentModule module WHERE module.moduleId=:moduleId")
    AssessmentModule findByModuleId(Integer moduleId);

    @Executable
    @Query("SELECT tlm.moduleName FROM AssessmentModule tlm")
    List<String> getModuleNames();
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentModuleId;
import com.xact.assessment.models.UserAssessmentModule;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserAssessmentModuleRepository extends CrudRepository<UserAssessmentModule, AssessmentModuleId> {
    @Executable
    @Query("SELECT userAssessment.module from UserAssessmentModule userAssessment WHERE userAssessment.assessment.assessmentId=:assessmentId and userAssessment.module.isActive=true")
    List<AssessmentModule> findModuleByAssessment(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("DELETE FROM UserAssessmentModule userAssessment where userAssessment.assessment.assessmentId=:assessmentId")
    void deleteByModule(Integer assessmentId);

}

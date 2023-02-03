/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.ParameterLevelId;
import com.xact.assessment.models.ParameterLevelRating;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ParameterLevelRatingRepository extends CrudRepository<ParameterLevelRating, ParameterLevelId> {

    @Executable
    @Query("SELECT pla FROM ParameterLevelRating pla WHERE pla.parameterLevelId.assessment.assessmentId=:assessmentId and pla.parameterLevelId.parameter.topic.module.category.isActive=true and pla.parameterLevelId.parameter.topic.module.isActive=true and pla.parameterLevelId.parameter.topic.isActive=true and pla.parameterLevelId.parameter.isActive=true and pla.parameterLevelId.parameter.topic.module.moduleId IN (SELECT userModule.module.moduleId  from  UserAssessmentModule userModule where userModule.assessment.assessmentId=:assessmentId)")
    List<ParameterLevelRating> findByAssessment(@Parameter("assessmentId") Integer assessmentId);
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.ParameterLevelRecommendation;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface ParameterLevelRecommendationRepository extends CrudRepository<ParameterLevelRecommendation, Integer> {


    @Executable
    @Query("SELECT plr FROM ParameterLevelRecommendation plr WHERE plr.assessment.assessmentId=:assessmentId and plr.parameter.topic.module.category.isActive=true and plr.parameter.topic.module.isActive=true and plr.parameter.topic.isActive=true and plr.parameter.isActive=true and plr.parameter.topic.module.moduleId IN(SELECT userModule.module.moduleId  from  UserAssessmentModule userModule where userModule.assessment.assessmentId=:assessmentId)order by plr.recommendationId desc")
    List<ParameterLevelRecommendation> findByAssessment(@Parameter("assessmentId") Integer assessmentId);

}

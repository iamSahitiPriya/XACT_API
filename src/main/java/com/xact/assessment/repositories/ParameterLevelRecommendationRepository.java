/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
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
public interface ParameterLevelRecommendationRepository extends CrudRepository<ParameterLevelRecommendation,Integer> {


    @Executable
    @Query("SELECT tlr FROM ParameterLevelRecommendation tlr WHERE tlr.assessment.assessmentId=:assessmentId and tlr.parameter.parameterId=:parameterId order by tlr.recommendationId")
    List<ParameterLevelRecommendation> findByAssessmentAndParameter(@Parameter("assessmentId") Integer assessmentId, @Parameter("parameterId") Integer parameterId);

    @Executable
    @Query("SELECT tlr FROM ParameterLevelRecommendation tlr WHERE tlr.assessment.assessmentId=:assessmentId and tlr.parameter.topic.module.category.isActive=true and tlr.parameter.topic.module.isActive=true and tlr.parameter.topic.isActive=true and tlr.parameter.isActive=true and tlr.parameter.topic.module.moduleId IN(SELECT userModule.module.moduleId  from  UserAssessmentModule userModule where userModule.assessment.assessmentId=:assessmentId)order by tlr.recommendationId")
    List<ParameterLevelRecommendation> findByAssessment(@Parameter("assessmentId") Integer assessmentId);

}

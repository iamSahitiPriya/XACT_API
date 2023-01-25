/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.models.TopicLevelId;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TopicLevelAssessmentRepository extends CrudRepository<TopicLevelAssessment, TopicLevelId> {

    @Executable
    @Query("SELECT tla FROM TopicLevelAssessment tla WHERE tla.topicLevelId.assessment.assessmentId=:assessmentId and tla.topicLevelId.topic.module.category.isActive=true and tla.topicLevelId.topic.module.isActive=true and tla.topicLevelId.topic.isActive=true and  tla.topicLevelId.topic.module.moduleId IN (SELECT userModule.module.moduleId  from  UserAssessmentModule userModule where userModule.assessment.assessmentId=:assessmentId)")
    List<TopicLevelAssessment> findByAssessment(@Parameter("assessmentId") Integer assessmentId);
}

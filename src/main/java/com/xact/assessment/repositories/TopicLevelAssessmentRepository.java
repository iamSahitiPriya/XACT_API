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
    @Query("SELECT tla FROM TopicLevelAssessment tla WHERE tla.topicLevelId.assessment.assessmentId=:assessmentId")
    List<TopicLevelAssessment> findByAssessment(@Parameter("assessmentId") Integer assessmentId);

}

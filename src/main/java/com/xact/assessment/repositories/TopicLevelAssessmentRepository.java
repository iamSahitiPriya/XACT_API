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

    @Executable
    @Query("select count(tla.topicLevelId.topic.topicId) from TopicLevelAssessment tla where tla.topicLevelId.assessment.assessmentId=:assessmentId")
    Long getAssessedTopics(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query(value = "select sum(param_topic) from (select count(distinct tap.topic) as param_topic from tbl_assessment_parameter t join tbm_assessment_parameter tap on t.parameter_id = tap.parameter_id where t.assessment_id=:assessmentId group by tap.topic) as param_topic", nativeQuery=true)
    Long getAssessedTopicsByParameter(@Parameter("assessmentId") Integer assessmentId);
}

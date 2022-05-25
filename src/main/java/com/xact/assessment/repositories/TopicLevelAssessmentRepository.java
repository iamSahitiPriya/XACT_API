package com.xact.assessment.repositories;

import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.models.TopicLevelId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface TopicLevelAssessmentRepository  extends CrudRepository<TopicLevelAssessment, TopicLevelId> {

}

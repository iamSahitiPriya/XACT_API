package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentTopic;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AssessmentTopicRepository extends CrudRepository<AssessmentTopic, Integer> {
}

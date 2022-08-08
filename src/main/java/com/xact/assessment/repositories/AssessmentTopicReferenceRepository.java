package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentTopicReference;
import io.micronaut.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentTopicReferenceRepository extends CrudRepository<AssessmentTopicReference, Integer> {
}

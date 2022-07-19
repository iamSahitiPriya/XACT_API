package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentTopic;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AssessmentTopicRepository extends CrudRepository<AssessmentTopic, Integer> {
    @Executable
    @Query("SELECT pla FROM AssessmentTopic pla WHERE pla.module.moduleId=:moduleId")
    List<AssessmentTopic> findByModule(@Parameter("moduleId") Integer moduleId);
}

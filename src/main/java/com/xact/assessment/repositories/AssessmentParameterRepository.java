package com.xact.assessment.repositories;


import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentTopic;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AssessmentParameterRepository extends CrudRepository<AssessmentParameter, Integer>{
    @Executable
    @Query("SELECT pla FROM AssessmentParameter pla WHERE pla.topic.topicId=:topicId")
    List<AssessmentParameter> findByTopic(@Parameter("topicId") Integer topicId);
}

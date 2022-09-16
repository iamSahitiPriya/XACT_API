package com.xact.assessment.repositories;

import com.xact.assessment.models.UserAssessmentModule;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserAssessmentModuleRepository extends CrudRepository<UserAssessmentModule,Integer> {

}

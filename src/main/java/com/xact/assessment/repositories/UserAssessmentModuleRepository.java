package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.UserAssessmentModule;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserAssessmentModuleRepository extends CrudRepository<UserAssessmentModule,Integer> {
    @Executable
    @Query("SELECT userAssessment.module from UserAssessmentModule userAssessment WHERE userAssessment.assessment.assessmentId=:assessmentId")
    List<AssessmentModule> findModuleByAssessment(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("DELETE FROM UserAssessmentModule userAssessment where userAssessment.assessment.assessmentId=:assessmentId AND userAssessment.module.moduleId=:moduleId")
    void deleteByModule(Integer assessmentId, Integer moduleId);
}

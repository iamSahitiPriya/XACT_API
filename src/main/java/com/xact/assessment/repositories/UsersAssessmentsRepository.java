/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentRole;
import com.xact.assessment.models.AssessmentUsers;
import com.xact.assessment.models.UserId;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UsersAssessmentsRepository extends CrudRepository<AssessmentUsers, UserId> {

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.userEmail=:userEmail")
    List<AssessmentUsers> findByUserEmail(@Parameter("userEmail") String userEmail);

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.userEmail=:userEmail AND au.userId.assessment.assessmentId=:assessmentId")
    AssessmentUsers findByUserEmail(@Parameter("userEmail") String userEmail, @Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId AND au.role=:role")
    List<AssessmentUsers> findUserByAssessmentId(@Parameter("assessmentId") Integer assessmentId,@Parameter("role") AssessmentRole role);

    @Executable
    @Query("DELETE FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId")
    void deleteById(Integer assessmentId);
}

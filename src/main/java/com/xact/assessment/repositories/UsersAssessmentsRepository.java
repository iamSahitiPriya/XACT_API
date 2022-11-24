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
import java.util.Optional;
import java.util.Set;


@Repository
public interface UsersAssessmentsRepository extends CrudRepository<AssessmentUsers, UserId> {

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.userEmail=:userEmail and au.userId.assessment.isDeleted=false")
    List<AssessmentUsers> findByUserEmail(@Parameter("userEmail") String userEmail);

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.userEmail=:userEmail AND au.userId.assessment.assessmentId=:assessmentId and au.userId.assessment.isDeleted=false")
    AssessmentUsers findByUserEmail(@Parameter("userEmail") String userEmail, @Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId AND au.role=:role")
    List<AssessmentUsers> findUserByAssessmentId(@Parameter("assessmentId") Integer assessmentId,@Parameter("role") AssessmentRole role);

    @Executable
    @Query("DELETE FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId")
    void deleteUsersByAssessmentId(Integer assessmentId);


    @Executable
    @Query("SELECT au FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId AND au.role='Owner'")
    Optional<AssessmentUsers> findOwnerByAssessmentId(@Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("SELECT au.userId.userEmail FROM AssessmentUsers au WHERE au.userId.assessment.assessmentId=:assessmentId")
    Set<String> getAllAssessmentUsers(@Parameter("assessmentId") Integer assessmentId);
}



/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentRole;
import com.xact.assessment.models.AssessmentUser;
import com.xact.assessment.models.UserId;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UsersAssessmentsRepository extends CrudRepository<AssessmentUser, UserId> {

    @Executable
    @Query("SELECT au FROM AssessmentUser au WHERE lower(au.userId.userEmail)=lower(:userEmail) and au.userId.assessment.isDeleted=false")
    List<AssessmentUser> findByUserEmail(@Parameter("userEmail") String userEmail);

    @Executable
    @Query("SELECT au FROM AssessmentUser au WHERE lower(au.userId.userEmail)=lower(:userEmail) AND au.userId.assessment.assessmentId=:assessmentId and au.userId.assessment.isDeleted=false")
    AssessmentUser findByUserEmail(@Parameter("userEmail") String userEmail, @Parameter("assessmentId") Integer assessmentId);

    @Executable
    @Query("SELECT au FROM AssessmentUser au WHERE au.userId.assessment.assessmentId=:assessmentId AND au.role=:role")
    List<AssessmentUser> findUserByAssessmentId(@Parameter("assessmentId") Integer assessmentId, @Parameter("role") AssessmentRole role);

    @Executable
    @Query("DELETE FROM AssessmentUser au WHERE au.userId.assessment.assessmentId=:assessmentId")
    void deleteUsersByAssessmentId(Integer assessmentId);
}



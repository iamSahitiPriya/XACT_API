/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.Assessment;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


@Repository
public interface AssessmentRepository extends CrudRepository<Assessment, Integer> {

    @Executable
    @Query("SELECT tla FROM Assessment tla WHERE (date(tla.createdAt) <= :startDate and date(tla.createdAt) >= :endDate)")
    List<Assessment> totalAssessments(@Parameter("startDate") Date startDate, @Parameter("endDate") Date endDate);
}

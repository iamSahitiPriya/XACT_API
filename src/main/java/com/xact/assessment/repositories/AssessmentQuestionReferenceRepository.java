/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentQuestionReference;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AssessmentQuestionReferenceRepository extends CrudRepository<AssessmentQuestionReference, Integer> {

    @Executable
    @Query("delete from AssessmentQuestionReference  aqr where aqr.question.questionId=:questionId")
    void deleteByQuestionId(Integer questionId);
}

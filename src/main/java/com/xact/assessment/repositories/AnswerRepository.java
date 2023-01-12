/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, AnswerId> {

    @Executable
    @Query("SELECT ans FROM Answer ans WHERE ans.answerId.assessment.assessmentId=:assessmentId")
    List<Answer> findByAssessment(@Parameter("assessmentId") Integer assessmentId);
    @Executable
    @Query("SELECT ans.answerNote FROM Answer ans WHERE ans.answerId.assessment.assessmentId=:assessmentId AND ans.answerId.question.questionId=:questionId")
    String findByQuestionId(Integer assessmentId, Integer questionId);
}

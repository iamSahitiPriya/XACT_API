/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.Question;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {


    @Executable
    @Query("SELECT question from Question question WHERE question.parameter.topic.module.moduleId=:moduleId and not question.questionStatus = 'PUBLISHED'")
    List<Question> getAuthorQuestions(Integer moduleId);

    @Executable
    @Query("SELECT question from Question question WHERE question.parameter.topic.module.moduleId=:moduleId and  question.questionStatus = 'SENT_FOR_REVIEW'")
    List<Question> getReviewerQuestions(Integer moduleId);
}

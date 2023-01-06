/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.Question;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer>{
}

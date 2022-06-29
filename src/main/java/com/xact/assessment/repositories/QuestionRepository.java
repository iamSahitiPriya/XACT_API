package com.xact.assessment.repositories;

import com.xact.assessment.models.Question;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer>{
}

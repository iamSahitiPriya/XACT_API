package com.xact.assessment.repositories;

import com.xact.assessment.models.Answer;
import com.xact.assessment.models.AnswerId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, AnswerId> {

}

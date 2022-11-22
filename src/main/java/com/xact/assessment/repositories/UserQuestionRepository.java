package com.xact.assessment.repositories;

import com.xact.assessment.models.UserQuestion;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserQuestionRepository extends CrudRepository<UserQuestion, String> {
}

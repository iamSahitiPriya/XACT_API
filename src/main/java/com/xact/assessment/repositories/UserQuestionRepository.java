package com.xact.assessment.repositories;

import com.xact.assessment.models.Answer;
import com.xact.assessment.models.UserQuestion;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserQuestionRepository extends CrudRepository<UserQuestion, Integer> {

    @Executable
    @Query("SELECT userQues FROM UserQuestion userQues WHERE userQues.assessment.assessmentId=:assessmentId")
    List<UserQuestion> findByAssessment(@Parameter("assessmentId") Integer assessmentId);
}

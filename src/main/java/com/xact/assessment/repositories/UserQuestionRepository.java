package com.xact.assessment.repositories;

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
    @Query("SELECT userQues FROM UserQuestion userQues WHERE userQues.assessment.assessmentId=:assessmentId AND userQues.parameter.isActive=true AND userQues.parameter.topic.isActive=true AND userQues.parameter.topic.module.isActive = true AND userQues.parameter.topic.module.category.isActive = true AND userQues.parameter.topic.module.moduleId IN (SELECT userModule.module.moduleId  from  UserAssessmentModule userModule where userModule.assessment.assessmentId=:assessmentId) ORDER BY userQues.questionId")
    List<UserQuestion> findByAssessmentId(@Parameter("assessmentId") Integer assessmentId);
}

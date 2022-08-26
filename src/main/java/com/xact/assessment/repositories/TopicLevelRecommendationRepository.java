package com.xact.assessment.repositories;


import com.xact.assessment.models.TopicLevelAssessment;
import com.xact.assessment.models.TopicLevelRecommendation;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface TopicLevelRecommendationRepository extends CrudRepository<TopicLevelRecommendation, Integer> {

    @Executable
    @Query("SELECT tlr FROM TopicLevelRecommendation tlr WHERE tlr.assessment.assessmentId=:assessmentId and tlr.topic.topicId=:topicId order by tlr.recommendationId")
    List<TopicLevelRecommendation> findByAssessmentAndTopic(@Parameter("assessmentId") Integer assessmentId,@Parameter("topicId") Integer topicId);

    @Executable
    @Query("SELECT tlr FROM TopicLevelRecommendation tlr WHERE tlr.assessment.assessmentId=:assessmentId order by tlr.recommendationId")
    List<TopicLevelRecommendation> findByAssessment(@Parameter("assessmentId") Integer assessmentId);


}

package com.xact.assessment.repositories;

import com.xact.assessment.models.ActivityId;
import com.xact.assessment.models.ActivityLog;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActivityLogRepository extends CrudRepository<ActivityLog, ActivityId> {

    @Executable
    @Query("SELECT al from ActivityLog al where al.topic=:topic and al.activityId.assessment=:assessment and al.updatedAt between :startTime  and  :endTime and al.activityId.userName <> :loggedInUser")
    List<ActivityLog> getLatestRecords(Date startTime, Date endTime, Assessment assessment, AssessmentTopic topic, String loggedInUser);
}
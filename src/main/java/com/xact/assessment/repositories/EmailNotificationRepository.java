package com.xact.assessment.repositories;

import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.models.EmailNotifier;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends CrudRepository<EmailNotifier, Integer> {
    @Executable
    @Query("")
    List<EmailNotifier> getDetailsToSendCompleteNotification();



}

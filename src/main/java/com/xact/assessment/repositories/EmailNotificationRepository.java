package com.xact.assessment.repositories;

import com.xact.assessment.models.EmailNotifier;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends CrudRepository<EmailNotifier, Integer> {
    @Executable
    @Query("SELECT emailNotifier from EmailNotifier emailNotifier where emailNotifier.status ='N' and emailNotifier.retries < 5")
    List<EmailNotifier> getNotificationDetailsToBeSend();


}

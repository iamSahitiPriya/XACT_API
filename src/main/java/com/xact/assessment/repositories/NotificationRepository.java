package com.xact.assessment.repositories;

import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findTop50ByStatusAndRetriesLessThan(NotificationStatus status,Integer retries);

}

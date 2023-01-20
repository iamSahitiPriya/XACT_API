package com.xact.assessment.repositories;

import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import io.micronaut.context.annotation.Executable;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findTop50ByStatusAndRetriesLessThan(NotificationStatus status,Integer retries);

    @Executable
    @Query("delete from Notification notification where notification.status = 'Y' and notification.updatedAt between :startDate and :endDate")
    void deleteSentNotifications(Date startDate, Date endDate);

}

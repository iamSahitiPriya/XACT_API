package com.xact.assessment.repositories;

import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.models.NotificationType;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findTop50ByStatusAndRetriesLessThan(NotificationStatus status, Integer retries);

    Notification findByTemplateNameAndPayload(NotificationType templateName, String payload);

    @Executable
    @Query("delete from Notification notification where notification.status = 'Y' and notification.updatedAt < :expiryDate")
    void deleteSentNotifications(@Parameter("expiryDate")Date expiryDate);


}

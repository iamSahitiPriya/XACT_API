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

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Integer> {

    List<Notification> findTop50ByStatusAndRetriesLessThan(NotificationStatus status, Integer retries);

    List<Notification> findByTemplateName(NotificationType templateName);

    @Executable
    @Query("delete from Notification notification where notification.status = 'Y' and notification.updatedAt < :expiryDate")
    void deleteSentNotifications(@Parameter("expiryDate")Date expiryDate);

    @Executable
    @Query("select tln from Notification tln where tln.templateName=:type")
    List<Notification> findByType(@Parameter("type") NotificationType type);
}

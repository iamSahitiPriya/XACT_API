package com.xact.assessment.repositories;

import com.xact.assessment.models.Notification;
import io.micronaut.context.annotation.Executable;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.query.QueryModel;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface EmailNotificationRepository extends CrudRepository<Notification, Integer> {
    @Executable
    @Query("SELECT notification from Notification notification where notification.status ='N' and notification.retries < 5")
    List<Notification> getNotificationDetailsToBeSend();

    @Executable
    @Query("UPDATE Notification notification SET notification.retries=:retries,notification.status='Y' where notification.notificationId=:notificationId")
    void updateNotification(@Parameter("notificationId")Integer notificationId, @Parameter("retries")Integer retries);


}

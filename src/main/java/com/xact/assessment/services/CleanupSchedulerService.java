/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.repositories.ActivityLogRepository;
import com.xact.assessment.repositories.NotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Singleton
public class CleanupSchedulerService {

    public static final int NOTIFICATION_DELAY = 60;
    private static final int ACTIVITY_LOG_DELAY = 1;
    private final NotificationRepository notificationRepository;
    private final ActivityLogRepository activityLogRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupSchedulerService.class);


    public CleanupSchedulerService(NotificationRepository notificationRepository, ActivityLogRepository activityLogRepository) {
        this.notificationRepository = notificationRepository;
        this.activityLogRepository = activityLogRepository;
    }

    @Scheduled(fixedDelay = "${cleanup.notification.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanSentNotifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -NOTIFICATION_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the sent notifications");
        notificationRepository.deleteSentNotifications(expiryDate);
    }

    @Scheduled(fixedDelay = "${cleanup.activityLog.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanExpiredActivityLogs() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -ACTIVITY_LOG_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the expired activity logs");
        activityLogRepository.deleteActivityLogs(expiryDate);
    }
}

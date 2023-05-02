/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services.schedulers;

import com.xact.assessment.services.ActivityLogService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.QuestionService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Singleton
public class CleanupSchedulerService {

    private static final int NOTIFICATION_DELAY = 60;
    private static final int ACTIVITY_LOG_DELAY = 1;
    private static final int DELETION_DELAY = 10;
    private final QuestionService questionService;
    private final NotificationService notificationService;
    private final ActivityLogService activityLogService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupSchedulerService.class);


    public CleanupSchedulerService(QuestionService questionService, NotificationService notificationService, ActivityLogService activityLogService) {
        this.questionService = questionService;
        this.notificationService = notificationService;
        this.activityLogService = activityLogService;
    }

    @Scheduled(fixedDelay = "${cleanup.notification.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanSentNotifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -NOTIFICATION_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the sent notifications");
        notificationService.deleteSentNotifications(expiryDate);
    }

    @Scheduled(fixedDelay = "${cleanup.activityLog.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanExpiredActivityLogs() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -ACTIVITY_LOG_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the expired activity logs");
        activityLogService.deleteActivityLogs(expiryDate);
    }

    @Scheduled(fixedDelay = "${cleanup.rejectedQuestion.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanRejectedQuestions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - DELETION_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the rejected questions");
        questionService.deleteRejectedQuestions(expiryDate);
    }
}

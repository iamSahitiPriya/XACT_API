/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationType;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class NotificationSchedulerService {

    private final NotificationService notificationService;
    public static final int DURATION = 15;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationSchedulerService.class);


    private final AssessmentService assessmentService;

    public NotificationSchedulerService(NotificationService notificationService, AssessmentService assessmentService) {
        this.notificationService = notificationService;
        this.assessmentService = assessmentService;
    }

    @Scheduled(fixedDelay = "${notification.inactive.fixedDelay}")
    public void saveInactiveAssessmentNotification() throws JsonProcessingException {
        LOGGER.info("Sending email for Inactive assessment ...");
        List<Notification> inactiveNotifications = notificationService.getNotificationBy(NotificationType.INACTIVE_V1);
        List<Assessment> inactiveAssessments = assessmentService.findInactiveAssessments(DURATION);
        if (!inactiveAssessments.isEmpty()) {
            for (Assessment inactiveAssessment : inactiveAssessments) {
                notificationService.setNotificationForInactiveAssessment(inactiveAssessment, inactiveNotifications);

            }
        }
    }

    @Scheduled(initialDelay = "${notification.feedback.initialDelay}", fixedDelay = "${notification.feedback.delay}")
    public void saveFeedbackNotificationForFinishedAssessments() {
        LOGGER.info("Sending email for feedback ...");
        List<Assessment> finishedAssessments = assessmentService.getFinishedAssessments();
        List<Notification> notifications = notificationService.findByType(NotificationType.FEEDBACK_V1);
        finishedAssessments.forEach(finishedAssessment ->
            notificationService.saveFeedbackNotificationForFinishedAssessments(finishedAssessment,notifications)
        );
    }
}

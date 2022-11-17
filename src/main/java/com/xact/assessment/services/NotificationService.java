package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.NotificationRepository;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailConfig emailConfig;
    private static final String notificationMessage = "EMail sent successfully!";
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);


    public NotificationService(NotificationRepository notificationRepository, EmailConfig emailConfig) {
        this.notificationRepository = notificationRepository;
        this.emailConfig = emailConfig;
    }

    @SneakyThrows
    public Notification setNotification(Set<String> userEmails) {
        Notification notification = new Notification();
        notification.setUserEmail(String.join(",", userEmails));
        notification.setStatus(NotificationStatus.N);

        return notification;
    }

    public Map<NotificationType, Set<String>> setNotificationTypeByUserRole(Set<AssessmentUsers> assessmentUsers) {
        Map<NotificationType, Set<String>> notifications = new EnumMap<>(NotificationType.class);
        Set<String> facilitatorEmails = new HashSet<>();
        for (AssessmentUsers eachUser : assessmentUsers) {
            if (eachUser.getRole().equals(AssessmentRole.Owner)) {
                notifications.put(NotificationType.Created_V1, Collections.singleton(eachUser.getUserId().getUserEmail()));
            } else if (eachUser.getRole().equals(AssessmentRole.Facilitator)) {
                facilitatorEmails.add(eachUser.getUserId().getUserEmail());
            }
        }
        if (!facilitatorEmails.isEmpty())
            notifications.put(NotificationType.AddUser_V1, facilitatorEmails);

        return notifications;
    }

    private void saveNotification(Notification notification) {
        LOGGER.info("Saving notification for {} {}", notification.getTemplateName(), notification.getPayload());
        try {
            notificationRepository.save(notification);
        } catch (Exception exception) {
            LOGGER.error("Notification not saved");
        }
    }

    public boolean isEmailMasked() {
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }

    public void update(Notification notification, NotificationResponse notificationResponse) {
        notification.setRetries(notification.getRetries() + 1);

        if (notificationResponse.getMessage().equals(notificationMessage)) {
            notification.setStatus(NotificationStatus.Y);
        }

        LOGGER.info("Updating notification retries and status for Notification Id {}", notification.getNotificationId());
        notificationRepository.update(notification);
    }

    @SneakyThrows
    public Notification setNotificationForCompleteAssessment(Assessment assessment, Set<String> assessmentUsers) {
        ObjectMapper objectMapper = new ObjectMapper();
        Notification notification = setNotification(assessmentUsers);
        notification.setTemplateName(NotificationType.Completed_V1);
        Map<String, String> payload = setPayloadForCompleteAssessment(assessment);
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    @SneakyThrows
    public Notification setNotificationForReopenAssessment(Assessment assessment, Set<String> assessmentUsers) {
        ObjectMapper objectMapper = new ObjectMapper();
        Notification notification = setNotification(assessmentUsers);
        notification.setTemplateName(NotificationType.Reopened_V1);
        Map<String, String> payload = setPayloadForReopenAssessment(assessment);
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    public Notification setNotificationForCreateAssessment(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
        Map<NotificationType, Set<String>> notificationsType = setNotificationTypeByUserRole(assessmentUsers);

        notificationsType.forEach((notificationType, users) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> payload = new HashMap<>();
            Notification notification = setNotification(users);
            notification.setTemplateName(notificationType);

            if (isNotificationTypeCreated(notificationType))
                payload = setPayloadForCreateAssessment(assessment);
            else
                payload = setPayloadForAddUser(assessment);

            try {
                notification.setPayload(objectMapper.writeValueAsString(payload));
            } catch (JsonProcessingException e) {
                LOGGER.error("Error while parsing JSON");
            }

            saveNotification(notification);
        });
        return null;
    }

    private boolean isNotificationTypeCreated(NotificationType notificationType) {
        return notificationType.equals(NotificationType.Created_V1);
    }

    private Map<String, String> setPayloadForCompleteAssessment(Assessment assessment) {
        Map<String, String> payload = getPayload(assessment);
        payload.put("organisation_name", assessment.getOrganisation().getOrganisationName());
        payload.put("created_at", assessment.getCreatedAt().toString());

        return payload;
    }

    private Map<String, String> setPayloadForReopenAssessment(Assessment assessment) {
        Map<String, String> payload = getPayload(assessment);
        payload.put("organisation_name", assessment.getOrganisation().getOrganisationName());
        payload.put("created_at", assessment.getCreatedAt().toString());

        return payload;
    }

    private Map<String, String> setPayloadForCreateAssessment(Assessment assessment) {
        Map<String, String> payload = getPayload(assessment);
        payload.put("organisation_name", assessment.getOrganisation().getOrganisationName());
        payload.put("created_at", assessment.getCreatedAt().toString());

        return payload;
    }

    private Map<String, String> setPayloadForAddUser(Assessment assessment) {
        Map<String, String> payload = getPayload(assessment);
        payload.put("organisation_name", assessment.getOrganisation().getOrganisationName());
        payload.put("created_at", assessment.getCreatedAt().toString());

        return payload;
    }

    private Map<String, String> getPayload(Assessment assessment) {
        Map<String, String> payload = new HashMap<>();
        payload.put("assessment_id", String.valueOf(assessment.getAssessmentId()));
        payload.put("assessment_name", assessment.getAssessmentName());
        return payload;
    }
}


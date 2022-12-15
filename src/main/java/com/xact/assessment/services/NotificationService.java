package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.NotificationRepository;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class NotificationService {
    public static final String ORGANISATION_NAME = "organisation_name";
    public static final String CREATED_AT = "created_at";
    public static final String ASSESSMENT_ID = "assessment_id";
    public static final String ASSESSMENT_NAME = "assessment_name";
    private final NotificationRepository notificationRepository;
    private final EmailConfig emailConfig;
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

    private Map<NotificationType, Set<String>> setNotificationTypeByUserRole(Set<AssessmentUser> assessmentUsers) {
        Map<NotificationType, Set<String>> notifications = new EnumMap<>(NotificationType.class);
        Set<String> facilitatorEmails = new HashSet<>();
        for (AssessmentUser eachUser : assessmentUsers) {
            if (eachUser.getRole().equals(AssessmentRole.Owner)) {
                notifications.put(NotificationType.CREATED_V1, Collections.singleton(eachUser.getUserId().getUserEmail()));
            } else if (eachUser.getRole().equals(AssessmentRole.Facilitator)) {
                facilitatorEmails.add(eachUser.getUserId().getUserEmail());
            }
        }
        if (!facilitatorEmails.isEmpty())
            notifications.put(NotificationType.ADD_USER_V1, facilitatorEmails);

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

    public void update(Notification notification) {
        notificationRepository.update(notification);
    }

    @SneakyThrows
    public Notification setNotificationForCompleteAssessment(Assessment assessment) {
        Set<String> users = assessment.getAssessmentUsers().stream().map(assessmentUsers -> assessmentUsers.getUserId().getUserEmail()).collect(Collectors.toSet());
        Notification notification = setNotification(users);
        notification.setTemplateName(NotificationType.COMPLETED_V1);
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    @SneakyThrows
    public Notification setNotificationForDeleteUser(Assessment assessment, Set<String> assessmentUsers) {
        if (!assessmentUsers.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Notification notification = setNotification(assessmentUsers);
            notification.setTemplateName(NotificationType.DELETE_USER_V1);
            Map<String, String> payload = getAssessmentCommonPayload(assessment);
            notification.setPayload(objectMapper.writeValueAsString(payload));

            saveNotification(notification);
            return notification;
        }
        return null;
    }

    @SneakyThrows
    public Notification setNotificationForAddUser(Assessment assessment, Set<String> assessmentUsers) {
        if (!assessmentUsers.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            Notification notification = setNotification(assessmentUsers);
            notification.setTemplateName(NotificationType.ADD_USER_V1);
            Map<String, String> payload = getAssessmentCommonPayload(assessment);
            notification.setPayload(objectMapper.writeValueAsString(payload));

            saveNotification(notification);
            return notification;
        }
        return null;
    }

    @SneakyThrows
    public Notification setNotificationForReopenAssessment(Assessment assessment) {
        Set<String> users = assessment.getAssessmentUsers().stream().map(assessmentUsers -> assessmentUsers.getUserId().getUserEmail()).collect(Collectors.toSet());
        Notification notification = setNotification(users);
        notification.setTemplateName(NotificationType.REOPENED_V1);
        Map<String, String> payload = getAssessmentCommonPayload(assessment);
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setPayload(objectMapper.writeValueAsString(payload));

        saveNotification(notification);
        return notification;
    }

    public Notification setNotificationForCreateAssessment(Assessment assessment) {
        Map<NotificationType, Set<String>> notificationsType = setNotificationTypeByUserRole(assessment.getAssessmentUsers());

        notificationsType.forEach((notificationType, users) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> payload;
            Notification notification = setNotification(users);
            notification.setTemplateName(notificationType);
            payload = getAssessmentCommonPayload(assessment);

            try {
                notification.setPayload(objectMapper.writeValueAsString(payload));
            } catch (JsonProcessingException e) {
                LOGGER.error("Error while parsing JSON");
            }

            saveNotification(notification);
        });
        return null;
    }

    private Map<String, String> getAssessmentCommonPayload(Assessment assessment) {
        Map<String, String> payload = new HashMap<>();
        payload.put(ASSESSMENT_ID, String.valueOf(assessment.getAssessmentId()));
        payload.put(ASSESSMENT_NAME, assessment.getAssessmentName());
        payload.put(ORGANISATION_NAME, assessment.getOrganisation().getOrganisationName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a zz");
        String date = simpleDateFormat.format(assessment.getCreatedAt());
        String time = timeFormat.format(assessment.getCreatedAt());
        String createdAt = date + " " + time;
        payload.put(CREATED_AT, createdAt);
        return payload;
    }
}


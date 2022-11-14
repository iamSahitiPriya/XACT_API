package com.xact.assessment.services;

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
    public void saveNotification(Assessment assessment, Set<String> userEmails, NotificationTemplateType notificationTemplateType ) {
        Notification notification = new Notification();
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setUserEmail(String.join(",",userEmails));
        notification.setTemplateName(notificationTemplateType);
        notification.setStatus(NotificationStatus.N);
        Map<String, String> payload = setPayload(assessment);

        notification.setPayload(objectMapper.writeValueAsString(payload));
        saveNotification(notification);
    }

    private Map<String, String> setPayload(Assessment assessment) {
        Map<String, String> payload = new HashMap<>();
        payload.put("assessment_id", String.valueOf(assessment.getAssessmentId()));
        payload.put("assessment_name", assessment.getAssessmentName());
        payload.put("organisation_name", assessment.getOrganisation().getOrganisationName());
        payload.put("created_at", assessment.getCreatedAt().toString());

        return payload;
    }

    public Map<NotificationTemplateType, Set<String>> setNotificationTypeByUserRole(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
        HashMap<NotificationTemplateType,Set<String>> notifications = new HashMap<>();
        HashSet<String> facilitatorEmails = new HashSet<>();
        for(AssessmentUsers eachUser : assessmentUsers) {
            if (eachUser.getRole().equals(AssessmentRole.Owner)) {
                notifications.put(NotificationTemplateType.Created, Collections.singleton(eachUser.getUserId().getUserEmail()));
            }
            else if (eachUser.getRole().equals(AssessmentRole.Facilitator)) {
               facilitatorEmails.add(eachUser.getUserId().getUserEmail());
            }
        }
        if(!facilitatorEmails.isEmpty())
            notifications.put(NotificationTemplateType.AddUser,facilitatorEmails);

        setNotificationsByUserRole(assessment, notifications);
        return notifications;
    }

    private void setNotificationsByUserRole(Assessment assessment, HashMap<NotificationTemplateType, Set<String>> notifications) {
        notifications.forEach((notificationTemplateType, userEmails) -> {
            saveNotification(assessment, userEmails, notificationTemplateType);
        });
    }

    private void saveNotification(Notification notification) {
        LOGGER.info("Saving notification for {} {}", notification.getTemplateName(), notification.getPayload());
        try {
            notificationRepository.save(notification);
        }catch (Exception exception) {
            LOGGER.error("Notification not saved");
        }
    }

    public boolean isEmailMasked(){
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }

    public void update(Notification notification, NotificationResponse notificationResponse) {
        notification.setRetries(notification.getRetries() + 1);
        notification.setStatus(NotificationStatus.Y);

        if(notificationResponse.getMessage().equals(notificationMessage)) {
            LOGGER.info("Updating notification retries and status for Notification Id {}", notification.getNotificationId());
            notificationRepository.update(notification);
        }
    }
}


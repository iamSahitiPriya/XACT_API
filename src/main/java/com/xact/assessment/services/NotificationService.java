package com.xact.assessment.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.EmailNotificationRepository;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class NotificationService {
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailConfig emailConfig;
    private static final String notificationMessage = "EMail sent successfully!";
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);


    public NotificationService(EmailNotificationRepository emailNotificationRepository, EmailConfig emailConfig) {
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailConfig = emailConfig;
    }

    @SneakyThrows
    public void saveNotification(Assessment assessment, Set<String> userEmails, NotificationTemplateType notificationTemplateType ) {
        Notification notification = new Notification();
        ObjectMapper objectMapper = new ObjectMapper();
        notification.setUserEmail(String.join(",",userEmails));
        notification.setTemplateName(notificationTemplateType);
        notification.setStatus(NotificationStatus.N);
        Map<String, String> payload = new HashMap<>();
        payload.put("assessment_id", String.valueOf(assessment.getAssessmentId()));
        payload.put("assessment_name", assessment.getAssessmentName());

        notification.setPayload(objectMapper.writeValueAsString(payload));
        saveNotification(notification);
    }

    public Void setNotificationTypeByUserRole(Assessment assessment, Set<AssessmentUsers> assessmentUsers) {
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
        notifications.put(NotificationTemplateType.AddUser,facilitatorEmails);


        setNotificationsByUserRole(assessment, notifications);
        return null;
    }

    private void setNotificationsByUserRole(Assessment assessment, HashMap<NotificationTemplateType, Set<String>> notifications) {
        notifications.forEach((notificationTemplateType, userEmails) -> {
            saveNotification(assessment, userEmails, notificationTemplateType);
        });
    }

    private void saveNotification(Notification notification) {
        LOGGER.info("Saving notification for {} {}", notification.getTemplateName(), notification.getPayload());
        try {
            emailNotificationRepository.save(notification);
        }catch (Exception exception) {
            LOGGER.error("Notification not saved");
        }

    }

    public boolean isEmailMasked(){
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }

    public void update(Notification notification, NotificationResponse notificationResponse) {
        notification.setRetries(notification.getRetries() + 1);

        if(notificationResponse.getMessage().equals(notificationMessage)) {
            LOGGER.info("Updating notification retries and status for Notification Id {}", notification.getNotificationId());
            emailNotificationRepository.updateNotification(notification.getNotificationId(),notification.getRetries());
        }
    }
}


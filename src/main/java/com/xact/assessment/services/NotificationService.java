package com.xact.assessment.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.models.NotificationTemplateType;
import com.xact.assessment.repositories.EmailNotificationRepository;
import jakarta.inject.Singleton;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public void setNotification(Assessment assessment, Set<String> userEmails, NotificationTemplateType notificationTemplateType ) {
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

//    public void setNotificationByRole(Assessment assessment, AssessmentUsers eachUser) {
//        if(eachUser.getRole().equals(AssessmentRole.Owner))
//            setNotification(assessment, eachUser.getUserId().getUserEmail(),NotificationTemplateType.Created);
//        else if(eachUser.getRole().equals(AssessmentRole.Facilitator))
//            setNotification(assessment, eachUser.getUserId().getUserEmail(),NotificationTemplateType.AddUser);
//    }

    private void saveNotification(Notification notification) {
        LOGGER.info("Saving notification for {} {}", notification.getTemplateName(), notification.getPayload());
        emailNotificationRepository.save(notification);
    }

    public boolean isEmailMasked(){
        return emailConfig.isNotificationEnabled() && emailConfig.isMaskEmail();
    }

    public void update(Notification notification, NotificationResponse notificationResponse) {
        notification.setRetries(notification.getRetries() + 1);

        if(notificationResponse.getMessage().equals(notificationMessage)) {
            LOGGER.info("Updating notification retries and status{}", notification.getNotificationId());
            emailNotificationRepository.updateNotification(notification.getNotificationId(),notification.getRetries());
        }

    }
}


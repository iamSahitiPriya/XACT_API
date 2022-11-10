/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Notification;
import com.xact.assessment.repositories.NotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Singleton
public class EmailSchedulerService {
    private final EmailConfig emailConfig;
    private final NotificationRepository notificationRepository;
    private final TokenService tokenService;
    private final EmailNotificationClient emailNotificationClient;
    private final NotificationService notificationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSchedulerService.class);

    public EmailSchedulerService(EmailConfig emailConfig, NotificationRepository notificationRepository, TokenService tokenService, EmailNotificationClient emailNotificationClient, NotificationService notificationService) {
        this.emailConfig = emailConfig;
        this.notificationRepository = notificationRepository;
        this.tokenService = tokenService;
        this.emailNotificationClient = emailNotificationClient;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotifications() throws JsonProcessingException {
        if(emailConfig.isNotificationEnabled()) {
            List<Notification> notificationList = notificationRepository.getNotificationDetailsToBeSend();

            if (!notificationList.isEmpty()) {
                String accessToken = "Bearer " + tokenService.getToken(emailConfig.getScope());
                for (Notification notification : notificationList) {
                    setUserEmail(notification);
                    String body = setEmailParameters(notification);
                    sendEmail(accessToken,notification,body);
                }
            }
        }
    }

    private void setUserEmail(Notification notification) {
        if(emailConfig.isMaskEmail()) {
            notification.setUserEmail(emailConfig.getDefaultEmail());
        }
    }

    private String setEmailParameters(Notification notification) throws JsonProcessingException {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationDetail notificationDetail= new NotificationDetail();
        notificationDetail.setFrom(new EmailHeader());
        notificationDetail.setSubject(notification.getTemplateName().getEmailSubject());
        notificationDetail.setTo(new ArrayList<>(Arrays.asList(notification.getUserEmail().split(","))));
        notificationDetail.setBcc(new ArrayList<>());
        notificationDetail.setCc(new ArrayList<>());
        notificationDetail.setReplyTo("");
        notificationDetail.setContentType("text/html");
        String notificationContent = getNotificationContent(notification);

        notificationDetail.setContent(notificationContent);

        notificationRequest.setEmail(notificationDetail);

        LOGGER.info("Sending notification to {}",notificationDetail.getTo());

        return new ObjectMapper().writeValueAsString(notificationRequest);
    }

    private String getNotificationContent(Notification notification) throws JsonProcessingException {
        setVelocityProperty();

        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();

        Template template = Velocity.getTemplate("templates/"+ notification.getTemplateName().getTemplateResource());

        EmailPayload emailPayload = new ObjectMapper().readValue(notification.getPayload(),EmailPayload.class);
        context.put("assessment", emailPayload);

        template.merge(context,writer);

        return writer.toString();
    }

    private void sendEmail(String accessToken, Notification notification, String body) {
        NotificationResponse notificationResponse = emailNotificationClient.sendNotification(accessToken, body);

        notificationService.update(notification,notificationResponse);
    }

    private void setVelocityProperty() {
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init( p );
    }

}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.EmailHeader;
import com.xact.assessment.dtos.EmailPayload;
import com.xact.assessment.dtos.NotificationDetail;
import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.utils.NamingConventionUtil;
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
    private static final Integer MAXIMUM_RETRIES = 6;
    private final EmailConfig emailConfig;
    private final ProfileConfig profileConfig;
    private final TokenService tokenService;
    private final EmailNotificationClient emailNotificationClient;
    private final NotificationService notificationService;
    private final NamingConventionUtil namingConventionUtil = new NamingConventionUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSchedulerService.class);

    public EmailSchedulerService(EmailConfig emailConfig, ProfileConfig profileConfig, TokenService tokenService, EmailNotificationClient emailNotificationClient, NotificationService notificationService) {
        this.emailConfig = emailConfig;
        this.profileConfig = profileConfig;
        this.tokenService = tokenService;
        this.emailNotificationClient = emailNotificationClient;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotifications() throws JsonProcessingException {
        LOGGER.info("Scheduling notifications ...");
        if (emailConfig.isNotificationEnabled()) {
            List<Notification> notificationList = notificationService.getTop50ByStatusAndRetriesLessThan(MAXIMUM_RETRIES);
            if (!notificationList.isEmpty()) {
                String accessToken = "Bearer " + tokenService.getToken(emailConfig.getScope());
                for (Notification notification : notificationList) {
                    String emailTo = setUserEmail(notification);
                    NotificationRequest notificationRequest = getEmailBody(notification, emailTo);
                    sendEmail(accessToken, notification, notificationRequest);
                }
            }
        }
    }

    private String setUserEmail(Notification notification) {
        if (emailConfig.isMaskEmail()) {
            return emailConfig.getDefaultEmail();
        }
        return notification.getUserEmail();
    }

    private NotificationRequest getEmailBody(Notification notification, String emailTo) throws JsonProcessingException {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationDetail notificationDetail = new NotificationDetail();
        notificationDetail.setFrom(new EmailHeader(emailConfig.getFromEmail(), emailConfig.getName()));
        notificationDetail.setSubject(notification.getTemplateName().getEmailSubject());
        notificationDetail.setTo(Arrays.asList(emailTo.split(",")));
        notificationDetail.setBcc(new ArrayList<>());
        notificationDetail.setCc(new ArrayList<>());
        notificationDetail.setReplyTo("");
        notificationDetail.setContentType("text/html");
        String notificationContent = getNotificationContent(notification);

        notificationDetail.setContent(notificationContent);

        notificationRequest.setEmail(notificationDetail);

        return notificationRequest;
    }

    private String getNotificationContent(Notification notification) throws JsonProcessingException {
        setVelocityProperty();

        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();

        Template template = Velocity.getTemplate("templates/" + notification.getTemplateName().getTemplateResource());

        EmailPayload emailPayload = new ObjectMapper().readValue(notification.getPayload(), EmailPayload.class);
        emailPayload.setOrganisationName(namingConventionUtil.convertToPascalCase(emailPayload.getOrganisationName()));

        context.put("assessment", emailPayload);
        context.put("url", profileConfig.getUrl());
        context.put("homePageUrl", profileConfig.getHomePageUrl());
        context.put("microSiteUrl", profileConfig.getMicroSiteUrl());
        context.put("supportUrl", profileConfig.getSupportUrl());

        template.merge(context, writer);

        return writer.toString();
    }

    private void sendEmail(String accessToken, Notification notification, NotificationRequest notificationRequest) {
        LOGGER.info("Sending notification {} {}", notification.getNotificationId(),notification.getTemplateName());
        notification.setRetries(notification.getRetries() + 1);
        notificationService.update(notification);

        emailNotificationClient.sendNotification(accessToken, notificationRequest);

        notification.setStatus(NotificationStatus.Y);
        notificationService.update(notification);
    }

    private void setVelocityProperty() {
        Properties p = new Properties();
        p.setProperty("resource.loaders", "class");
        p.setProperty("resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(p);
    }

}

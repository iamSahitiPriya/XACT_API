/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.repositories.NotificationRepository;
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
    private static final Integer MAXIMUM_RETRIES = 6 ;
    private final EmailConfig emailConfig;
    private final ProfileConfig profileConfig;
    private final NotificationRepository notificationRepository;
    private final TokenService tokenService;
    private final EmailNotificationClient emailNotificationClient;
    private final NotificationService notificationService;
    private final NamingConventionUtil namingConventionUtil = new NamingConventionUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSchedulerService.class);

    public EmailSchedulerService(EmailConfig emailConfig, ProfileConfig profileConfig, NotificationRepository notificationRepository, TokenService tokenService, EmailNotificationClient emailNotificationClient, NotificationService notificationService) {
        this.emailConfig = emailConfig;
        this.profileConfig = profileConfig;
        this.notificationRepository = notificationRepository;
        this.tokenService = tokenService;
        this.emailNotificationClient = emailNotificationClient;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotifications() throws JsonProcessingException {
        if(emailConfig.isNotificationEnabled()) {
            List<Notification> notificationList = notificationRepository.findTop50ByStatusAndRetriesLessThan(NotificationStatus.N,MAXIMUM_RETRIES);
            if (!notificationList.isEmpty()) {
                String accessToken = "Bearer " + tokenService.getToken(emailConfig.getScope());
                for (Notification notification : notificationList) {
                    String emailTo = setUserEmail(notification);
                    String body = getEmailBody(notification,emailTo);
                    sendEmail(accessToken,notification,emailTo,body);
                }
            }
        }
    }

    private String setUserEmail(Notification notification) {
        if(emailConfig.isMaskEmail()) {
            return emailConfig.getDefaultEmail();
        }
        return notification.getUserEmail();
    }

    private String getEmailBody(Notification notification, String emailTo) throws JsonProcessingException {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationDetail notificationDetail= new NotificationDetail();
        notificationDetail.setFrom(new EmailHeader(emailConfig.getFromEmail(),emailConfig.getName()));
        notificationDetail.setSubject(notification.getTemplateName().getEmailSubject());
        notificationDetail.setTo(new ArrayList<>(Arrays.asList(emailTo.split(","))));
        notificationDetail.setBcc(new ArrayList<>());
        notificationDetail.setCc(new ArrayList<>());
        notificationDetail.setReplyTo("");
        notificationDetail.setContentType("text/html");
        String notificationContent = getNotificationContent(notification);

        notificationDetail.setContent(notificationContent);

        notificationRequest.setEmail(notificationDetail);

        return new ObjectMapper().writeValueAsString(notificationRequest);
    }

    private String getNotificationContent(Notification notification) throws JsonProcessingException {
        setVelocityProperty();

        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();

        String version = (notification.getTemplateName().toString()).split("_")[1];

        Template template = Velocity.getTemplate("templates/"+ version + "_" + notification.getTemplateName().getTemplateResource() );

        EmailPayload emailPayload = new ObjectMapper().readValue(notification.getPayload(),EmailPayload.class);
        emailPayload.setOrganisationName(namingConventionUtil.convertToPascalCase(emailPayload.getOrganisationName()));

        context.put("assessment", emailPayload);
        context.put("url",profileConfig.getUrl());
        context.put("homePageUrl",profileConfig.getHomePageUrl());

        template.merge(context,writer);

        return writer.toString();
    }

    private void sendEmail(String accessToken, Notification notification, String emailTo, String body) {
        LOGGER.info("Sending notification to {}",emailTo);

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

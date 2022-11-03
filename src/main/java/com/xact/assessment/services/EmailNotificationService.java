package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.EmailHeader;
import com.xact.assessment.dtos.NotificationDetail;
import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.models.EmailNotifier;
import com.xact.assessment.models.NotificationTemplateType;
import com.xact.assessment.repositories.EmailNotificationRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class EmailNotificationService {
    private  final  TokenService tokenService;
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailConfig emailConfig;
    private final EmailNotificationClient emailNotificationClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);


    public EmailNotificationService(TokenService tokenService, EmailNotificationRepository emailNotificationRepository, EmailConfig emailConfig, EmailNotificationClient emailNotificationClient) {
        this.tokenService = tokenService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailConfig = emailConfig;
        this.emailNotificationClient = emailNotificationClient;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotificationForCompleteStatus() throws JsonProcessingException {
        List<EmailNotifier> emailNotifierList = (List<EmailNotifier>) emailNotificationRepository.findAll();
        System.out.println("service called");

        if(!emailNotifierList.isEmpty()){
            String accessToken = "Bearer " + tokenService.getToken(emailConfig.getScope());
            for (EmailNotifier emailNotifier:emailNotifierList) {

                sendEmailNotification(accessToken,emailNotifier.getUserEmail(), emailNotifier.getTemplateName(),
                        "Your Assessment has been Completed!");
            }
        }
    }

    private void sendEmailNotification(String accessToken, String receiver,NotificationTemplateType templateType,String subject ) throws JsonProcessingException {
        NotificationRequest notificationRequest = new NotificationRequest();
        NotificationDetail notificationDetail= new NotificationDetail();
        notificationDetail.setFrom(new EmailHeader());
        notificationDetail.setSubject(subject);
        notificationDetail.setTo(Collections.singletonList(receiver));
        notificationDetail.setBcc(new ArrayList<>());
        notificationDetail.setCc(new ArrayList<>());
        notificationDetail.setReplyTo("");
        notificationDetail.setContentType("text/html");
        notificationDetail.setContent("Completed");

        notificationRequest.setEmail(notificationDetail);
        String json = new ObjectMapper().writeValueAsString(notificationRequest);

        LOGGER.info("Sending notification to {}",notificationDetail.getTo());
        HttpResponse httpResponse =emailNotificationClient.sendNotification(accessToken,json);

    }
}


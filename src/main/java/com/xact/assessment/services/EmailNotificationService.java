package com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.EmailHeader;
import com.xact.assessment.dtos.NotificationDetail;
import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.models.EmailNotifier;
import com.xact.assessment.models.NotificationTemplateType;
import com.xact.assessment.repositories.EmailNotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class EmailNotificationService {
    private  final  TokenService tokenService;
    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailConfig emailConfig;

    public EmailNotificationService(TokenService tokenService, EmailNotificationRepository emailNotificationRepository, EmailConfig emailConfig) {
        this.tokenService = tokenService;
        this.emailNotificationRepository = emailNotificationRepository;
        this.emailConfig = emailConfig;
    }

    @Scheduled(fixedDelay = "${email.delay}")
    public void sendEmailNotificationForCompleteStatus() throws JsonProcessingException {
        List<EmailNotifier> emailNotifierList = (List<EmailNotifier>) emailNotificationRepository.findAll();
        System.out.println("service called");
        if(!emailNotifierList.isEmpty()){
        System.out.println(emailNotifierList.get(0).getPayLoad());
        sendEmailNotification("wgduxksgcyuoi klh","ruchika@thoughtworks.com", NotificationTemplateType.Completed,"new",
                "Your Assessment has been Completed!");
        }

//        if(!emailNotifierList.isEmpty()){
//            String accessToken = tokenService.getToken(emailConfig.getScope());
//            for (EmailNotifier emailNotifier:emailNotifierList) {
//                sendEmailNotification(accessToken,emailNotifier.getUserEmail(), emailNotifier.getTemplateName(),emailNotifier.getAssessment().getAssessmentName(),
//                        "Your Assessment has been Completed!");
//            }
//        }
//        else{
//            System.out.println(emailNotifierList.get(0).getPayLoad());
//            sendEmailNotification("wgduxksgcyuoi klh","ruchika@thoughtworks.com", NotificationTemplateType.Completed,"new",
//                    "Your Assessment has been Completed!");
//
//        }


    }

    private void sendEmailNotification(String accessToken, String receiver,NotificationTemplateType templateType,String assessmentName,String subject ) throws JsonProcessingException {
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
        System.out.println(json);

    }
}


/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.EmailNotificationRepository;
import com.xact.assessment.services.EmailNotificationService;
import com.xact.assessment.services.TokenService;
import io.micronaut.test.annotation.MockBean;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class EmailNotificationServiceTest {

    EmailNotificationClient emailNotificationClient = mock(EmailNotificationClient.class);

    EmailNotificationService emailNotificationService;
    TokenService tokenService  = mock(TokenService.class);;
    EmailNotificationRepository emailNotificationRepository = mock(EmailNotificationRepository.class);
    EmailConfig emailConfig = mock(EmailConfig.class);

    public EmailNotificationServiceTest() {
    emailNotificationService = new EmailNotificationService(tokenService,emailNotificationRepository,emailConfig,emailNotificationClient); }

    @Test
    void shouldSendEmailNotification() throws JsonProcessingException {
        NotificationResponse notificationResponse = new NotificationResponse("1","EMail sent successfully!");
        String scope = "email.send";
        EmailNotifier emailNotifier = new EmailNotifier(1, NotificationTemplateType.Created,"brindha.e@thoughtworks.com","{assessment_id:1,assessment_name :" + "\"fintech\"}", NotificationStatus.N,0,new Date(),new Date());
        List<EmailNotifier> emailNotifierList = new ArrayList<>();
        emailNotifierList.add(emailNotifier);
        when(emailConfig.getScope()).thenReturn(scope);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("", 1, "abc", "", new Date());
        String token = "Bearer " + accessTokenResponse.getAccess_token();
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccess_token());
        String json = "{\"email\":{\"subject\":\"Assessment Created\",\"to\":[\"brindha.e@thoughtworks.com\"],\"cc\":[],\"bcc\":[],\"from\":{\"email\":\"project-xact@thoughtworks.net\",\"name\":\"X-ACT Support\"},\"replyTo\":\"\",\"contentType\":\"text/html\",\"content\":\"<html>\\n<body>\\n<h3>Hello User,</h3>\\n<p>You have created the Assessment!!</p>\\n</body>\\n</html>\\n\"}}";

        when(emailNotificationRepository.getNotificationDetailsToBeSend()).thenReturn(emailNotifierList);
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccess_token());
        Mockito.when(emailNotificationClient.sendNotification(token,json)).thenReturn(notificationResponse);
        when(emailNotificationRepository.update(emailNotifier)).thenReturn(emailNotifier);


        emailNotificationService.sendEmailNotifications();

        verify(emailNotificationRepository).update(emailNotifier);
    }


    @Test
    void shouldSetNotificationTypeAsCreatedForAssessmentOwner() {
        String email = "abc@thoughtworks.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(email,assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        assessment.setAssessmentUsers(Collections.singleton(assessmentUser));
        EmailNotifier emailNotifier = new EmailNotifier(1,NotificationTemplateType.Created,email,"",NotificationStatus.N,0,new Date(),new Date());
        when(emailNotificationRepository.save(emailNotifier)).thenReturn(emailNotifier);

        emailNotificationService.setNotificationByRole(assessment,assessmentUser);
        emailNotificationRepository.save(emailNotifier);

        verify(emailNotificationRepository).save(emailNotifier);
    }

    @Test
    void shouldSetNotificationTypeAsAddUserForAssessmentFacilitator() {
        String email = "abc@thoughtworks.com";
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(email,assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        assessment.setAssessmentUsers(Collections.singleton(assessmentUser));
        EmailNotifier emailNotifier = new EmailNotifier(1,NotificationTemplateType.AddUser,email,"",NotificationStatus.N,0,new Date(),new Date());
        when(emailNotificationRepository.save(emailNotifier)).thenReturn(emailNotifier);

        emailNotificationService.setNotificationByRole(assessment,assessmentUser);
        emailNotificationRepository.save(emailNotifier);

        verify(emailNotificationRepository).save(emailNotifier);
    }
}

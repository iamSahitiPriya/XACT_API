/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.models.NotificationTemplateType;
import com.xact.assessment.repositories.NotificationRepository;
import com.xact.assessment.services.EmailSchedulerService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.TokenService;
import com.xact.assessment.utils.NamingConventionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class EmailSchedulerServiceTest {
    private EmailSchedulerService emailSchedulerService;
    private final EmailConfig emailConfig;
    private final TokenService tokenService;
    private final NotificationRepository notificationRepository;
    private final EmailNotificationClient emailNotificationClient;
    private final NotificationService notificationService;
    private final ProfileConfig profileConfig;
    private final NamingConventionUtil namingConventionUtil;

    public EmailSchedulerServiceTest() {
        emailConfig = mock(EmailConfig.class);
        tokenService = mock(TokenService.class);
        notificationRepository = mock(NotificationRepository.class);
        emailNotificationClient = mock(EmailNotificationClient.class);
        notificationService = mock(NotificationService.class);
        profileConfig = mock(ProfileConfig.class);
        namingConventionUtil = mock(NamingConventionUtil.class);

        emailSchedulerService = new EmailSchedulerService(emailConfig, profileConfig, notificationRepository,tokenService,emailNotificationClient,notificationService);
    }

    @Test
    void shouldSendEmailNotification() throws JsonProcessingException {
        NotificationResponse notificationResponse = new NotificationResponse("1","EMail sent successfully!");
        String scope = "email.send";
        Notification notification = new Notification(1, NotificationTemplateType.Created,"brindha.e@thoughtworks.com","{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.N,0,new Date(),new Date());
        Notification notification1 = new Notification(1, NotificationTemplateType.Created,"brindha.e@thoughtworks.com","{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.Y,0,new Date(),new Date());
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        when(emailConfig.isNotificationEnabled()).thenReturn(true);
        when(emailConfig.getScope()).thenReturn(scope);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("", 1, "abc", "", new Date());
        String token = "Bearer " + accessTokenResponse.getAccessToken();
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccessToken());
        String json = "{\"email\":{\"subject\":\"Assessment Created\",\"to\":[\"brindha.e@thoughtworks.com\"],\"cc\":[],\"bcc\":[],\"from\":{\"email\":\"project-xact@thoughtworks.net\",\"name\":\"X-ACT Support\"},\"replyTo\":\"\",\"contentType\":\"text/html\",\"content\":\"<html>\\n<body>\\n<div style=\\\"height:50%; width: 50%;margin: 10%;background-color: white;\\\">\\n    <hr style=\\\"height: 6px;background: #4BA1AC; border: none;\\\"/>\\n    <h1 style=\\\"color: #4BA1AC; font-size: 40px;\\\">X-ACT</h1>\\n    <h3 style=\\\"padding-left: 5px;font-size: 30px;\\\">Hello User,</h3>\\n    <p style=\\\"padding-left: 5px;font-size: 20px;\\\">You have created the Assessment <b style=\\\"color:#4BA1AC ;\\\">fintech</b>!!</p>\\n</div>\\n</body>\\n</html>\\n\"}}";

        when(notificationRepository.getNotificationDetailsToBeSend()).thenReturn(notificationList);
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccessToken());
        when(emailNotificationClient.sendNotification(token,json)).thenReturn(notificationResponse);
        when(notificationRepository.updateNotification(notification.getNotificationId(),notification.getRetries())).thenReturn(notification1);
        doNothing().when(notificationService).update(notification,notificationResponse);

        emailSchedulerService.sendEmailNotifications();
        Notification notification2 = notificationRepository.updateNotification(notification.getNotificationId(),notification.getRetries());

        Assertions.assertEquals(NotificationStatus.Y,notification2.getStatus());
    }
}

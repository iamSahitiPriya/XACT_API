/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.client.EmailNotificationClient;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.config.ProfileConfig;
import com.xact.assessment.dtos.NotificationDetail;
import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.AccessTokenResponse;
import com.xact.assessment.models.Notification;
import com.xact.assessment.models.NotificationStatus;
import com.xact.assessment.models.NotificationType;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.TokenService;
import com.xact.assessment.services.schedulers.EmailSchedulerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

class EmailSchedulerServiceTest {
    private EmailSchedulerService emailSchedulerService;
    private final EmailConfig emailConfig;
    private final TokenService tokenService;
    private final EmailNotificationClient emailNotificationClient;
    private final NotificationService notificationService;
    private final ProfileConfig profileConfig;
    private final AssessmentService assessmentService;

    public EmailSchedulerServiceTest() {
        assessmentService = mock(AssessmentService.class);
        emailConfig = mock(EmailConfig.class);
        tokenService = mock(TokenService.class);
        emailNotificationClient = mock(EmailNotificationClient.class);
        notificationService = mock(NotificationService.class);
        profileConfig = mock(ProfileConfig.class);

        emailSchedulerService = new EmailSchedulerService(emailConfig, profileConfig, tokenService, emailNotificationClient, notificationService);
    }

    @Test
    void shouldSendEmailNotification() throws JsonProcessingException {
        NotificationResponse notificationResponse = new NotificationResponse("1", "EMail sent successfully!");
        String scope = "email.send";
        Notification notification = new Notification(1, NotificationType.CREATED_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\",\"created_at\":\"1-Dec-22 06:32 pm\"}", NotificationStatus.N, 0, new Date(), new Date());
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);
        when(emailConfig.isNotificationEnabled()).thenReturn(true);
        when(emailConfig.getScope()).thenReturn(scope);
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse("", 1, "abc", "", new Date());
        String token = "Bearer " + accessTokenResponse.getAccessToken();
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccessToken());

        var notificationRequest = new NotificationRequest();
        var notificationEmail = new NotificationDetail();
        notificationEmail.setSubject("Assessment Created");
        notificationEmail.setTo(Arrays.asList("brindha.e@thoughtworks.com"));
        notificationEmail.setContentType("html/text");
        notificationRequest.setEmail(notificationEmail);


        when(notificationService.getTop50ByStatusAndRetriesLessThan( 6)).thenReturn(notificationList);
        when(tokenService.getToken(scope)).thenReturn(accessTokenResponse.getAccessToken());
        when(emailNotificationClient.sendNotification(token, notificationRequest)).thenReturn(notificationResponse);
        doNothing().when(notificationService).update(notification);

        emailSchedulerService.sendEmailNotifications();
          notificationService.update(notification);

        Assertions.assertEquals(NotificationStatus.Y, notification.getStatus());
    }
}

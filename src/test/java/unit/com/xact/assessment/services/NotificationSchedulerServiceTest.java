/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.dtos.NotificationDetail;
import com.xact.assessment.dtos.NotificationRequest;
import com.xact.assessment.dtos.NotificationResponse;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.NotificationSchedulerService;
import com.xact.assessment.services.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class NotificationSchedulerServiceTest {
    private final AssessmentService assessmentService;
    private final EmailConfig emailConfig;
    private final NotificationService notificationService;
    private final NotificationSchedulerService notificationSchedulerService;

    public NotificationSchedulerServiceTest() {
        assessmentService = mock(AssessmentService.class);
        emailConfig = mock(EmailConfig.class);
        notificationService = mock(NotificationService.class);
        notificationSchedulerService = new NotificationSchedulerService(notificationService, assessmentService);


    }

    @Test
    void shouldSendNotificationForInactiveAssessment() throws JsonProcessingException {
        NotificationResponse notificationResponse = new NotificationResponse("1", "EMail sent successfully!");
        String scope = "email.send";
        Notification notification = new Notification(1, NotificationType.INACTIVE_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\",\"created_at\":\"1-Dec-22 06:32 pm\"}", NotificationStatus.N, 0, new Date(), new Date());
        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(notification);

        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        var notificationRequest = new NotificationRequest();
        var notificationEmail = new NotificationDetail();
        notificationEmail.setSubject("Assessment Inactive");
        notificationEmail.setTo(Arrays.asList("brindha.e@thoughtworks.com"));
        notificationEmail.setContentType("html/text");
        notificationRequest.setEmail(notificationEmail);

        when(notificationService.getTop50ByStatusAndRetriesLessThan(6)).thenReturn(notificationList);
        when(assessmentService.findInactiveAssessments(15)).thenReturn(Collections.singletonList(assessment));
        doNothing().when(notificationService).update(notification);

        notificationSchedulerService.sendInactiveAssessmentNotification();
        notificationService.update(notification);

        Assertions.assertEquals(NotificationStatus.N, notification.getStatus());
    }
}

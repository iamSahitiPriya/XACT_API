/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.config.EmailConfig;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.NotificationRepository;
import com.xact.assessment.services.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    NotificationService notificationService;
    NotificationRepository notificationRepository = mock(NotificationRepository.class);
    EmailConfig emailConfig = mock(EmailConfig.class);

    public NotificationServiceTest() {
        notificationService = new NotificationService(notificationRepository, emailConfig);
    }

    @Test
    void shouldSetNotificationWhenAssessmentIsCreated() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUsers assessmentUser1 = new AssessmentUsers();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);

        Notification notification = new Notification(1, NotificationType.ADD_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);


        notificationService.setNotificationForCreateAssessment(assessment, assessmentUsers);
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }


    @Test
    void shouldReturnTrueWhenNotificationEnabledAndMaskEnabled() {
        when(emailConfig.isMaskEmail()).thenReturn(true);
        when(emailConfig.isNotificationEnabled()).thenReturn(true);

        Assertions.assertTrue(notificationService.isEmailMasked());
    }

    @Test
    void shouldUpdateNotificationAfterSendingNotification() {
        Notification notification = new Notification(1, NotificationType.CREATED_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.N, 0, new Date(), new Date());
        Notification notification1 = new Notification(1, NotificationType.CREATED_V1, "brindha.e@thoughtworks.com", "{\"assessment_id\":\"1\",\"assessment_name\":\"fintech\"}", NotificationStatus.Y, 0, new Date(), new Date());

        when(notificationRepository.update(notification)).thenReturn(notification1);

        notificationService.update(notification);

        Assertions.assertEquals(NotificationStatus.Y, notification1.getStatus());
    }

    @Test
    void shouldSetNotificationWhenAnUserIsAdded() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setOrganisation(organisation);

        AssessmentUsers assessmentUser = new AssessmentUsers();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUsers assessmentUser1 = new AssessmentUsers();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUsers> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        Set<String> users = new HashSet<>();
        users.add(assessmentUser1.getUserId().getUserEmail());

        Notification notification = new Notification(1, NotificationType.ADD_USER_V1, email, "", NotificationStatus.N, 0, new Date(), new Date());
        when(notificationRepository.save(notification)).thenReturn(notification);


        notificationService.setNotificationForAddUser(assessment, users);
        notificationRepository.save(notification);

        verify(notificationRepository).save(notification);
    }
}

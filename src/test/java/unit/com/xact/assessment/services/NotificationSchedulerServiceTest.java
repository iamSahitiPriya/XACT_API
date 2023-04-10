/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.config.FeedbackNotificationConfig;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.NotificationSchedulerService;
import com.xact.assessment.services.NotificationService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class NotificationSchedulerServiceTest {
    private final AssessmentService assessmentService;
    private final NotificationService notificationService;
    private final NotificationSchedulerService notificationSchedulerService;
    private final FeedbackNotificationConfig feedbackNotificationConfig;

    public NotificationSchedulerServiceTest( ){
        feedbackNotificationConfig = mock(FeedbackNotificationConfig.class);
        assessmentService = mock(AssessmentService.class);
        notificationService = mock(NotificationService.class);
        notificationSchedulerService = new NotificationSchedulerService(notificationService, assessmentService, this.feedbackNotificationConfig);


    }

    @Test
    void shouldSaveNotificationForFeedback() {
        String email = "abc@thoughtworks.com";
        Organisation organisation = new Organisation(1, "abC", "ABC", "abc", 6);
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);
        assessment.setAssessmentName("Assessment");
        assessment.setCreatedAt(new Date());
        assessment.setUpdatedAt(new Date(22 - 11 - 2022));
        assessment.setOrganisation(organisation);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId(email, assessment);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Facilitator);
        AssessmentUser assessmentUser1 = new AssessmentUser();
        UserId userId1 = new UserId(email, assessment);
        assessmentUser1.setUserId(userId1);
        assessmentUser1.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUsers = new HashSet<>();
        assessmentUsers.add(assessmentUser);
        assessmentUsers.add(assessmentUser1);
        assessment.setAssessmentUsers(assessmentUsers);
        String payload = "{\"assessment_name\":\"New Assessment\",\"created_at\":\"27-Jan-2023 06:33 pm IST\",\"assessment_id\":\"2\",\"organisation_name\":\"EQ International\"}";
        Notification notification = new Notification(1, NotificationType.FEEDBACK_V1, email, payload, NotificationStatus.N, 0, new Date(22 - 10 - 2022), new Date(22 - 10 - 2022));

        when(assessmentService.getFinishedAssessments()).thenReturn(Collections.singletonList(assessment));
        when(notificationService.findByType(NotificationType.FEEDBACK_V1)).thenReturn(Collections.singletonList(notification));

        notificationSchedulerService.saveFeedbackNotificationForFinishedAssessments();

        verify(notificationService).saveFeedbackNotificationForFinishedAssessments(assessment, Collections.singletonList(notification));
    }

}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services.scheduler;

import com.xact.assessment.services.ActivityLogService;
import com.xact.assessment.services.NotificationService;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.schedulers.CleanupSchedulerService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CleanupSchedulerServiceTest {

    NotificationService notificationService = mock(NotificationService.class);
    ActivityLogService activityLogService = mock(ActivityLogService.class);
    QuestionService questionService = mock(QuestionService.class);
    CleanupSchedulerService cleanupSchedulerService;

    public CleanupSchedulerServiceTest() {
       this.cleanupSchedulerService = new CleanupSchedulerService(questionService, notificationService, activityLogService);
    }

    @Test
    void shouldDeleteSentNotifications() {
        cleanupSchedulerService.cleanSentNotifications();

        verify(notificationService).deleteSentNotifications(any(Date.class));
    }

    @Test
    void shouldDeleteExpiredActivityLogs() {
        cleanupSchedulerService.cleanExpiredActivityLogs();

        verify(activityLogService).deleteActivityLogs(any(Date.class));
    }

    @Test
    void shouldDeleteRejectedQuestions() {
        cleanupSchedulerService.cleanRejectedQuestions();

        verify(questionService).deleteRejectedQuestions(any(Date.class));
    }
}

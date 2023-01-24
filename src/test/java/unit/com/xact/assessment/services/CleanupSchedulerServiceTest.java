package unit.com.xact.assessment.services;

import com.xact.assessment.repositories.ActivityLogRepository;
import com.xact.assessment.repositories.NotificationRepository;
import com.xact.assessment.services.CleanupSchedulerService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CleanupSchedulerServiceTest {

    NotificationRepository notificationRepository = mock(NotificationRepository.class);
    ActivityLogRepository activityLogRepository = mock(ActivityLogRepository.class);
    CleanupSchedulerService cleanupSchedulerService;

    public CleanupSchedulerServiceTest() {
       this.cleanupSchedulerService = new CleanupSchedulerService(notificationRepository, activityLogRepository);
    }

    @Test
    void shouldDeleteSentNotifications() {
        cleanupSchedulerService.cleanSentNotifications();

        verify(notificationRepository).deleteSentNotifications(any(Date.class),any(Date.class));
    }

    @Test
    void shouldDeleteExpiredActivityLogs() {
        cleanupSchedulerService.cleanExpiredActivityLogs();

        verify(activityLogRepository).deleteActivityLogs(any(Date.class),any(Date.class));
    }
}

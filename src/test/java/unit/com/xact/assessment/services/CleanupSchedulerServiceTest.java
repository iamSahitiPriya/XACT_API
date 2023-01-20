package unit.com.xact.assessment.services;

import com.xact.assessment.repositories.NotificationRepository;
import com.xact.assessment.services.CleanupSchedulerService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class CleanupSchedulerServiceTest {

    NotificationRepository notificationRepository = mock(NotificationRepository.class);
    CleanupSchedulerService cleanupSchedulerService;

    public CleanupSchedulerServiceTest() {
       this.cleanupSchedulerService = new CleanupSchedulerService(notificationRepository);
    }

    @Test
    void shouldDeleteSentNotifications() {
        cleanupSchedulerService.cleanSentNotifications();

        verify(notificationRepository).deleteSentNotifications(any(Date.class),any(Date.class));
    }
}

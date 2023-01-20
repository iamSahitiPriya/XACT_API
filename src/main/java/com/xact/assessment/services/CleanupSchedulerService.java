package com.xact.assessment.services;

import com.xact.assessment.repositories.NotificationRepository;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Singleton
public class CleanupSchedulerService {

    public static final int DELAY = 60;
    private final NotificationRepository notificationRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupSchedulerService.class);


    public CleanupSchedulerService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(fixedDelay = "${cleanup.notification.fixedDelay}")
    public void cleanSentNotifications() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -DELAY);
        Date startDate = calendar.getTime();
        Date endDate = new Date();
        LOGGER.info("Cleaning up the sent notifications");
        notificationRepository.deleteSentNotifications(startDate, endDate);
    }
}

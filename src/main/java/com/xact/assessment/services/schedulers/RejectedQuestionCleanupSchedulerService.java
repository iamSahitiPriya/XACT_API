/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services.schedulers;

import com.xact.assessment.services.QuestionService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

@Singleton
public class RejectedQuestionCleanupSchedulerService {
    private static final int DELETION_DELAY = 10;
    private final QuestionService questionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RejectedQuestionCleanupSchedulerService.class);

    public RejectedQuestionCleanupSchedulerService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Scheduled(fixedDelay = "${cleanup.rejectedQuestion.fixedDelay}", initialDelay = "${cleanup.initialDelay}")
    public void cleanRejectedQuestions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - DELETION_DELAY);
        Date expiryDate = calendar.getTime();
        LOGGER.info("Cleaning up the rejected questions");
        questionService.deleteRejectedQuestions(expiryDate);
    }
}

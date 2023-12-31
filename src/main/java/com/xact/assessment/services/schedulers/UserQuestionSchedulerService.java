/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services.schedulers;

import com.xact.assessment.models.UserQuestion;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserQuestionService;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class UserQuestionSchedulerService {
    private final UserQuestionService userQuestionService;

    private final QuestionService questionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserQuestionSchedulerService.class);



    public UserQuestionSchedulerService(UserQuestionService userQuestionService, QuestionService questionService) {
        this.userQuestionService = userQuestionService;
        this.questionService = questionService;
    }

    @Scheduled(fixedDelay = "${userQuestion.fixedDelay}")
    public void saveUserQuestionForCompletedAssessment(){
        LOGGER.info("Saving user questions in master table...");
        List<UserQuestion> userQuestionList = userQuestionService.getUserQuestionsOfFinishedAssessment();
        questionService.save(userQuestionList);
    }
}

/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services.scheduler;

import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.schedulers.RejectedQuestionCleanupSchedulerService;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RejectedQuestionsCleanupSchedulerServiceTest {

    QuestionService questionService = mock(QuestionService.class);
    RejectedQuestionCleanupSchedulerService rejectedQuestionsCleanupSchedulerService;

    public RejectedQuestionsCleanupSchedulerServiceTest() {
        this.rejectedQuestionsCleanupSchedulerService = new RejectedQuestionCleanupSchedulerService(questionService);
    }

    @Test
    void shouldDeleteRejectedQuestions() {
        rejectedQuestionsCleanupSchedulerService.cleanRejectedQuestions();

        verify(questionService).deleteRejectedQuestions(any(Date.class));
    }
}

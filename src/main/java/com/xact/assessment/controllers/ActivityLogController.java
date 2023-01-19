/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.services.ActivityLogService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.sse.Event;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@Controller("/v1")
@Singleton
public class ActivityLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogController.class);
    public static final int PERIOD = 5    ;
    public static final int DELAY = 1;
    private final ActivityLogService activityLogService;

    @Get(value = "/assessment/{assessmentId}/topic/{topicId}/activity", produces = MediaType.TEXT_EVENT_STREAM)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Flux<Event<List<ActivityResponse>>> getActivityLogs(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, Authentication authentication){
        LOGGER.info("Fetching Activity for assessment:{} and topic{}",assessmentId, topicId);
        Assessment assessment = activityLogService.getAssessment(assessmentId);
        AssessmentTopic assessmentTopic = activityLogService.getTopic(topicId);
        return Flux.interval(Duration.ofSeconds(DELAY), Duration.ofSeconds(PERIOD)).map(sequence -> Event.of(activityLogService.getLatestActivityRecords(assessment,assessmentTopic,authentication)));
    }


}

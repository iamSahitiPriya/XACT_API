/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;

import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.services.ActivityLogService;
import com.xact.assessment.services.TopicService;
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
@Controller("/v1/activity")
@Singleton
public class ActivityLogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogController.class);
    private final ActivityLogService activityLogService;
    private final TopicService topicService;

    @Get(value = "/{assessmentId}/{topicId}", produces = MediaType.TEXT_EVENT_STREAM)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public Flux<Event<List<ActivityResponse>>> getActivityLogs(@PathVariable("assessmentId") Integer assessmentId, @PathVariable("topicId") Integer topicId, Authentication authentication){
        Assessment assessment = activityLogService.getAssessment(assessmentId);
        AssessmentTopic assessmentTopic = activityLogService.getTopic(topicId);
        return Flux.interval(Duration.ofSeconds(1), Duration.ofSeconds(10)).map(sequence ->{
            List<ActivityResponse> activityLogResponse=activityLogService.getLatestActivityRecords(assessment,assessmentTopic);
            return  Event.of(activityLogResponse);});
    }


}

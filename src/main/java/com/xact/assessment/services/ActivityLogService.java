/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.models.ActivityId;
import com.xact.assessment.models.ActivityLog;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.ActivityLogRepository;
import io.micronaut.data.exceptions.EmptyResultException;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Singleton
public class ActivityLogService {
    public static final int EXPIRY_TIME = 30000;
    private AssessmentService assessmentService;
    private ActivityLogRepository activityLogRepository;
    private AnswerService answerService;
    private UserQuestionService userQuestionService;
    private UserAuthService userAuthService;
    private TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private TopicService topicService;
    private static final ModelMapper mapper = new ModelMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityLogService.class);


    public Assessment getAssessment(Integer assessmentId) {
        return assessmentService.getAssessmentById(assessmentId);
    }

    public AssessmentTopic getTopic(Integer topicId) {
        return topicService.getTopicById(topicId);
    }

    public List<ActivityResponse> getLatestActivityRecords(Assessment assessment, AssessmentTopic assessmentTopic, Authentication authentication) {
        String loggedInUser = userAuthService.getLoggedInUser(authentication).getUserEmail();
        List<ActivityLog> activityLogs = new ArrayList<>();
        Date timeStart = new Date();
        timeStart.setTime(timeStart.getTime() - EXPIRY_TIME);
        Date timeEnd = new Date();
        try {
             activityLogs = activityLogRepository.getLatestRecords(timeStart, timeEnd, assessment, assessmentTopic, loggedInUser);
        }catch (EmptyResultException e) {
            LOGGER.error("No activity found for this assessment {}",assessment.getAssessmentId());
        }
        return getActivityResponses(activityLogs);
    }

    private List<ActivityResponse> getActivityResponses(List<ActivityLog> activityLogs) {
        List<ActivityResponse> activityResponses = new ArrayList<>();
        for (ActivityLog activityLog: activityLogs) {
            ActivityResponse activityResponse = mapper.map(activityLog, ActivityResponse.class);
            activityResponse.setUserName(activityLog.getActivityId().getUserName());
            activityResponse.setInputText(setInputText(activityLog));
            activityResponses.add(activityResponse);
        }
        return activityResponses;
    }

    private String setInputText(ActivityLog activityLog) {
        return switch (activityLog.getActivityType()) {
            case DEFAULT_QUESTION->
                    answerService.getAnswerByQuestionId(activityLog.getActivityId().getAssessment().getAssessmentId(), activityLog.getIdentifier());
            case ADDITIONAL_QUESTION->
                    userQuestionService.getAnswerByQuestionId(activityLog.getActivityId().getAssessment().getAssessmentId(), activityLog.getIdentifier());
            case TOPIC_RECOMMENDATION, PARAMETER_RECOMMENDATION ->
                    topicAndParameterLevelAssessmentService.getRecommendationById(activityLog.getIdentifier(), activityLog.getActivityType());
        };
    }

    public ActivityLog saveActivityLog(Assessment assessment, Authentication authentication, Integer identifier, AssessmentTopic assessmentTopic, ActivityType type) {
        String loggedInUser = userAuthService.getLoggedInUser(authentication).getUserEmail();
        ActivityId activityId  = new ActivityId(assessment,loggedInUser);
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityType(type);
        activityLog.setActivityId(activityId);
        activityLog.setTopic(assessmentTopic);
        activityLog.setIdentifier(identifier);
        if(activityLogRepository.existsById(activityId)){
            return updateActivityLog(activityLog);
        }
        return saveActivityLog(activityLog);
    }

    private ActivityLog updateActivityLog(ActivityLog activityLog) {
        LOGGER.info("Update Activity log for assessment:{}",activityLog.getActivityId().getAssessment().getAssessmentId());
        return activityLogRepository.update(activityLog);
    }

    private ActivityLog saveActivityLog(ActivityLog activityLog) {
        LOGGER.info("Save Activity log for assessment: {}", activityLog.getActivityId().getAssessment().getAssessmentId());
        return activityLogRepository.save(activityLog);
    }
}

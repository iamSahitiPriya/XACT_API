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
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

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
    private static final ModelMapper mapper = new ModelMapper();

    private TopicService topicService;
    public Assessment getAssessment(Integer assessmentId) {
        return assessmentService.getAssessmentById(assessmentId);
    }

    public AssessmentTopic getTopic(Integer topicId) {
        return topicService.getTopicById(topicId);
    }

    public List<ActivityResponse> getLatestActivityRecords(Assessment assessment, AssessmentTopic assessmentTopic) {
        List<ActivityResponse> activityResponses = new ArrayList<>();
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime() - EXPIRY_TIME);
        List<ActivityLog> activityLogs = activityLogRepository.getLatestRecords(currentDate,new Date(), assessment,assessmentTopic);
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
            case DEFAULT ->
                    answerService.getAnswerByQuestionId(activityLog.getActivityId().getAssessment().getAssessmentId(), activityLog.getIdentifier());
            case ADDITIONAL ->
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
        return activityLogRepository.update(activityLog);
    }

    private ActivityLog saveActivityLog(ActivityLog activityLog) {
        return activityLogRepository.save(activityLog);
    }
}

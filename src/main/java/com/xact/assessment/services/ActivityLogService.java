/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ActivityResponse;
import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ActivityLogRepository;
import io.micronaut.data.exceptions.EmptyResultException;
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
    public static final String SPACE_DELIMITER = " ";
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

    public List<ActivityResponse> getLatestActivityRecords(Assessment assessment, AssessmentTopic assessmentTopic, User user) {
        String loggedInUser = user.getUserEmail();
        List<ActivityLog> activityLogs = new ArrayList<>();
        Date startTime = new Date();
        startTime.setTime(startTime.getTime() - EXPIRY_TIME);
        Date endTime = new Date();
        try {
             activityLogs = activityLogRepository.getLatestRecords(startTime, endTime, assessment, assessmentTopic, loggedInUser);
        }catch (EmptyResultException e) {
            LOGGER.error("No activity found for this assessment {} and topic {}",assessment.getAssessmentId(), assessmentTopic.getTopicId());
        }
        return getActivityResponses(activityLogs);
    }

    private List<ActivityResponse> getActivityResponses(List<ActivityLog> activityLogs) {
        List<ActivityResponse> activityResponses = new ArrayList<>();
        for (ActivityLog activityLog: activityLogs) {
            ActivityResponse activityResponse = mapper.map(activityLog, ActivityResponse.class);
            UserInfo userInfo = userAuthService.getUserInfo(activityLog.getActivityId().getUserName());
            activityResponse.setEmail(userInfo.getEmail());
            activityResponse.setFullName(String.join(SPACE_DELIMITER,userInfo.getFirstName(),userInfo.getLastName()));
            activityResponse.setInputText(getInputText(activityLog));
            activityResponses.add(activityResponse);
        }
        return activityResponses;
    }

    private String getInputText(ActivityLog activityLog) {
        return switch (activityLog.getActivityType()) {
            case DEFAULT_QUESTION->
                    answerService.getAnswerByQuestionId(activityLog.getActivityId().getAssessment().getAssessmentId(), activityLog.getIdentifier());
            case ADDITIONAL_QUESTION->
                    userQuestionService.getAnswerByQuestionId(activityLog.getIdentifier());
            case TOPIC_RECOMMENDATION, PARAMETER_RECOMMENDATION ->
                    topicAndParameterLevelAssessmentService.getRecommendationById(activityLog.getIdentifier(), activityLog.getActivityType());
        };
    }

    public ActivityLog saveActivityLog(Assessment assessment, User user, Integer identifier, AssessmentTopic assessmentTopic, ActivityType type) {
        String loggedInUser = user.getUserEmail();
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

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ActivityType;
import com.xact.assessment.dtos.RecommendationRequest;
import com.xact.assessment.dtos.UpdateAnswerRequest;
import com.xact.assessment.models.*;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicAndParameterLevelAssessmentService {

    private final ParameterService parameterService;
    private final AnswerService answerService;
    private final TopicService topicService;
    private final QuestionService questionService;


    public TopicAndParameterLevelAssessmentService(ParameterService parameterService, AnswerService answerService, TopicService topicService, QuestionService questionService) {
        this.parameterService = parameterService;
        this.answerService = answerService;
        this.topicService = topicService;
        this.questionService = questionService;
    }

    public TopicLevelRating saveTopicRating(TopicLevelRating topicLevelRating) {
        return topicService.saveTopicRating(topicLevelRating);
    }


    public ParameterLevelRating saveParameterRating(ParameterLevelRating parameterLevelRating) {
        return parameterService.saveParameterRating(parameterLevelRating);
    }


    public List<ParameterLevelRating> getParameterRatings(Integer assessmentId) {
        return parameterService.getParameterLevelRatings(assessmentId);
    }

    public List<TopicLevelRating> getTopicRatings(Integer assessmentId) {
        return topicService.getTopicLevelRatings(assessmentId);
    }


    public Optional<ParameterLevelRating> searchParameterRating(ParameterLevelId parameterLevelId) {
        return parameterService.searchParameterRating(parameterLevelId);
    }


    public Optional<TopicLevelRating> searchTopicRating(TopicLevelId topicLevelId) {
        return topicService.searchTopicRating(topicLevelId);
    }

    public Optional<TopicLevelRecommendation> searchTopicRecommendation(Integer recommendationId) {
        return topicService.searchTopicRecommendation(recommendationId);
    }


    public void deleteTopicRecommendation(Integer recommendationId) {
        topicService.deleteTopicRecommendation(recommendationId);
    }


    public void deleteParameterRecommendation(Integer recommendationId) {
        parameterService.deleteParameterRecommendation(recommendationId);
    }

    public Optional<AssessmentParameter> getParameter(Integer parameterId) {
        return parameterService.getParameter(parameterId);
    }

    public AssessmentTopic getTopicByQuestionId(Integer questionId) {
        return questionService.getTopicByQuestionId(questionId);
    }

    public void saveAnswer(UpdateAnswerRequest answerRequest, Assessment assessment) {
        answerService.saveAnswer(answerRequest, assessment);
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return topicService.getTopic(topicId);
    }

    public List<Answer> getAnswers(Integer assessmentId) {
        return answerService.getAnswers(assessmentId);
    }


    public String getRecommendationById(Integer identifier, ActivityType activityType) {
        if (activityType.equals(ActivityType.TOPIC_RECOMMENDATION)) {
            return topicService.getTopicRecommendationById(identifier);
        } else {
            return parameterService.getParameterRecommendationById(identifier);
        }
    }

    public List<TopicLevelRecommendation> getTopicRecommendations(Integer assessmentId) {
        return topicService.getTopicRecommendations(assessmentId);
    }


    public List<ParameterLevelRecommendation> getParameterRecommendations(Integer assessmentId) {
        return parameterService.getParameterRecommendations(assessmentId);
    }

    public TopicLevelRecommendation updateTopicRecommendation(RecommendationRequest recommendationRequest) {
        return topicService.updateTopicRecommendation(recommendationRequest);
    }

    public TopicLevelRecommendation saveTopicRecommendation(RecommendationRequest recommendationRequest, Assessment assessment, Integer topicId) {
        return topicService.saveTopicRecommendation(recommendationRequest, assessment, topicId);

    }

    public ParameterLevelRecommendation updateParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest) {
        return parameterService.updateParameterRecommendation(parameterLevelRecommendationRequest);
    }

    public ParameterLevelRecommendation saveParameterRecommendation(RecommendationRequest parameterLevelRecommendationRequest, Assessment assessment, Integer parameterId) {
        return parameterService.saveParameterRecommendation(parameterLevelRecommendationRequest, assessment, parameterId);
    }

}

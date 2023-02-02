/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.ActivityType;
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

    public TopicLevelAssessment saveRatingAndRecommendation(TopicLevelAssessment topicLevelAssessment) {
        return topicService.saveRatingAndRecommendation(topicLevelAssessment);
    }


    public ParameterLevelAssessment saveRatingAndRecommendation(ParameterLevelAssessment parameterLevelAssessment) {
        return parameterService.saveRatingAndRecommendation(parameterLevelAssessment);
    }

    public TopicLevelRecommendation saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        return topicService.saveTopicLevelRecommendation(topicLevelRecommendation);
    }


    public ParameterLevelRecommendation saveParameterLevelRecommendation(ParameterLevelRecommendation parameterLevelRecommendation) {
        return parameterService.saveParameterLevelRecommendation(parameterLevelRecommendation);
    }

    public List<ParameterLevelAssessment> getParameterAssessmentData(Integer assessmentId) {
        return parameterService.getParameterAssessmentData(assessmentId);
    }

    public List<TopicLevelAssessment> getTopicAssessmentData(Integer assessmentId) {
        return topicService.getTopicAssessmentData(assessmentId);
    }

    public List<TopicLevelRecommendation> getTopicAssessmentRecommendationData(Integer assessmentId, Integer topicId) {
        return topicService.getTopicAssessmentRecommendationData(assessmentId, topicId);
    }

    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterService.searchParameter(parameterLevelId);
    }


    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicService.searchTopic(topicLevelId);
    }

    public Optional<TopicLevelRecommendation> searchTopicRecommendation(Integer recommendationId) {
        return topicService.searchTopicRecommendation(recommendationId);
    }


    public void deleteRecommendation(Integer recommendationId) {
        topicService.deleteRecommendation(recommendationId);
    }


    public boolean checkTopicRecommendationId(Integer recommendationId) {
        return topicService.checkTopicRecommendationId(recommendationId);
    }


    public List<TopicLevelRecommendation> getAssessmentTopicRecommendationData(Integer assessmentId) {
        return topicService.getAssessmentTopicRecommendationData(assessmentId);
    }


    public List<ParameterLevelRecommendation> getAssessmentParameterRecommendationData(Integer assessmentId) {
        return parameterService.getAssessmentParameterRecommendationData(assessmentId);
    }

    public List<ParameterLevelRecommendation> getParameterAssessmentRecommendationData(Integer assessmentId, Integer parameterId) {
        return parameterService.getParameterAssessmentRecommendationData(assessmentId, parameterId);
    }

    public Optional<ParameterLevelRecommendation> searchParameterRecommendation(Integer recommendationId) {
        return parameterService.searchParameterRecommendation(recommendationId);
    }

    public boolean checkParameterRecommendationId(Integer recommendationId) {
        return parameterService.checkParameterRecommendationId(recommendationId);
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
        if(activityType.equals(ActivityType.TOPIC_RECOMMENDATION)){
            return topicService.getTopicRecommendationById(identifier);
        }else{
            return parameterService.getParameterRecommendationById(identifier);
        }
    }

}

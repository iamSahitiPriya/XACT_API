package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelRecommendationRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TopicAndParameterLevelAssessmentService {

    private final TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private final ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private final TopicLevelRecommendationRepository topicLevelRecommendationRepository;
    private final AnswerService answerService;

    public TopicAndParameterLevelAssessmentService(TopicLevelAssessmentRepository topicLevelAssessmentRepository, ParameterLevelAssessmentRepository parameterLevelAssessmentRepository, TopicLevelRecommendationRepository topicLevelRecommendationRepository, AnswerService answerService) {
        this.topicLevelAssessmentRepository = topicLevelAssessmentRepository;
        this.parameterLevelAssessmentRepository = parameterLevelAssessmentRepository;
        this.topicLevelRecommendationRepository = topicLevelRecommendationRepository;
        this.answerService = answerService;
    }

    public TopicLevelAssessment saveRatingAndRecommendation(TopicLevelAssessment topicLevelAssessment) {

        if (topicLevelAssessmentRepository.existsById(topicLevelAssessment.getTopicLevelId())) {
            if (topicLevelAssessment.getRating() == null) {
                topicLevelAssessmentRepository.delete(topicLevelAssessment);
            } else {
                topicLevelAssessmentRepository.update(topicLevelAssessment);
            }
        } else {
            if (topicLevelAssessment.getRating() != null)
                topicLevelAssessmentRepository.save(topicLevelAssessment);
        }
        return topicLevelAssessment;
    }


    public ParameterLevelAssessment saveRatingAndRecommendation(ParameterLevelAssessment parameterLevelAssessment) {

        if (parameterLevelAssessmentRepository.existsById(parameterLevelAssessment.getParameterLevelId())) {
            if (parameterLevelAssessment.getRating() == null) {
                parameterLevelAssessmentRepository.delete(parameterLevelAssessment);
            } else {
                parameterLevelAssessmentRepository.update(parameterLevelAssessment);
            }
        } else {
            if (parameterLevelAssessment.getRating() != null) {
                parameterLevelAssessmentRepository.save(parameterLevelAssessment);
            }
        }
        return parameterLevelAssessment;
    }

    @Transactional
    public void saveTopicLevelAssessment(TopicLevelAssessment topicLevelAssessment,List<TopicLevelRecommendation> topicLevelRecommendationList, List<Answer> answerList) {
        saveRatingAndRecommendation(topicLevelAssessment);
        for(TopicLevelRecommendation topicLevelRecommendation :topicLevelRecommendationList){
                    saveTopicLevelRecommendation(topicLevelRecommendation);
        }
        for (Answer answer : answerList) {
            answerService.saveAnswer(answer);
        }
    }

    public TopicLevelRecommendation saveTopicLevelRecommendation(TopicLevelRecommendation topicLevelRecommendation) {
        if (topicLevelRecommendation.getRecommendationId()!=null) {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationRepository.update(topicLevelRecommendation);
            }
           else{
                topicLevelRecommendationRepository.delete(topicLevelRecommendation);
            }
        } else {
            if (topicLevelRecommendation.hasRecommendation()) {
                topicLevelRecommendationRepository.save(topicLevelRecommendation);
            }
        }
       return topicLevelRecommendation;
    }

    public void saveParameterLevelAssessment(List<ParameterLevelAssessment> parameterLevelAssessmentList, List<Answer> answerList) {
        for (ParameterLevelAssessment parameterLevelAssessment : parameterLevelAssessmentList) {
            saveRatingAndRecommendation(parameterLevelAssessment);
        }
        for (Answer answer : answerList) {
            answerService.saveAnswer(answer);
        }
    }

    public List<ParameterLevelAssessment> getParameterAssessmentData(Integer assessmentId) {
        return parameterLevelAssessmentRepository.findByAssessment(assessmentId);
    }

    public List<TopicLevelAssessment> getTopicAssessmentData(Integer assessmentId) {
        return topicLevelAssessmentRepository.findByAssessment(assessmentId);
    }

    public List<TopicLevelRecommendation> getTopicAssessmentRecommendationData(Integer assessmentId,Integer topicId) {
        return  topicLevelRecommendationRepository.findByAssessmentAndTopic(assessmentId,topicId);
    }

    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterLevelAssessmentRepository.findById(parameterLevelId);
    }


    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelAssessmentRepository.findById(topicLevelId);
    }

    public Optional<TopicLevelRecommendation> searchRecommendation(Integer recommendationId) {
        return topicLevelRecommendationRepository.findById(recommendationId);
    }
}

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
//        for(TopicLevelRecommendation topicLevelRecommendation :topicLevelRecommendationList){
//                    saveTopicLevelRecommendation(topicLevelRecommendation);
//        }
        saveTopicLevelRecommendation(topicLevelRecommendationList);
        for (Answer answer : answerList) {
            answerService.saveAnswer(answer);
        }
    }

    public void saveTopicLevelRecommendation(List<TopicLevelRecommendation> topicLevelRecommendation) {
        List<TopicLevelRecommendation> topicLevelRecommendations=new ArrayList<>();
        for(TopicLevelRecommendation topicLevelRecommendation1: topicLevelRecommendation){
           topicLevelRecommendations=topicLevelRecommendationRepository.findByAssessmentAndTopic(topicLevelRecommendation1.getAssessment().getAssessmentId(),topicLevelRecommendation1.getTopic().getTopicId());
        }
        if ((topicLevelRecommendations.size()!=0)) {
            topicLevelRecommendationRepository.deleteAll(topicLevelRecommendations);
        }
        for(TopicLevelRecommendation topicLevelRecommendation1: topicLevelRecommendation)
            if (topicLevelRecommendation1.hasRecommendation()) {

                topicLevelRecommendationRepository.save(topicLevelRecommendation1);
            }
//            else {
//                topicLevelRecommendationRepository.delete(topicLevelRecommendation);
//            }

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

    public Optional<ParameterLevelAssessment> searchParameter(ParameterLevelId parameterLevelId) {
        return parameterLevelAssessmentRepository.findById(parameterLevelId);
    }


    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelAssessmentRepository.findById(topicLevelId);
    }
}

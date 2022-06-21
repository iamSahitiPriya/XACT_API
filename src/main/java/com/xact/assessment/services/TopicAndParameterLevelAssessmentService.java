package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.ParameterLevelAssessmentRepository;
import com.xact.assessment.repositories.TopicLevelAssessmentRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
@Transactional
public class TopicAndParameterLevelAssessmentService {

    private final TopicLevelAssessmentRepository topicLevelAssessmentRepository;
    private final ParameterLevelAssessmentRepository parameterLevelAssessmentRepository;
    private final AnswerService answerService;

    public TopicAndParameterLevelAssessmentService(TopicLevelAssessmentRepository topicLevelAssessmentRepository, ParameterLevelAssessmentRepository parameterLevelAssessmentRepository, AnswerService answerService) {
        this.topicLevelAssessmentRepository = topicLevelAssessmentRepository;
        this.parameterLevelAssessmentRepository = parameterLevelAssessmentRepository;
        this.answerService = answerService;
    }

    public TopicLevelAssessment saveRatingAndRecommendation(TopicLevelAssessment topicLevelAssessment) {

        if (topicLevelAssessmentRepository.existsById(topicLevelAssessment.getTopicLevelId())) {
            topicLevelAssessmentRepository.update(topicLevelAssessment);
        } else {
            topicLevelAssessmentRepository.save(topicLevelAssessment);
        }
        return topicLevelAssessment;
    }


    public ParameterLevelAssessment saveRatingAndRecommendation(ParameterLevelAssessment parameterLevelAssessment) {

        if (parameterLevelAssessmentRepository.existsById(parameterLevelAssessment.getParameterLevelId())) {
            parameterLevelAssessmentRepository.update(parameterLevelAssessment);
        } else {
            parameterLevelAssessmentRepository.save(parameterLevelAssessment);
        }
        return parameterLevelAssessment;
    }

    public void saveTopicLevelAssessment(TopicLevelAssessment topicLevelAssessment, List<Answer> answerList){
        saveRatingAndRecommendation(topicLevelAssessment);
        for (Answer answer: answerList) {
            answerService.saveAnswer(answer);
        }
    }

    public void saveParameterLevelAssessment(List<ParameterLevelAssessment> parameterLevelAssessmentList, List<Answer> answerList){
        for (ParameterLevelAssessment parameterLevelAssessment: parameterLevelAssessmentList) {
            saveRatingAndRecommendation(parameterLevelAssessment);
        }
        for (Answer answer: answerList) {
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
        return  parameterLevelAssessmentRepository.findById(parameterLevelId);
    }


    public Optional<TopicLevelAssessment> searchTopic(TopicLevelId topicLevelId) {
        return topicLevelAssessmentRepository.findById(topicLevelId);
    }
}

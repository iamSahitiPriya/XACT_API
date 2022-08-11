package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton

public class TopicService {
    private final AssessmentTopicRepository assessmentTopicRepository;

    public TopicService(AssessmentTopicRepository assessmentTopicRepository) {
        this.assessmentTopicRepository = assessmentTopicRepository;
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return assessmentTopicRepository.findById(topicId);
    }
    public void createTopic(AssessmentTopic topic){
        assessmentTopicRepository.save(topic);
    }
    public void updateTopic(AssessmentTopic topic){
        assessmentTopicRepository.update(topic);
    }
}

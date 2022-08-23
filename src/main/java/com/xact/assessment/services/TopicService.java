package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import jakarta.inject.Singleton;

<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
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
<<<<<<< HEAD
=======
    public void createTopic(AssessmentTopic topic){
        assessmentTopicRepository.save(topic);
    }
    public void updateTopic(AssessmentTopic topic){
        assessmentTopicRepository.update(topic);
    }

    public List<AssessmentTopic> getAllTopics() {
        return (List<AssessmentTopic>) assessmentTopicRepository.findAll();
    }
>>>>>>> b93eb638144e893f11f41aa65c0cc5a13e1ab148
}

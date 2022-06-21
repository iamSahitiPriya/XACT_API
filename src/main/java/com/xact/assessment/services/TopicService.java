package com.xact.assessment.services;

import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentTopic;
import com.xact.assessment.repositories.AssessmentParameterRepository;
import com.xact.assessment.repositories.AssessmentTopicRepository;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;
import java.util.Optional;

@Singleton
@Transactional
public class TopicService {
    private final AssessmentTopicRepository assessmentTopicRepository;

    public TopicService(AssessmentTopicRepository assessmentTopicRepository) {
        this.assessmentTopicRepository = assessmentTopicRepository;
    }

    public Optional<AssessmentTopic> getTopic(Integer topicId) {
        return  assessmentTopicRepository.findById(topicId);
    }
}

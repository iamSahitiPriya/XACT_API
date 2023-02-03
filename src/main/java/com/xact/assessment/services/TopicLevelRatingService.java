package com.xact.assessment.services;

import com.xact.assessment.models.TopicLevelId;
import com.xact.assessment.models.TopicLevelRating;
import com.xact.assessment.repositories.TopicLevelRatingRepository;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class TopicLevelRatingService {
    private final TopicLevelRatingRepository topicLevelRatingRepository;

    public TopicLevelRatingService(TopicLevelRatingRepository topicLevelRatingRepository) {
        this.topicLevelRatingRepository = topicLevelRatingRepository;
    }

    public TopicLevelRating update(TopicLevelRating topicLevelRating) {
        return topicLevelRatingRepository.update(topicLevelRating);
    }

    public TopicLevelRating save(TopicLevelRating topicLevelRating) {
        return topicLevelRatingRepository.save(topicLevelRating);
    }

    public void delete(TopicLevelRating topicLevelRating) {
        topicLevelRatingRepository.delete(topicLevelRating);
    }

    public boolean existsByID(TopicLevelRating topicLevelRating) {
        return topicLevelRatingRepository.existsById(topicLevelRating.getTopicLevelId());
    }

    public List<TopicLevelRating> findByAssessment(Integer assessmentId) {
        return topicLevelRatingRepository.findByAssessment(assessmentId);
    }

    public Optional<TopicLevelRating> findById(TopicLevelId topicLevelId) {
        return topicLevelRatingRepository.findById(topicLevelId);
    }
}

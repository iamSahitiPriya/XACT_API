/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;

import com.xact.assessment.dtos.TopicRatingAndRecommendation;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.TopicLevelId;
import com.xact.assessment.models.TopicLevelRating;
import com.xact.assessment.repositories.TopicLevelRatingRepository;
import com.xact.assessment.services.TopicLevelRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

class TopicLevelRatingServiceTest {
    private ModelMapper mapper = new ModelMapper();
    private TopicLevelRatingService topicLevelRatingService;
    private TopicLevelRatingRepository topicLevelRatingRepository;


    @BeforeEach
    public void beforeEach() {
        topicLevelRatingRepository = mock(TopicLevelRatingRepository.class);
        topicLevelRatingService = new TopicLevelRatingService(topicLevelRatingRepository);
    }

    @Test
    void shouldSaveTopicLevelRating() {
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessment1.getAssessmentId());

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);

        when(topicLevelRatingRepository.save(topicLevelRating1)).thenReturn(topicLevelRating1);
        topicLevelRatingService.save(topicLevelRating1);

        verify(topicLevelRatingRepository).save(topicLevelRating1);
    }

    @Test
    void shouldDeleteTopicLevelRating() {
        TopicRatingAndRecommendation topicRatingAndRecommendation = new TopicRatingAndRecommendation();
        topicRatingAndRecommendation.setTopicId(1);
        topicRatingAndRecommendation.setRating(1);

        Assessment assessment1 = new Assessment();
        assessment1.setAssessmentId(assessment1.getAssessmentId());

        TopicLevelId topicLevelId1 = mapper.map(topicRatingAndRecommendation, TopicLevelId.class);
        topicLevelId1.setAssessment(assessment1);
        TopicLevelRating topicLevelRating1 = mapper.map(topicRatingAndRecommendation, TopicLevelRating.class);
        topicLevelRating1.setTopicLevelId(topicLevelId1);

        doNothing().when(topicLevelRatingRepository).delete(topicLevelRating1);
        topicLevelRatingService.delete(topicLevelRating1);

        verify(topicLevelRatingRepository).delete(topicLevelRating1);
    }
}

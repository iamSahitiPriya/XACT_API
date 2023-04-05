/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.mappers;

import com.xact.assessment.dtos.Recommendation;
import com.xact.assessment.dtos.RecommendationDeliveryHorizon;
import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.RecommendationImpact;
import com.xact.assessment.mappers.ReportDataMapper;
import com.xact.assessment.models.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportDataMapperTest {

    ReportDataMapper reportDataMapper = new ReportDataMapper();

    @Test
    void testMapReportRecommendationResponseForTopic() {

        TopicLevelRecommendation recommendation = new TopicLevelRecommendation();
        recommendation.setRecommendationId(1);
        recommendation.setRecommendationText("Rec");
        recommendation.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);
        recommendation.setRecommendationEffort(RecommendationEffort.LOW);
        recommendation.setRecommendationImpact(RecommendationImpact.LOW);
        recommendation.setCreatedAt(new Date());
        recommendation.setUpdatedAt(new Date());
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        AssessmentModule module = new AssessmentModule();
        module.setModuleId(1);
        AssessmentCategory category= new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("my category");
        module.setCategory(category);
        topic.setModule(module);
        recommendation.setTopic(topic);


        Recommendation recommendationResponse = reportDataMapper.mapReportRecommendationResponse(recommendation);


        assertEquals(recommendation.getRecommendationId(),recommendationResponse.getRecommendationId());
        assertEquals(recommendation.getRecommendationText(),recommendationResponse.getRecommendation());
        assertEquals(RecommendationDeliveryHorizon.NOW,recommendationResponse.getDeliveryHorizon());
        assertEquals(RecommendationImpact.LOW,recommendationResponse.getImpact());
        assertEquals(RecommendationEffort.LOW,recommendationResponse.getEffort());
        assertEquals(recommendation.getUpdatedAt(),recommendationResponse.getUpdatedAt());
        assertEquals("my category",recommendationResponse.getCategoryName());
        assertEquals(1,recommendationResponse.getCategory());
        assertEquals(1,recommendationResponse.getModule());
        assertEquals(1,recommendationResponse.getTopic());
    }

    @Test
    void testMapReportRecommendationResponseForParameter() {

        ParameterLevelRecommendation recommendation = new ParameterLevelRecommendation();
        recommendation.setRecommendationId(1);
        recommendation.setRecommendationText("Rec");
        recommendation.setDeliveryHorizon(RecommendationDeliveryHorizon.NOW);
        recommendation.setRecommendationEffort(RecommendationEffort.LOW);
        recommendation.setRecommendationImpact(RecommendationImpact.LOW);
        recommendation.setCreatedAt(new Date());
        recommendation.setUpdatedAt(new Date());
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        AssessmentModule module = new AssessmentModule();
        module.setModuleId(1);
        AssessmentCategory category= new AssessmentCategory();
        category.setCategoryId(1);
        category.setCategoryName("my category");
        module.setCategory(category);
        topic.setModule(module);

        AssessmentParameter param = new AssessmentParameter();
        param.setParameterId(1);
        param.setTopic(topic);
        recommendation.setParameter(param);


        Recommendation recommendationResponse = reportDataMapper.mapReportRecommendationResponse(recommendation);


        assertEquals(recommendation.getRecommendationId(),recommendationResponse.getRecommendationId());
        assertEquals(recommendation.getRecommendationText(),recommendationResponse.getRecommendation());
        assertEquals(RecommendationDeliveryHorizon.NOW,recommendationResponse.getDeliveryHorizon());
        assertEquals(RecommendationImpact.LOW,recommendationResponse.getImpact());
        assertEquals(RecommendationEffort.LOW,recommendationResponse.getEffort());
        assertEquals(recommendation.getUpdatedAt(),recommendationResponse.getUpdatedAt());
        assertEquals("my category",recommendationResponse.getCategoryName());
        assertEquals(1,recommendationResponse.getCategory());
        assertEquals(1,recommendationResponse.getModule());
        assertEquals(1,recommendationResponse.getTopic());

    }
}

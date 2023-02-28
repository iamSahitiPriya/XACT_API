/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.mappers;

import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;

import java.util.ArrayList;
import java.util.List;

public class ReportDataMapper {

    public void mapToResponseStructure(List<AssessmentCategory> assessmentCategories, List<ReportCategoryResponse> reportCategoryResponseList) {
        for (AssessmentCategory assessmentCategory : assessmentCategories) {
            ReportCategoryResponse reportCategoryResponse = mapReportCategoryResponse(assessmentCategory);
            List<ReportModuleResponse> reportModuleResponseList = new ArrayList<>();
            for (AssessmentModule assessmentModule : assessmentCategory.getModules()) {
                ReportModuleResponse reportModuleResponse = mapReportModuleResponse(assessmentModule);
                List<ReportTopicResponse> reportTopicResponseList = new ArrayList<>();
                for (AssessmentTopic assessmentTopic : assessmentModule.getActiveTopics()) {
                    ReportTopicResponse reportTopicResponse = mapReportTopicResponse(assessmentTopic);
                    if (assessmentTopic.hasReferences()) {
                        reportTopicResponse.setValue(assessmentTopic.getRating());
                    } else {
                        reportTopicResponse.setRating(assessmentTopic.getTopicAverage());
                        List<ReportParameterResponse> reportParameterResponseList = new ArrayList<>();
                        for (AssessmentParameter assessmentParameter : assessmentTopic.getActiveParameters()) {
                            ReportParameterResponse reportParameterResponse = mapReportParameterResponse(assessmentParameter);
                            reportParameterResponseList.add(reportParameterResponse);
                        }
                        reportTopicResponse.setChildren(reportParameterResponseList);
                    }
                    reportTopicResponseList.add(reportTopicResponse);
                }
                reportModuleResponse.setChildren(reportTopicResponseList);
                reportModuleResponseList.add(reportModuleResponse);
            }
            reportCategoryResponse.setChildren(reportModuleResponseList);
            reportCategoryResponseList.add(reportCategoryResponse);

        }
    }


    private ReportParameterResponse mapReportParameterResponse(AssessmentParameter assessmentParameter) {
        ReportParameterResponse reportParameterResponse = new ReportParameterResponse();
        reportParameterResponse.setName(assessmentParameter.getParameterName());
        reportParameterResponse.setValue(assessmentParameter.getRating());
        return reportParameterResponse;
    }


    private ReportTopicResponse mapReportTopicResponse(AssessmentTopic assessmentTopic) {
        ReportTopicResponse reportTopicResponse = new ReportTopicResponse();
        reportTopicResponse.setName(assessmentTopic.getTopicName());
        return reportTopicResponse;
    }


    private ReportModuleResponse mapReportModuleResponse(AssessmentModule assessmentModule) {
        ReportModuleResponse reportModuleResponse = new ReportModuleResponse();
        reportModuleResponse.setName(assessmentModule.getModuleName());
        reportModuleResponse.setRating(assessmentModule.getModuleAverage());
        return reportModuleResponse;
    }


    private ReportCategoryResponse mapReportCategoryResponse(AssessmentCategory assessmentCategory) {
        ReportCategoryResponse reportCategoryResponse = new ReportCategoryResponse();
        reportCategoryResponse.setName(assessmentCategory.getCategoryName());
        reportCategoryResponse.setRating(assessmentCategory.getCategoryAverage());
        return reportCategoryResponse;
    }

    public Recommendation mapReportRecommendationResponse(TopicLevelRecommendation recommendation) {
        Recommendation recommendationResponse = new Recommendation();
        recommendationResponse.setRecommendationId(recommendation.getRecommendationId());
        recommendationResponse.setRecommendation(recommendation.getRecommendation());
        recommendationResponse.setDeliveryHorizon(recommendation.getDeliveryHorizon());
        recommendationResponse.setImpact(recommendation.getRecommendationImpact());
        recommendationResponse.setEffort(recommendation.getRecommendationEffort());

        recommendationResponse.setTopic(recommendation.getTopic().getTopicId());
        recommendationResponse.setModule(recommendation.getTopic().getModule().getModuleId());
        recommendationResponse.setCategory(recommendation.getTopic().getModule().getCategory().getCategoryId());

        recommendationResponse.setUpdatedAt(recommendation.getUpdatedAt());
        recommendationResponse.setCategoryName(recommendation.getTopic().getModule().getCategory().getCategoryName());
        return recommendationResponse;
    }

    public Recommendation mapReportRecommendationResponse(ParameterLevelRecommendation recommendation) {
        Recommendation recommendationResponse = new Recommendation();
        recommendationResponse.setRecommendationId(recommendation.getRecommendationId());
        recommendationResponse.setRecommendation(recommendation.getRecommendation());
        recommendationResponse.setDeliveryHorizon(recommendation.getDeliveryHorizon());
        recommendationResponse.setImpact(recommendation.getRecommendationImpact());
        recommendationResponse.setEffort(recommendation.getRecommendationEffort());

        recommendationResponse.setTopic(recommendation.getParameter().getTopic().getTopicId());
        recommendationResponse.setModule(recommendation.getParameter().getTopic().getModule().getModuleId());
        recommendationResponse.setCategory(recommendation.getParameter().getTopic().getModule().getCategory().getCategoryId());

        recommendationResponse.setUpdatedAt(recommendation.getUpdatedAt());
        recommendationResponse.setCategoryName(recommendation.getParameter().getTopic().getModule().getCategory().getCategoryName());
        return recommendationResponse;
    }
}

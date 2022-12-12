/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.mappers;

import com.xact.assessment.dtos.ReportCategoryResponse;
import com.xact.assessment.dtos.ReportModuleResponse;
import com.xact.assessment.dtos.ReportParameterResponse;
import com.xact.assessment.dtos.ReportTopicResponse;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.AssessmentModule;
import com.xact.assessment.models.AssessmentParameter;
import com.xact.assessment.models.AssessmentTopic;

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
}

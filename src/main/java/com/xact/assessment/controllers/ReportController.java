package com.xact.assessment.controllers;


import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.ReportService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller("/v1/reports")
public class ReportController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;
    private final UserAuthService userAuthService;
    private final AssessmentService assessmentService;


    public ReportController(ReportService reportService, AssessmentService assessmentService, UserAuthService userAuthService) {
        this.reportService = reportService;
        this.assessmentService = assessmentService;
        this.userAuthService = userAuthService;
    }

    @Get(value = "/assessments/{assessmentId}", produces = MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<byte[]> getReport(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        LOGGER.info("Get report for assessment: {}", assessmentId);

        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        if (assessment != null) {
            String reportName = "Report_x-act_" + assessmentId + ".xlsx";
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Workbook workbook = reportService.generateReport(assessmentId); // creates the workbook
                workbook.write(stream);
                workbook.close();
                return HttpResponse.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                return HttpResponse.serverError();
            }
        }
        return HttpResponse.serverError();
    }

    private Assessment getAuthenticatedAssessment(Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        return assessmentService.getAssessment(assessmentId, loggedInUser);
    }

    @Get(value = "/sunburst/{assessmentId}", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<ReportDataResponse> getAssessmentReportData(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId,authentication);
        List<AssessmentCategory> assessmentCategories = reportService.generateSunburstData(assessment.getAssessmentId());
        ReportDataResponse reportDataResponse = new ReportDataResponse();
        reportDataResponse.setName(assessment.getAssessmentName());
        List<ReportCategoryResponse> reportCategoryResponseList  = new ArrayList<>();
        for(AssessmentCategory assessmentCategory: assessmentCategories){
            ReportCategoryResponse reportCategoryResponse = new ReportCategoryResponse();
            reportCategoryResponse.setName(assessmentCategory.getCategoryName());
            reportCategoryResponse.setRating(assessmentCategory.getCategoryAverage());
            List<ReportModuleResponse> reportModuleResponseList = new ArrayList<>();
            for(AssessmentModule assessmentModule: assessmentCategory.getModules()){
                ReportModuleResponse reportModuleResponse = new ReportModuleResponse();
                reportModuleResponse.setName(assessmentModule.getModuleName());
                reportModuleResponse.setRating(assessmentModule.getModuleAverage());
                List<ReportTopicResponse> reportTopicResponseList = new ArrayList<>();
                for(AssessmentTopic assessmentTopic: assessmentModule.getTopics()){
                    ReportTopicResponse reportTopicResponse = new ReportTopicResponse();
                    reportTopicResponse.setName(assessmentTopic.getTopicName());
                    if(assessmentTopic.hasReferences()){
                        reportTopicResponse.setValue(assessmentTopic.getRating());
                    }
                    else{
                        reportTopicResponse.setRating(assessmentTopic.getTopicAverage());
                        List<ReportParameterResponse> reportParameterResponseList = new ArrayList<>();
                        for(AssessmentParameter assessmentParameter : assessmentTopic.getParameters()){
                            ReportParameterResponse reportParameterResponse = new ReportParameterResponse();
                            reportParameterResponse.setName(assessmentParameter.getParameterName());
                            reportParameterResponse.setValue(assessmentParameter.getRating());
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
        reportDataResponse.setChildren(reportCategoryResponseList);

        return HttpResponse.ok(reportDataResponse);

    }

    }


/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.controllers;


import com.xact.assessment.dtos.ReportCategoryResponse;
import com.xact.assessment.dtos.ReportDataResponse;
import com.xact.assessment.dtos.SummaryResponse;
import com.xact.assessment.mappers.ReportDataMapper;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentCategory;
import com.xact.assessment.models.User;
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
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller("/v1/reports")
@Transactional
public class ReportController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private final ReportService reportService;
    private final UserAuthService userAuthService;
    private final AssessmentService assessmentService;
    private final ReportDataMapper reportDataMapper = new ReportDataMapper();


    public ReportController(ReportService reportService, AssessmentService assessmentService, UserAuthService userAuthService) {
        this.reportService = reportService;
        this.assessmentService = assessmentService;
        this.userAuthService = userAuthService;
    }

    @Get(value = "/{assessmentId}", produces = MediaType.APPLICATION_OCTET_STREAM)
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
                LOGGER.error("Error", e);
                return HttpResponse.serverError();
            }
        }
        return HttpResponse.serverError();
    }

    @Get(value = "/{assessmentId}/charts/sunburst", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<ReportDataResponse> getAssessmentReportData(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        List<AssessmentCategory> assessmentCategories = reportService.generateSunburstData(assessment.getAssessmentId());
        ReportDataResponse reportDataResponse = new ReportDataResponse();
        reportDataResponse.setName(assessment.getAssessmentName());
        List<ReportCategoryResponse> reportCategoryResponseList = new ArrayList<>();
        reportDataMapper.mapToResponseStructure(assessmentCategories, reportCategoryResponseList);
        reportDataResponse.setChildren(reportCategoryResponseList);

        return HttpResponse.ok(reportDataResponse);

    }

    @Get(value = "/{assessmentId}/summary", produces = MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public HttpResponse<SummaryResponse> getSummary(@PathVariable("assessmentId") Integer assessmentId, Authentication authentication) {
        Assessment assessment = getAuthenticatedAssessment(assessmentId, authentication);
        SummaryResponse summaryResponse = reportService.getSummary(assessment.getAssessmentId());
        return HttpResponse.ok(summaryResponse);
    }

    @Get(value = "/template", produces = MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<byte[]> getReportTemplate() {
        LOGGER.info("Get report template");
        String reportName = "Thoughtworks-X-Act-Template.docx";
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("templates/Tech_Due_Diligence_Report_Sample.docx");
            byte[] fileData = IOUtils.toByteArray(is);
            is.close();
            return HttpResponse.ok(fileData).header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=" + reportName);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return HttpResponse.serverError();
        }
    }

    private Assessment getAuthenticatedAssessment(Integer assessmentId, Authentication authentication) {
        User loggedInUser = userAuthService.getLoggedInUser(authentication);
        return assessmentService.getAssessment(assessmentId, loggedInUser);
    }

}

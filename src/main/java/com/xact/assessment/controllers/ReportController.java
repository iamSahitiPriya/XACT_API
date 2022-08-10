package com.xact.assessment.controllers;


import com.xact.assessment.models.Assessment;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

@Controller("/v1/reports")
public class ReportController {

    private final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

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

}


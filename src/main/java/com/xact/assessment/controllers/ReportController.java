package com.xact.assessment.controllers;


import com.xact.assessment.services.ReportService;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;

@Controller("/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Get(value = "/assessments/{assessmentId}", produces = MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public MutableHttpResponse<byte[]> getReport(@PathVariable("assessmentId") Integer assessmentId) {

        String reportName = "Report_x-act_" + assessmentId + ".xlsx";
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Workbook workbook = reportService.generateReport(assessmentId); // creates the workbook
            workbook.write(stream);
            workbook.close();
            return HttpResponse.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);
        } catch (Exception e) {
            e.printStackTrace();
            return HttpResponse.serverError();
        }
    }

}

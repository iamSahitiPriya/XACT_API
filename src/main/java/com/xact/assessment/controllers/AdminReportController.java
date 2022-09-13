package com.xact.assessment.controllers;


import com.xact.assessment.annotations.AdminAuth;
import com.xact.assessment.services.AdminReportService;
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
@AdminAuth
public class AdminReportController {

    private static  final Logger LOGGER = LoggerFactory.getLogger(AdminReportController.class);

    private final AdminReportService adminReportService;



    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;

    }

    @Get(value = "/admin/{assessmentId}/{startDate}/{endDate}", produces = MediaType.APPLICATION_OCTET_STREAM)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public MutableHttpResponse<byte[]> getAdminReport(@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,Authentication authentication) {
            String reportName = "Admin-Report_x-act_.xlsx";
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Workbook workbook = adminReportService.generateAdminReport(startDate,endDate); // creates the workbook
                workbook.write(stream);
                workbook.close();
                return HttpResponse.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                return HttpResponse.serverError();
            }
        }

    }




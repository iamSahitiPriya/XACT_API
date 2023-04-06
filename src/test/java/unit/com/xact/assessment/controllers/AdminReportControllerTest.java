/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.AdminReportController;
import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentStatus;
import com.xact.assessment.models.Organisation;
import com.xact.assessment.services.AdminReportService;
import com.xact.assessment.services.AssessmentService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class AdminReportControllerTest {

    private final Authentication authentication = Mockito.mock(Authentication.class);

    private final AdminReportService adminReportService = Mockito.mock(AdminReportService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);

    private AdminReportController adminReportController = new AdminReportController(adminReportService);

    @Test
    void getReport() throws ParseException {

        Date created = new Date(2022 - 9 - 05);
        Date updated = new Date(2022 - 9 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);


        when(adminReportService.generateAdminReport("2022-09-06","2022-08-23")).thenReturn(getMockWorkbook());
        List<Assessment> assessments=new ArrayList<>();
        when(assessmentService.getAdminAssessmentsData("2022-09-06","2022-08-23")).thenReturn(assessments);

        MutableHttpResponse<byte[]> xlsDataResponse = adminReportController.getAdminReport("2022-09-06","2022-08-23",authentication);

        assertEquals(HttpStatus.OK, xlsDataResponse.status());
        assertNotNull(xlsDataResponse.body());
    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("2022-09-06 to 2022-08-23");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }
}


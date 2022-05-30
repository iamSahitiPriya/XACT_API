/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ReportController;
import com.xact.assessment.services.ReportService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ReportControllerTest {

    private final ReportService reportService = Mockito.mock(ReportService.class);
    private ReportController reportController = new ReportController(reportService);

    @Test
    void getReport() {
        Integer assessmentId = 123;
        when(reportService.generateReport(assessmentId)).thenReturn(getMockWorkbook());


        MutableHttpResponse<byte[]> xlsDataResponse = reportController.getReport(assessmentId);


        assertEquals(xlsDataResponse.status(), HttpStatus.OK);
        assertNotNull(xlsDataResponse.body());
    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("Category");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }
}

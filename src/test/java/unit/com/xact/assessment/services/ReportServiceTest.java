/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;


import com.xact.assessment.services.ReportService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {

    private ReportService reportService = new ReportService();

    @Test
    void getWorkbook() {
        Integer assessmentId = 123;

        Workbook report = reportService.generateReport(assessmentId);


        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("Category");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }

}

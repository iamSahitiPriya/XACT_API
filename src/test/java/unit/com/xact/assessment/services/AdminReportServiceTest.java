/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;


import com.xact.assessment.models.*;
import com.xact.assessment.services.AdminReportService;
import com.xact.assessment.services.AssessmentService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdminReportServiceTest {

    AssessmentService assessmentService = mock(AssessmentService.class);

    private final AdminReportService adminReportService = new AdminReportService(assessmentService);

    @Test
    void getWorkbookAssessmentData() throws ParseException {

        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, AssessmentStatus.Active, created1, updated1);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);

        AssessmentUser assessmentUser = new AssessmentUser();
        UserId userId = new UserId("test@gmail.com", assessment1);
        assessmentUser.setUserId(userId);
        assessmentUser.setRole(AssessmentRole.Owner);
        Set<AssessmentUser> assessmentUserSet = new HashSet<>();
        assessment1.setAssessmentUsers(assessmentUserSet);


        when(assessmentService.getAdminAssessmentsData("2022-05-30", "2022-09-30")).thenReturn(assessments);

        Workbook report = adminReportService.generateAdminReport("2022-05-30", "2022-09-30");

        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("2022-09-30 to 2022-05-30");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }
}

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;


import com.xact.assessment.models.*;
import com.xact.assessment.services.AdminReportService;
import com.xact.assessment.services.AssessmentService;
import com.xact.assessment.services.UsersAssessmentsService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdminReportServiceTest {

    AssessmentService assessmentService = mock(AssessmentService.class);

    UsersAssessmentsService usersAssessmentsService=mock(UsersAssessmentsService.class);

    private final AdminReportService adminReportService = new AdminReportService(assessmentService, usersAssessmentsService);

    @Test
    void getWorkbookAssessmentData() throws ParseException {

        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created1, updated1);

        List<Assessment> assessments = new ArrayList<>();
        assessments.add(assessment1);

        AssessmentUsers assessmentUsers=new AssessmentUsers();
        UserId userId=new UserId("test@gmail.com",assessment1);
        assessmentUsers.setUserId(userId);
        assessmentUsers.setRole(AssessmentRole.Owner);


        when(assessmentService.getAdminAssessmentsData("2022-05-30", "2022-09-30")).thenReturn(assessments);
        when(usersAssessmentsService.findOwnerByAssessmentId(assessment1.getAssessmentId())).thenReturn(Optional.of(assessmentUsers));

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
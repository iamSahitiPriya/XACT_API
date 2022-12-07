/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import com.xact.assessment.models.AssessmentUser;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Singleton
public class AdminReportService {

    public static final String FORMULA_STRING = "=-+@";

    private final AssessmentService assessmentService;

    private final UsersAssessmentsService usersAssessmentsService;

    private String assessmentStartDate;
    private String assessmentEndDate;

    public AdminReportService(AssessmentService assessmentService, UsersAssessmentsService usersAssessmentsService) {
        this.assessmentService = assessmentService;
        this.usersAssessmentsService = usersAssessmentsService;
    }

    public Workbook generateAdminReport(String startDate, String endDate) throws ParseException {
        assessmentStartDate = startDate;
        assessmentEndDate = endDate;
        List<Assessment> assessmentList = assessmentService.getAdminAssessmentsData(startDate, endDate);
        return createAdminReport(assessmentList);
    }

    private Workbook createAdminReport(List<Assessment> assessmentList) {
        Workbook workbook = new XSSFWorkbook();

        writeReport(assessmentList, workbook);

        return workbook;
    }

    private void writeReport(List<Assessment> assessmentList, Workbook workbook) {

        for (Assessment assessment : assessmentList) {
            writeAssessmentRow(workbook, assessment);
        }

    }

    private void writeAssessmentRow(Workbook workbook, Assessment assessment) {

        Sheet sheet = getMatchingSheet(workbook);
        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet, assessment);
    }

    private Sheet getMatchingSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheet(assessmentEndDate + " to " + assessmentStartDate);
        return (sheet == null) ? workbook.createSheet(assessmentEndDate + " to " + assessmentStartDate) : sheet;
    }

    private void generateHeaderIfNotExist(Sheet sheet, Workbook workbook) {
        int rowNum = sheet.getLastRowNum();

        if (rowNum == -1) {
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 13);
            font.setBold(true);
            style.setFont(font);

            createBoldCell(row, 0, "Assessment Id", style);
            createBoldCell(row, 1, "Assessment Purpose", style);
            createBoldCell(row, 2, "Assessment Name", style);
            createBoldCell(row, 3, "Organisation Name", style);
            createBoldCell(row, 4, "Industry of Organisation", style);
            createBoldCell(row, 5, "Domain of Target", style);
            createBoldCell(row, 6, "Size of Target Team", style);
            createBoldCell(row, 7, "Assessment Status", style);
            createBoldCell(row, 8, "Created At", style);
            createBoldCell(row, 9, "Updated At", style);
            createBoldCell(row, 10, "Owner of Assessment", style);
        }
    }

    private void createBoldCell(Row row, int i, String value, CellStyle style) {
        Cell cell = row.createCell(i, CellType.STRING);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createStyledCell(Row row, int i, String value, CellStyle style) {
        Cell cell = row.createCell(i, CellType.STRING);
        cell.setCellValue(value);
        if (value != null && !value.isBlank() && FORMULA_STRING.indexOf(value.trim().charAt(0)) >= 0) {
            cell.setCellStyle(style);
        }
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, Assessment assessment) {

        int size = assessment.getOrganisation().getSize();
        String teamSize = Integer.toString(size);

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();


        String userEmail = getEmail(assessment.getAssessmentId());


        createStyledCell(row, 0, assessment.getAssessmentId().toString(), style);
        createStyledCell(row, 1, assessment.getAssessmentPurpose(), style);
        createStyledCell(row, 2, assessment.getAssessmentName(), style);
        createStyledCell(row, 3, assessment.getOrganisation().getOrganisationName(), style);
        createStyledCell(row, 4, assessment.getOrganisation().getIndustry(), style);
        createStyledCell(row, 5, assessment.getOrganisation().getDomain(), style);
        createStyledCell(row, 6, teamSize, style);
        createStyledCell(row, 7, assessment.getAssessmentStatus().toString(), style);
        createStyledCell(row, 8, assessment.getCreatedAt().toString(), style);
        createStyledCell(row, 9, assessment.getUpdatedAt().toString(), style);
        createStyledCell(row, 10, userEmail, style);

    }

    private String getEmail(Integer assessmentId) {
        Optional<AssessmentUser> assessmentUsers = usersAssessmentsService.findOwnerByAssessmentId(assessmentId);
        if (assessmentUsers.isPresent()) {
            return assessmentUsers.get().getUserId().getUserEmail();
        }
        return "";
    }


}

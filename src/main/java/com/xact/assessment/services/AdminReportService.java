package com.xact.assessment.services;

import com.xact.assessment.models.Assessment;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.ParseException;
import java.util.List;

@Singleton
public class AdminReportService {

    public static final String FORMULA_STRING = "=-+@";

    private final AssessmentService assessmentService;

    private String assessmentStartDate;
    private String assessmentEndDate;

    public AdminReportService(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    public Workbook generateAdminReport(String startDate, String endDate) throws ParseException {
        assessmentStartDate=startDate;
        assessmentEndDate=endDate;
        List<Assessment> assessmentList = assessmentService.getAdminAssessmentsData(startDate,endDate);
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
        Sheet sheet = workbook.getSheet( assessmentEndDate+" to "+assessmentStartDate);
        return (sheet == null) ? workbook.createSheet( assessmentEndDate+" to "+assessmentStartDate) : sheet;
    }

    private void generateHeaderIfNotExist(Sheet sheet, Workbook workbook) {
        int rowNum = sheet.getLastRowNum();

        if (rowNum == -1) {
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            createBoldCell(row, 0, "S.No", style);
            createBoldCell(row, 1, "Assessment Name", style);
            createBoldCell(row, 2, "Organisation Name", style);
            createBoldCell(row, 3, "Industry", style);

            createBoldCell(row, 4, "Domain", style);
            createBoldCell(row, 5, "Size", style);
            createBoldCell(row, 6, "Status", style);
            createBoldCell(row, 7, "Created At", style);
            createBoldCell(row, 8, "Completed At", style);
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

        int size =assessment.getOrganisation().getSize();
        String teamSize = Integer.toString(size);

        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);

        int rowNum =row.getRowNum();
        String sNum = Integer.toString(rowNum);

        createStyledCell(row, 0,sNum , style);
        createStyledCell(row, 1, assessment.getAssessmentName(), style);
        createStyledCell(row, 2, assessment.getOrganisation().getOrganisationName(), style);
        createStyledCell(row, 3, assessment.getOrganisation().getIndustry(), style);
        createStyledCell(row, 4, assessment.getOrganisation().getDomain(), style);
        createStyledCell(row, 5,teamSize, style);
        createStyledCell(row, 6, assessment.getAssessmentStatus().toString(), style);
        createStyledCell(row, 7, assessment.getCreatedAt().toString(), style);
        createStyledCell(row, 8, assessment.getUpdatedAt().toString(), style);

    }


}

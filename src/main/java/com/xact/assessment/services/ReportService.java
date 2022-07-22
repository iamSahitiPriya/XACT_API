package com.xact.assessment.services;

import com.xact.assessment.models.*;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Singleton
public class ReportService {

    public static final String BLANK_STRING = "";
    public static final String FORMULA_STRING = "=-+@";
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final AnswerService answerService;

    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, AnswerService answerService) {
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.answerService = answerService;
    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = answerService.getAnswers(assessmentId);
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);

        return createReport(answers, parameterAssessmentData, topicAssessmentData);
    }

    private Workbook createReport(List<Answer> answers, List<ParameterLevelAssessment> parameterAssessments, List<TopicLevelAssessment> topicLevelAssessments) {
        Workbook workbook = new XSSFWorkbook();

        for (Answer answer : answers) {
            writeAnswerRow(workbook, answer);
        }
        for (ParameterLevelAssessment parameterLevelAssessment : parameterAssessments) {
            writeParameterRow(workbook, parameterLevelAssessment);
        }
        for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessments) {
            writeTopicRow(workbook, topicLevelAssessment);
        }


        return workbook;
    }

    private void writeTopicRow(Workbook workbook, TopicLevelAssessment topicLevelAssessment) {
//        String recommendation = topicLevelAssessment.getRecommendation();
        String recommendation="";
        String rating = String.valueOf(topicLevelAssessment.getRating());
        AssessmentTopic topic = topicLevelAssessment.getTopicLevelId().getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);

        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet, module, topic, rating, recommendation);
    }

    private void writeParameterRow(Workbook workbook, ParameterLevelAssessment parameterLevelAssessment) {
        String recommendation = parameterLevelAssessment.getRecommendation();
        String rating = String.valueOf(parameterLevelAssessment.getRating());
        AssessmentParameter parameter = parameterLevelAssessment.getParameterLevelId().getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);
        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet, module, topic, parameter, rating, recommendation);
    }

    private void writeAnswerRow(Workbook workbook, Answer answer) {
        Question question = answer.getAnswerId().getQuestion();
        AssessmentParameter parameter = question.getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);
        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet,
                module,
                topic,
                BLANK_STRING,
                BLANK_STRING,
                parameter,
                BLANK_STRING,
                BLANK_STRING,
                question.getQuestionText(),
                answer.getAnswer());
    }


    private Sheet getMatchingSheet(Workbook workbook, AssessmentCategory category) {
        Sheet sheet = workbook.getSheet(category.getCategoryName());
        return (sheet == null) ? workbook.createSheet(category.getCategoryName()) : sheet;
    }

    private void generateHeaderIfNotExist(Sheet sheet, Workbook workbook) {
        int rowNum = sheet.getLastRowNum();

        if (rowNum == -1) {
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            createBoldCell(row, 0, "Module", style);
            createBoldCell(row, 1, "Topic", style);
            createBoldCell(row, 2, "Topic Score", style);
            createBoldCell(row, 3, "Topic Recommendation", style);
            createBoldCell(row, 4, "Parameter", style);
            createBoldCell(row, 5, "Parameter Score", style);
            createBoldCell(row, 6, "Parameter Recommendation", style);
            createBoldCell(row, 7, "Question", style);
            createBoldCell(row, 8, "Answer", style);
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

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, String topicRating, String topicRecommendation) {
        writeDataOnSheet(workbook, sheet, module, topic, topicRating, topicRecommendation, new AssessmentParameter(), BLANK_STRING, BLANK_STRING, BLANK_STRING, BLANK_STRING);
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, AssessmentParameter parameter, String paramRating, String paramRecommendation) {
        writeDataOnSheet(workbook, sheet, module, topic, ReportService.BLANK_STRING, ReportService.BLANK_STRING, parameter, paramRating, paramRecommendation, BLANK_STRING, BLANK_STRING);
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, String topicRating, String topicRecommendation, AssessmentParameter parameter, String paramRating, String
                                          paramRecommendation, String questionText, String answer) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);
        createStyledCell(row, 0, module.getModuleName(), style);
        createStyledCell(row, 1, topic.getTopicName(), style);
        createStyledCell(row, 2, topicRating, style);
        createStyledCell(row, 3, topicRecommendation, style);
        createStyledCell(row, 4, parameter.getParameterName(), style);
        createStyledCell(row, 5, paramRating, style);
        createStyledCell(row, 6, paramRecommendation, style);
        createStyledCell(row, 7, questionText, style);
        createStyledCell(row, 8, answer, style);
    }

}

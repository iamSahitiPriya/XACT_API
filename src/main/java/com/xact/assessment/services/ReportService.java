package com.xact.assessment.services;

import com.xact.assessment.models.*;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

@Singleton
public class ReportService {

    public static final String BLANK_STRING = "";
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
        String recommendation = topicLevelAssessment.getRecommendation();
        String rating = String.valueOf(topicLevelAssessment.getRating());
        AssessmentTopic topic = topicLevelAssessment.getTopicLevelId().getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);
        generateHeaderIfNotExist(sheet);
        writeDataOnSheet(sheet, module, topic, rating, recommendation);
    }

    private void writeParameterRow(Workbook workbook, ParameterLevelAssessment parameterLevelAssessment) {
        String recommendation = parameterLevelAssessment.getRecommendation();
        String rating = String.valueOf(parameterLevelAssessment.getRating());
        AssessmentParameter parameter = parameterLevelAssessment.getParameterLevelId().getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);
        generateHeaderIfNotExist(sheet);
        writeDataOnSheet(sheet, module, topic, parameter, rating, recommendation);
    }

    private void writeAnswerRow(Workbook workbook, Answer answer) {
        Question question = answer.getAnswerId().getQuestion();
        AssessmentParameter parameter = question.getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);
        generateHeaderIfNotExist(sheet);
        writeDataOnSheet(sheet,
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

    private void generateHeaderIfNotExist(Sheet sheet) {
        int rowNum = sheet.getLastRowNum();
        if (rowNum == -1) {
            Row row = sheet.createRow(0);
            row.createCell(0, CellType.STRING).setCellValue("Module");
            row.createCell(1, CellType.STRING).setCellValue("Topic");
            row.createCell(2, CellType.STRING).setCellValue("Topic Score");
            row.createCell(3, CellType.STRING).setCellValue("Topic Recommendation");
            row.createCell(4, CellType.STRING).setCellValue("Parameter");
            row.createCell(5, CellType.STRING).setCellValue("Parameter Score");
            row.createCell(6, CellType.STRING).setCellValue("Parameter Recommendation");
            row.createCell(7, CellType.STRING).setCellValue("Question");
            row.createCell(8, CellType.STRING).setCellValue("Answer");
        }
    }

    private void writeDataOnSheet(Sheet sheet, AssessmentModule module, AssessmentTopic topic, String topicRating, String topicRecommendation) {
        writeDataOnSheet(sheet, module, topic, topicRating, topicRecommendation, new AssessmentParameter(), BLANK_STRING, BLANK_STRING, BLANK_STRING, BLANK_STRING);
    }

    private void writeDataOnSheet(Sheet sheet, AssessmentModule module, AssessmentTopic topic, AssessmentParameter parameter, String paramRating, String paramRecommendation) {
        writeDataOnSheet(sheet, module, topic, ReportService.BLANK_STRING, ReportService.BLANK_STRING, parameter, paramRating, paramRecommendation, BLANK_STRING, BLANK_STRING);
    }

    private void writeDataOnSheet(Sheet sheet, AssessmentModule module, AssessmentTopic topic, String topicRating, String topicRecommendation, AssessmentParameter parameter, String paramRating, String paramRecommendation, String questionText, String answer) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0, CellType.STRING).setCellValue(module.getModuleName());
        row.createCell(1, CellType.STRING).setCellValue(topic.getTopicName());
        row.createCell(2, CellType.STRING).setCellValue(topicRating);
        row.createCell(3, CellType.STRING).setCellValue(topicRecommendation);
        row.createCell(4, CellType.STRING).setCellValue(parameter.getParameterName());
        row.createCell(5, CellType.STRING).setCellValue(paramRating);
        row.createCell(6, CellType.STRING).setCellValue(paramRecommendation);
        row.createCell(7, CellType.STRING).setCellValue(questionText);
        row.createCell(8, CellType.STRING).setCellValue(answer);
    }

}

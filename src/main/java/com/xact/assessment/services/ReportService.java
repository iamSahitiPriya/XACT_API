package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class ReportService {

    public static final String BLANK_STRING = "";
    public static final String FORMULA_STRING = "=-+@";
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final AnswerService answerService;
    private final ChartService chartService;
    private final AverageCategoryService averageCategoryService;
    private final CategoryRepository categoryRepository;

    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, AnswerService answerService, ChartService chartService, AverageCategoryService averageCategoryService, CategoryRepository categoryRepository) {
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.answerService = answerService;
        this.chartService = chartService;
        this.averageCategoryService = averageCategoryService;
        this.categoryRepository = categoryRepository;
    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = answerService.getAnswers(assessmentId);
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);

        return createReport(answers, parameterAssessmentData, topicAssessmentData,assessmentId);
    }

    private Workbook createReport(List<Answer> answers, List<ParameterLevelAssessment> parameterAssessments, List<TopicLevelAssessment> topicLevelAssessments,Integer assessmentId) {
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
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        Map<String,List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
        List<AssessmentCategory> assessmentCategoryList = categoryRepository.findAll();
        for(AssessmentCategory assessmentCategory: assessmentCategoryList){
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = averageCategoryService.getAverage(assessmentCategory.getCategoryId(),assessment);
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfCurrentScores.add(categoryMaturity);
        }
        dataSet.put("Current Maturity",listOfCurrentScores);
        List<CategoryMaturity> listOfDesiredScores = new ArrayList<>();

        for(AssessmentCategory assessmentCategory: assessmentCategoryList){
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = 5.0;
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfDesiredScores.add(categoryMaturity);
        }
        dataSet.put("Desired Maturity",listOfDesiredScores);

        generateCharts(workbook,dataSet);

        return workbook;
    }

    private void generateCharts(Workbook workbook,Map<String, List<CategoryMaturity>> dataSet) {
        Sheet sheet = workbook.createSheet("Charts");
        //Get the contents of an InputStream as a byte[].
        Map<String, List<CategoryMaturity>> dataSet1 = null;
        byte[] bytes = chartService.generateChart(dataSet);
        //Adds a picture to the workbook
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        //create an anchor with upper left cell _and_ bottom right cell
        anchor.setCol1(1); //Column B
        anchor.setRow1(2); //Row 3
        anchor.setCol2(15); //Column 16th
        anchor.setRow2(21); //Row 22
        drawing.createPicture(anchor, pictureIdx);
    }

    private void writeTopicRow(Workbook workbook, TopicLevelAssessment topicLevelAssessment) {
        String recommendation = topicLevelAssessment.getRecommendation();
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

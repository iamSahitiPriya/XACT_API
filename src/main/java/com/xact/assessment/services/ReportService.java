package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

@Singleton
public class ReportService {

    public static final String BLANK_STRING = "";
    public static final String FORMULA_STRING = "=-+@";
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final AnswerService answerService;
    private final ChartService chartService;
    private final CategoryRepository categoryRepository;
    private final TopicService topicService;


    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, AnswerService answerService, ChartService chartService, CategoryRepository categoryRepository,TopicService topicService) {
        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.answerService = answerService;
        this.chartService = chartService;
        this.categoryRepository = categoryRepository;
        this.topicService = topicService;


    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = answerService.getAnswers(assessmentId);
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
        List<TopicLevelRecommendation> topicLevelRecommendationData = topicAndParameterLevelAssessmentService.getAssessmentRecommendationData(assessmentId);
        HashMap<Integer,List<TopicLevelRecommendation>> topicLevelRecommendationList = getTopicwiseRecommendations(topicLevelRecommendationData,assessmentId);

        return createReport(answers, parameterAssessmentData, topicAssessmentData, topicLevelRecommendationList, assessmentId);
    }

    private HashMap<Integer,List<TopicLevelRecommendation>> getTopicwiseRecommendations(List<TopicLevelRecommendation> topicLevelRecommendations, Integer assessmentId) {
        HashMap<Integer,List<TopicLevelRecommendation>> topicLevelRecommendationMap = new HashMap<>();
        List <TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        for(TopicLevelRecommendation topicLevelRecommendation : topicLevelRecommendations) {
            List<TopicLevelRecommendation> topicLevelRecommendationList1 = topicAndParameterLevelAssessmentService.getTopicAssessmentRecommendationData(assessmentId,topicLevelRecommendation.getTopic().getTopicId());
            topicLevelRecommendationList.addAll(topicLevelRecommendationList1);
            topicLevelRecommendationMap.put(topicLevelRecommendation.getTopic().getTopicId(),topicLevelRecommendationList1);

        }

        return topicLevelRecommendationMap;
    }

    private Workbook createReport(List<Answer> answers, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments, HashMap<Integer,List<TopicLevelRecommendation>> topicLevelRecommendations, Integer assessmentId) {
        Workbook workbook = new XSSFWorkbook();

        writeReport(answers,parameterLevelAssessments,topicLevelAssessments,topicLevelRecommendations,workbook);

        createDataAndGenerateChart(workbook, assessmentId,parameterLevelAssessments,topicLevelAssessments);

        return workbook;
    }
    public void writeReport(List<Answer> answers, List<ParameterLevelAssessment> parameterAssessments, List<TopicLevelAssessment> topicLevelAssessments, HashMap<Integer,List<TopicLevelRecommendation>> topicLevelRecommendations, Workbook workbook){
        for (Answer answer : answers) {
            writeAnswerRow(workbook, answer);
        }
        for (ParameterLevelAssessment parameterLevelAssessment : parameterAssessments) {
            writeParameterRow(workbook, parameterLevelAssessment);
        }
        for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessments) {
            List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.get(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
            topicLevelRecommendations.remove(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
            writeTopicRow(workbook, topicLevelAssessment,topicLevelRecommendationList);
        }
        for (Map.Entry<Integer, List<TopicLevelRecommendation>> entry : topicLevelRecommendations.entrySet()) {
            Integer key = entry.getKey();
            AssessmentTopic assessmentTopic = topicService.getTopic(key).orElse(new AssessmentTopic());
            AssessmentModule assessmentModule = assessmentTopic.getModule();
            List<TopicLevelRecommendation> topicLevelRecommendationList = entry.getValue();
            AssessmentCategory category = assessmentModule.getCategory();

            Sheet sheet = getMatchingSheet(workbook, category);

            generateHeaderIfNotExist(sheet, workbook);
            writeDataOnSheet(workbook, sheet, assessmentModule, assessmentTopic, "", topicLevelRecommendationList);

        }
    }
    public void createDataAndGenerateChart(Workbook workbook,Integer assessmentId,List<ParameterLevelAssessment> parameterLevelAssessments,List<TopicLevelAssessment> topicLevelAssessments){
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        Map<String,List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
        List<AssessmentCategory> assessmentCategoryList = categoryRepository.findAll();
        for(AssessmentCategory assessmentCategory: assessmentCategoryList){
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = assessmentCategory.getCategoryAverage(topicLevelAssessments,parameterLevelAssessments);
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
    }

    public void generateCharts(Workbook workbook,Map<String, List<CategoryMaturity>> dataSet) {
        Sheet sheet = workbook.createSheet("Charts");
        //Get the contents of an InputStream as a byte[].
        byte[] bytes = chartService.generateChart(dataSet);
        //Adds a picture to the workbook
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        //create an anchor with upper left cell _and_ bottom right cell
        anchor.setCol1(1); //Column B
        anchor.setRow1(2); //Row 3
        anchor.setCol2(12); //Column 13th
        anchor.setRow2(24); //Row 25
        drawing.createPicture(anchor, pictureIdx);
    }

    private void writeTopicRow(Workbook workbook, TopicLevelAssessment topicLevelAssessment, List<TopicLevelRecommendation> topicLevelRecommendations) {

        String rating = String.valueOf(topicLevelAssessment.getRating());
        AssessmentTopic topic = topicLevelAssessment.getTopicLevelId().getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);

        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet, module, topic, rating, topicLevelRecommendations);
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
                new TopicLevelRecommendation(),
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
            createBoldCell(row, 4, "Impact", style);
            createBoldCell(row, 5, "Effort", style);
            createBoldCell(row, 6, "Delivery Horizon", style);
            createBoldCell(row, 7, "Parameter", style);
            createBoldCell(row, 8, "Parameter Score", style);
            createBoldCell(row, 9, "Parameter Recommendation", style);
            createBoldCell(row, 10, "Question", style);
            createBoldCell(row, 11, "Answer", style);
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
            topic, String topicRating, List<TopicLevelRecommendation> topicRecommendation) {
        for(int index=0;index<topicRecommendation.size();index++){
            if(index == 0) {
                writeDataOnSheet(workbook, sheet, module, topic, topicRating, topicRecommendation.get(index), new AssessmentParameter(), BLANK_STRING, BLANK_STRING, BLANK_STRING, BLANK_STRING);
            }
            else {
                writeDataOnSheet(workbook, sheet, module, topic, " ", topicRecommendation.get(index), new AssessmentParameter(), BLANK_STRING, BLANK_STRING, BLANK_STRING, BLANK_STRING);
            }
        }

    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, AssessmentParameter parameter, String paramRating, String paramRecommendation) {
        writeDataOnSheet(workbook, sheet, module, topic, ReportService.BLANK_STRING, new TopicLevelRecommendation(), parameter, paramRating, paramRecommendation, BLANK_STRING, BLANK_STRING);
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, String topicRating, TopicLevelRecommendation topicRecommendation, AssessmentParameter parameter, String paramRating, String
                                          paramRecommendation, String questionText, String answer) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);
        createStyledCell(row, 0, module.getModuleName(), style);
        createStyledCell(row, 1, topic.getTopicName(), style);
        createStyledCell(row, 2, topicRating, style);
        createStyledCell(row, 3, topicRecommendation.getRecommendation(), style);
        createStyledCell(row, 4, topicRecommendation.getRecommendationImpact().toString(), style);
        createStyledCell(row, 5, topicRecommendation.getRecommendationEffort().toString(), style);
        createStyledCell(row, 6, topicRecommendation.getDeliveryHorizon(), style);
        createStyledCell(row, 7, parameter.getParameterName(), style);
        createStyledCell(row, 8, paramRating, style);
        createStyledCell(row, 9, paramRecommendation, style);
        createStyledCell(row, 10, questionText, style);
        createStyledCell(row, 11, answer, style);
    }

}

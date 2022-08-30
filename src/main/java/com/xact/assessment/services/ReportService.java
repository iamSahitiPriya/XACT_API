package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Singleton
public class ReportService {

    private static final String BLANK_STRING = "";

    private int recommendationCount = 0;

    private final String FORMULA_STRING = "=-+@";
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;
    private final AnswerService answerService;
    private final ChartService chartService;
    private final CategoryRepository categoryRepository;

    private final TopicService topicService;
    private final ParameterService parameterService;


    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, AnswerService answerService, ChartService chartService, CategoryRepository categoryRepository, TopicService topicService, ParameterService parameterService) {

        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.answerService = answerService;
        this.chartService = chartService;
        this.categoryRepository = categoryRepository;
        this.topicService = topicService;
        this.parameterService = parameterService;


    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = answerService.getAnswers(assessmentId);
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
        List<TopicLevelRecommendation> topicLevelRecommendationData = topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId);
        List<ParameterLevelRecommendation> parameterLevelRecommendationData = topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId);
        HashMap<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = getTopicWiseRecommendations(topicLevelRecommendationData, assessmentId);
        HashMap<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = getParameterWiseRecommendations(parameterLevelRecommendationData, assessmentId);
        return createReport(answers, parameterAssessmentData, topicAssessmentData, topicLevelRecommendationMap, parameterLevelRecommendationMap, assessmentId);
    }

    private HashMap<Integer, List<ParameterLevelRecommendation>> getParameterWiseRecommendations(List<ParameterLevelRecommendation> parameterLevelRecommendations, Integer assessmentId) {
        HashMap<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = new HashMap<>();
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        for (ParameterLevelRecommendation parameterLevelRecommendation : parameterLevelRecommendations) {
            List<ParameterLevelRecommendation> parameterLevelRecommendationList1 = topicAndParameterLevelAssessmentService.getParameterAssessmentRecommendationData(assessmentId, parameterLevelRecommendation.getParameter().getParameterId());
            parameterLevelRecommendationList.addAll(parameterLevelRecommendationList1);
            parameterLevelRecommendationMap.put(parameterLevelRecommendation.getParameter().getParameterId(), parameterLevelRecommendationList1);

        }
        return parameterLevelRecommendationMap;
    }

    private HashMap<Integer, List<TopicLevelRecommendation>> getTopicWiseRecommendations(List<TopicLevelRecommendation> topicLevelRecommendations, Integer assessmentId) {
        HashMap<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();

        for (TopicLevelRecommendation topicLevelRecommendation : topicLevelRecommendations) {
            List<TopicLevelRecommendation> topicLevelRecommendationList1 = topicAndParameterLevelAssessmentService.getTopicAssessmentRecommendationData(assessmentId, topicLevelRecommendation.getTopic().getTopicId());
            topicLevelRecommendationList.addAll(topicLevelRecommendationList1);
            topicLevelRecommendationMap.put(topicLevelRecommendation.getTopic().getTopicId(), topicLevelRecommendationList1);
        }
        return topicLevelRecommendationMap;
    }

    private Workbook createReport(List<Answer> answers, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments, HashMap<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, HashMap<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Integer assessmentId) {
        Workbook workbook = new XSSFWorkbook();

        writeReport(answers, parameterLevelAssessments, topicLevelAssessments, topicLevelRecommendations, parameterLevelRecommendations, workbook);

        createDataAndGenerateChart(workbook, assessmentId, parameterLevelAssessments, topicLevelAssessments);

        return workbook;
    }


    private void writeReport(List<Answer> answers, List<ParameterLevelAssessment> parameterAssessments, List<TopicLevelAssessment> topicLevelAssessments, HashMap<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, HashMap<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Workbook workbook) {
        for (Answer answer : answers) {
            writeAnswerRow(workbook, answer);
        }
        if(parameterAssessments.size() > 0) {
            for (ParameterLevelAssessment parameterLevelAssessment : parameterAssessments) {
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterLevelRecommendations.get(parameterLevelAssessment.getParameterLevelId().getParameter().getParameterId());
                if (parameterLevelRecommendationList != null) {
                    parameterLevelRecommendations.remove(parameterLevelAssessment.getParameterLevelId().getParameter().getParameterId());
                    writeParameterRow(workbook, parameterLevelAssessment, parameterLevelRecommendationList);
                } else {
                    writeParameterRow(workbook, parameterLevelAssessment, new ArrayList<>());
                }

            }
        }
        else {
            for (Map.Entry<Integer, List<ParameterLevelRecommendation>> entry : parameterLevelRecommendations.entrySet()) {
                Integer key = entry.getKey();
                AssessmentParameter assessmentParameter = parameterService.getParameter(key).orElse(new AssessmentParameter());
                AssessmentTopic assessmentTopic = assessmentParameter.getTopic();
                AssessmentModule assessmentModule = assessmentTopic.getModule();
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = entry.getValue();
                AssessmentCategory category = assessmentModule.getCategory();
                Sheet sheet = getMatchingSheet(workbook, category);

                generateHeaderIfNotExist(sheet, workbook);
                writeDataOnSheet(workbook, sheet, assessmentModule, assessmentTopic, assessmentParameter, "", parameterLevelRecommendationList);

            }
        }
        if(topicLevelAssessments.size() > 0) {
            for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessments) {
                List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.get(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
                if (topicLevelRecommendationList != null) {
                    topicLevelRecommendations.remove(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
                    writeTopicRow(workbook, topicLevelAssessment, topicLevelRecommendationList);
                } else {
                    writeTopicRow(workbook, topicLevelAssessment, new ArrayList<>());
                }

            }
        }
        else {
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

    }

    private void createDataAndGenerateChart(Workbook workbook, Integer assessmentId, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments) {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        Map<String, List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
        List<AssessmentCategory> assessmentCategoryList = categoryRepository.findAll();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            fillInMaturityScore(assessmentCategory, topicLevelAssessments, parameterLevelAssessments);
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = assessmentCategory.getCategoryAverage();
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfCurrentScores.add(categoryMaturity);
        }
        dataSet.put("Current Maturity", listOfCurrentScores);
        List<CategoryMaturity> listOfDesiredScores = new ArrayList<>();

        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            CategoryMaturity categoryMaturity = new CategoryMaturity();
            double avgCategoryScore = 5.0;
            categoryMaturity.setCategory(assessmentCategory.getCategoryName());
            categoryMaturity.setScore(avgCategoryScore);
            listOfDesiredScores.add(categoryMaturity);
        }
        dataSet.put("Desired Maturity", listOfDesiredScores);

        generateCharts(workbook, dataSet);
    }

    private void generateCharts(Workbook workbook, Map<String, List<CategoryMaturity>> dataSet) {
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

    private void writeParameterRow(Workbook workbook, ParameterLevelAssessment parameterLevelAssessment, List<ParameterLevelRecommendation> parameterLevelRecommendations) {
        String rating = String.valueOf(parameterLevelAssessment.getRating());
        AssessmentParameter parameter = parameterLevelAssessment.getParameterLevelId().getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        AssessmentCategory category = module.getCategory();

        Sheet sheet = getMatchingSheet(workbook, category);

        generateHeaderIfNotExist(sheet, workbook);
        writeDataOnSheet(workbook, sheet, module, topic, parameter, rating, parameterLevelRecommendations);
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
                0,
                parameter,
                BLANK_STRING,
                new ParameterLevelRecommendation(),
                0,
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
            createBoldCell(row, 10, "Impact", style);
            createBoldCell(row, 11, "Effort", style);
            createBoldCell(row, 12, "Delivery Horizon", style);
            createBoldCell(row, 13, "Question", style);
            createBoldCell(row, 14, "Answer", style);

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

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic topic, String topicRating, List<TopicLevelRecommendation> topicRecommendation) {
        if (topicRecommendation.size() == 0) {
            writeDataOnSheet(workbook, sheet, module, topic, topicRating, new TopicLevelRecommendation(), topicRecommendation.size(), new AssessmentParameter(), BLANK_STRING, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
        } else {
            for (int index = 0; index < topicRecommendation.size(); index++) {
                if (index == 0) {
                    writeDataOnSheet(workbook, sheet, module, topic, topicRating, topicRecommendation.get(index), topicRecommendation.size(), new AssessmentParameter(), BLANK_STRING, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
                } else {
                    writeDataOnSheet(workbook, sheet, module, topic, " ", topicRecommendation.get(index), topicRecommendation.size(), new AssessmentParameter(), BLANK_STRING, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
                }
            }
        }
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, AssessmentParameter parameter, String paramRating, List<ParameterLevelRecommendation> parameterRecommendation) {
        if (parameterRecommendation.size() == 0) {
            writeDataOnSheet(workbook, sheet, module, topic, BLANK_STRING, new TopicLevelRecommendation(), 0, parameter, paramRating, new ParameterLevelRecommendation(), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
        } else {
            for (int index = 0; index < parameterRecommendation.size(); index++) {
                if (index == 0) {
                    writeDataOnSheet(workbook, sheet, module, topic, BLANK_STRING, new TopicLevelRecommendation(), 0, parameter, paramRating, parameterRecommendation.get(index), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
                } else {
                    writeDataOnSheet(workbook, sheet, module, topic, BLANK_STRING, new TopicLevelRecommendation(), 0, parameter, BLANK_STRING, parameterRecommendation.get(index), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
                }
            }
        }
    }


    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, String topicRating, TopicLevelRecommendation topicRecommendation, int topicRecommendationCount, AssessmentParameter parameter, String paramRating, ParameterLevelRecommendation
                                          paramRecommendation, int paramRecommendationCount, String questionText, String answer) {
        String topicImpact, topicEffort, paramImpact, paramEffort;
        topicImpact = getTopicRecommendationImpact(topicRecommendation);
        topicEffort = getTopicRecommendationEffort(topicRecommendation);
        paramImpact = getParameterRecommendationImpact(paramRecommendation);
        paramEffort = getParameterRecommendationEffort(paramRecommendation);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);

        createStyledCell(row, 0, module.getModuleName(), style);
        createStyledCell(row, 1, topic.getTopicName(), style);

        checkToMergeRatingColumn(sheet, topicRating, topicRecommendationCount, 2);

        createStyledCell(row, 2, topicRating, style);
        createStyledCell(row, 3, topicRecommendation.getRecommendation(), style);
        createStyledCell(row, 4, topicImpact, style);
        createStyledCell(row, 5, topicEffort, style);
        createStyledCell(row, 6, topicRecommendation.getDeliveryHorizon(), style);

        createStyledCell(row, 7, parameter.getParameterName(), style);

        checkToMergeRatingColumn(sheet, paramRating, paramRecommendationCount, 8);

        createStyledCell(row, 8, paramRating, style);
        createStyledCell(row, 9, paramRecommendation.getRecommendation(), style);
        createStyledCell(row, 10, paramImpact, style);
        createStyledCell(row, 11, paramEffort, style);
        createStyledCell(row, 12, paramRecommendation.getDeliveryHorizon(), style);
        createStyledCell(row, 13, questionText, style);
        createStyledCell(row, 14, answer, style);
    }

    private String getParameterRecommendationImpact(ParameterLevelRecommendation paramRecommendation) {
        String impact;
        if (paramRecommendation.getRecommendationImpact() != null) {
            impact = paramRecommendation.getRecommendationImpact().toString();
        } else {
            impact = " ";
        }
        return impact;
    }

    private String getParameterRecommendationEffort(ParameterLevelRecommendation parameterRecommendation) {
        String effort;
        if (parameterRecommendation.getRecommendationEffort() != null) {
            effort = parameterRecommendation.getRecommendationEffort().toString();
        } else {
            effort = " ";
        }
        return effort;
    }

    private String getTopicRecommendationEffort(TopicLevelRecommendation topicRecommendation) {
        String effort;
        if (topicRecommendation.getRecommendationEffort() != null) {
            effort = topicRecommendation.getRecommendationEffort().toString();
        } else {
            effort = " ";
        }
        return effort;
    }

    private String getTopicRecommendationImpact(TopicLevelRecommendation topicRecommendation) {
        String impact;
        if (topicRecommendation.getRecommendationImpact() != null) {
            impact = topicRecommendation.getRecommendationImpact().toString();
        } else {
            impact = " ";
        }
        return impact;
    }

    private void checkToMergeRatingColumn(Sheet sheet, String rating, int totalRecommendationCount, int columnNo) {
        if (rating.equals(" ")) {
            recommendationCount += 1;
        } else {
            if (totalRecommendationCount > 1) {
                recommendationCount += 1;
            }
        }

        mergeCells(sheet, totalRecommendationCount, recommendationCount, columnNo);
    }

    private void mergeCells(Sheet sheet, int totalRecommendationCount, int count, int columnNo) {
        if (count == totalRecommendationCount && count > 0) {
            recommendationCount = 0;
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - (totalRecommendationCount - 1), sheet.getLastRowNum(), columnNo, columnNo));
        }
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

    private void fillInMaturityScore(AssessmentCategory assessmentCategory, List<TopicLevelAssessment> topicLevelAssessments, List<ParameterLevelAssessment> parameterLevelAssessments) {
        for (AssessmentModule assessmentModule : assessmentCategory.getModules()) {
            for (AssessmentTopic assessmentTopic : assessmentModule.getTopics()) {
                if (assessmentTopic.hasReferences()) {
                    for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessments) {
                        if (topicLevelAssessment.getTopicLevelId().getTopic().getTopicId().equals(assessmentTopic.getTopicId())) {
                            assessmentTopic.setRating(topicLevelAssessment.getRating());
                        }
                    }
                } else {
                    for (AssessmentParameter assessmentParameter : assessmentTopic.getParameters()) {
                        for (ParameterLevelAssessment parameterLevelAssessment : parameterLevelAssessments) {
                            if (parameterLevelAssessment.getParameterLevelId().getParameter().getParameterId().equals(assessmentParameter.getParameterId())) {
                                assessmentParameter.setRating(parameterLevelAssessment.getRating());
                            }
                        }

                    }
                }
            }
        }
    }


}

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import jakarta.inject.Singleton;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;


@Singleton
public class ReportService {

    private static final String BLANK_STRING = "";
    private static final Integer ZERO = 0;

    private int recommendationCount = 0;

    private static final String FORMULA_STRING = "=-+@";
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService;

    private final AnswerService answerService;
    private final ChartService chartService;
    private final CategoryRepository categoryRepository;

    private final TopicService topicService;
    private final ParameterService parameterService;

    private final AssessmentMasterDataService assessmentMasterDataService;

    private final AssessmentService assessmentService;
    private List<AssessmentCategory> assessmentCategoryList;
    private Set<Integer> selectedModules;


    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, AnswerService answerService, ChartService chartService, CategoryRepository categoryRepository, TopicService topicService, ParameterService parameterService, AssessmentMasterDataService assessmentMasterDataService, AssessmentService assessmentService) {

        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.answerService = answerService;
        this.chartService = chartService;
        this.categoryRepository = categoryRepository;
        this.topicService = topicService;
        this.parameterService = parameterService;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.assessmentService = assessmentService;
    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = answerService.getAnswers(assessmentId);
        assessmentCategoryList = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
        selectedModules = mapSelectedModulesInSet(assessmentCategoryList);
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
        List<TopicLevelRecommendation> topicLevelRecommendationData = topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId);
        List<ParameterLevelRecommendation> parameterLevelRecommendationData = topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId);
        Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = getTopicWiseRecommendations(topicLevelRecommendationData, assessmentId);
        Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = getParameterWiseRecommendations(parameterLevelRecommendationData, assessmentId);
        return createReport(answers, parameterAssessmentData, topicAssessmentData, topicLevelRecommendationMap, parameterLevelRecommendationMap, assessmentId);
    }

    private Set<Integer> mapSelectedModulesInSet(List<AssessmentCategory> assessmentCategoryList) {
        Set<Integer> selectedModules = new HashSet<>();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            Set<Integer> moduleIds = assessmentCategory.getModules().stream()
                    .map(assessmentModule -> assessmentModule.getModuleId())
                    .collect(toSet());
            selectedModules.addAll(moduleIds);
        }
        return selectedModules;
    }

    public List<AssessmentCategory> generateSunburstData(Integer assessmentId) {
        List<ParameterLevelAssessment> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId);
        List<TopicLevelAssessment> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId);
        List<AssessmentCategory> assessmentCategoryList = categoryRepository.findAll();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            fillInMaturityScore(assessmentCategory, topicAssessmentData, parameterAssessmentData);
        }
        return assessmentCategoryList;


    }

    public Map<Integer, List<ParameterLevelRecommendation>> getParameterWiseRecommendations(List<ParameterLevelRecommendation> parameterLevelRecommendations, Integer assessmentId) {
        Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = new HashMap<>();

        Set<Integer> parameterIds = parameterLevelRecommendations.stream()
                .map(parameterLevelRecommendation -> parameterLevelRecommendation.getParameter().getParameterId())
                .collect(toSet());
        for (Integer parameterId : parameterIds) {
            List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterLevelRecommendations.stream().filter(parameterLevelRecommendation -> parameterId.equals(parameterLevelRecommendation.getParameter().getParameterId())).collect(Collectors.toList());
            parameterLevelRecommendationMap.put(parameterId, parameterLevelRecommendationList);

        }
        return parameterLevelRecommendationMap;
    }

    private Map<Integer, List<TopicLevelRecommendation>> getTopicWiseRecommendations(List<TopicLevelRecommendation> topicLevelRecommendations, Integer assessmentId) {
        Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = new HashMap<>();
        Set<Integer> topicIds = topicLevelRecommendations.stream()
                .map(topicLevelRecommendation -> topicLevelRecommendation.getTopic().getTopicId())
                .collect(toSet());

        for (Integer topicId : topicIds) {
            List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.stream().filter(topicLevelRecommendation -> topicId.equals(topicLevelRecommendation.getTopic().getTopicId())).collect(Collectors.toList());
            topicLevelRecommendationMap.put(topicId, topicLevelRecommendationList);
        }
        return topicLevelRecommendationMap;
    }

    private Workbook createReport(List<Answer> answers, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments, Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Integer assessmentId) {
        Workbook workbook = new XSSFWorkbook();

        writeReport(answers, parameterLevelAssessments, topicLevelAssessments, topicLevelRecommendations, parameterLevelRecommendations, workbook, assessmentId);

        createDataAndGenerateChart(workbook, assessmentId, parameterLevelAssessments, topicLevelAssessments);

        return workbook;
    }


    private void writeReport(List<Answer> answers, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments, Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Workbook workbook, Integer assessmentId) {
        for (Answer answer : answers) {
            writeAnswerRow(workbook, answer, assessmentId);
        }
        if (!parameterLevelAssessments.isEmpty()) {
            for (ParameterLevelAssessment parameterLevelAssessment : parameterLevelAssessments) {
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterLevelRecommendations.get(parameterLevelAssessment.getParameterLevelId().getParameter().getParameterId());
                if (parameterLevelRecommendationList != null) {
                    parameterLevelRecommendations.remove(parameterLevelAssessment.getParameterLevelId().getParameter().getParameterId());
                    writeParameterRow(workbook, parameterLevelAssessment, parameterLevelRecommendationList, assessmentId);
                } else {
                    writeParameterRow(workbook, parameterLevelAssessment, new ArrayList<>(), assessmentId);
                }

            }
        } else {
            for (Map.Entry<Integer, List<ParameterLevelRecommendation>> entry : parameterLevelRecommendations.entrySet()) {
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = entry.getValue();
                if (parameterLevelRecommendationList != null && !parameterLevelRecommendationList.isEmpty()) {
                    AssessmentParameter assessmentParameter = parameterLevelRecommendationList.get(0).getParameter();
                    AssessmentTopic assessmentTopic = assessmentParameter.getTopic();
                    AssessmentModule assessmentModule = assessmentTopic.getModule();
                    if (checkIfModuleSelected(assessmentModule.getModuleId())) {
                        AssessmentCategory category = assessmentModule.getCategory();
                        Sheet sheet = getMatchingSheet(workbook, category);
                        generateHeaderIfNotExist(sheet, workbook);
                        writeDataOnSheet(workbook, sheet, assessmentModule, assessmentTopic, assessmentParameter, ZERO, parameterLevelRecommendationList);

                    }
                }
            }
        }
        if (!topicLevelAssessments.isEmpty()) {
            for (TopicLevelAssessment topicLevelAssessment : topicLevelAssessments) {
                List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.get(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
                if (topicLevelRecommendationList != null) {
                    topicLevelRecommendations.remove(topicLevelAssessment.getTopicLevelId().getTopic().getTopicId());
                    writeTopicRow(workbook, topicLevelAssessment, topicLevelRecommendationList, assessmentId);
                } else {
                    writeTopicRow(workbook, topicLevelAssessment, new ArrayList<>(), assessmentId);
                }

            }
        } else {
            for (Map.Entry<Integer, List<TopicLevelRecommendation>> entry : topicLevelRecommendations.entrySet()) {
                List<TopicLevelRecommendation> topicLevelRecommendationList = entry.getValue();
                if (topicLevelRecommendationList != null && !topicLevelRecommendationList.isEmpty()) {
                    AssessmentTopic assessmentTopic = topicLevelRecommendationList.get(0).getTopic();
                    AssessmentModule assessmentModule = assessmentTopic.getModule();
                    if (checkIfModuleSelected(assessmentModule.getModuleId())) {
                        AssessmentCategory category = assessmentModule.getCategory();
                        Sheet sheet = getMatchingSheet(workbook, category);
                        generateHeaderIfNotExist(sheet, workbook);
                        writeDataOnSheet(workbook, sheet, assessmentModule, assessmentTopic, ZERO, topicLevelRecommendationList);
                    }
                }
            }
        }

    }

    private boolean checkIfModuleSelected(Integer moduleId) {
        return selectedModules.contains(moduleId);
    }

    private void createDataAndGenerateChart(Workbook workbook, Integer assessmentId, List<ParameterLevelAssessment> parameterLevelAssessments, List<TopicLevelAssessment> topicLevelAssessments) {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        Map<String, List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
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

    private void writeTopicRow(Workbook workbook, TopicLevelAssessment topicLevelAssessment, List<TopicLevelRecommendation> topicLevelRecommendations, Integer assessmentId) {

        Integer rating = topicLevelAssessment.getRating();
        AssessmentTopic topic = topicLevelAssessment.getTopicLevelId().getTopic();
        AssessmentModule module = topic.getModule();
        if (checkIfModuleSelected(module.getModuleId())) {
            AssessmentCategory category = module.getCategory();
            Sheet sheet = getMatchingSheet(workbook, category);

            generateHeaderIfNotExist(sheet, workbook);

            writeDataOnSheet(workbook, sheet, module, topic, rating, topicLevelRecommendations);
        }
    }

    private void writeParameterRow(Workbook workbook, ParameterLevelAssessment parameterLevelAssessment, List<ParameterLevelRecommendation> parameterLevelRecommendations, Integer assessmentId) {
        Integer rating = parameterLevelAssessment.getRating();
        AssessmentParameter parameter = parameterLevelAssessment.getParameterLevelId().getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        if (checkIfModuleSelected(module.getModuleId())) {
            AssessmentCategory category = module.getCategory();

            Sheet sheet = getMatchingSheet(workbook, category);

            generateHeaderIfNotExist(sheet, workbook);
            writeDataOnSheet(workbook, sheet, module, topic, parameter, rating, parameterLevelRecommendations);
        }
    }

    private void writeAnswerRow(Workbook workbook, Answer answer, Integer assessmentId) {
        Question question = answer.getAnswerId().getQuestion();
        AssessmentParameter parameter = question.getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        if (checkIfModuleSelected(module.getModuleId())) {
            AssessmentCategory category = module.getCategory();

            Sheet sheet = getMatchingSheet(workbook, category);
            generateHeaderIfNotExist(sheet, workbook);
            writeDataOnSheet(workbook, sheet,
                    module,
                    topic,
                    ZERO,
                    new TopicLevelRecommendation(),
                    0,
                    parameter,
                    ZERO,
                    new ParameterLevelRecommendation(),
                    0,
                    question.getQuestionText(),
                    answer.getAnswer());
        }
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

    private void createStyledStringCell(Row row, int i, String value, CellStyle style) {
        Cell cell = row.createCell(i, CellType.STRING);
        cell.setCellValue(value);
        if (value != null && !value.isBlank() && FORMULA_STRING.indexOf(value.trim().charAt(0)) >= 0) {
            cell.setCellStyle(style);
        }
    }

    private void createStyledNumberCell(Row row, int i, Integer value) {
        if (ZERO.equals(value)) {
            row.createCell(i, CellType.BLANK);
        } else {
            Cell cell = row.createCell(i, CellType.NUMERIC);
            cell.setCellValue(value);
        }
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic topic, Integer topicRating, List<TopicLevelRecommendation> topicRecommendation) {
        if (topicRecommendation.isEmpty()) {
            writeDataOnSheet(workbook, sheet, module, topic, topicRating, new TopicLevelRecommendation(), topicRecommendation.size(), new AssessmentParameter(), ZERO, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
        } else {
            for (int index = 0; index < topicRecommendation.size(); index++) {
                if (index == 0) {
                    writeDataOnSheet(workbook, sheet, module, topic, topicRating, topicRecommendation.get(index), topicRecommendation.size(), new AssessmentParameter(), ZERO, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
                } else {
                    writeDataOnSheet(workbook, sheet, module, topic, ZERO, topicRecommendation.get(index), topicRecommendation.size(), new AssessmentParameter(), ZERO, new ParameterLevelRecommendation(), 0, BLANK_STRING, BLANK_STRING);
                }
            }
        }
    }

    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, AssessmentParameter parameter, Integer paramRating, List<ParameterLevelRecommendation> parameterRecommendation) {
        if (parameterRecommendation.isEmpty()) {
            writeDataOnSheet(workbook, sheet, module, topic, ZERO, new TopicLevelRecommendation(), 0, parameter, paramRating, new ParameterLevelRecommendation(), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
        } else {
            for (int index = 0; index < parameterRecommendation.size(); index++) {
                if (index == 0) {
                    writeDataOnSheet(workbook, sheet, module, topic, ZERO, new TopicLevelRecommendation(), 0, parameter, paramRating, parameterRecommendation.get(index), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
                } else {
                    writeDataOnSheet(workbook, sheet, module, topic, ZERO, new TopicLevelRecommendation(), 0, parameter, ZERO, parameterRecommendation.get(index), parameterRecommendation.size(), BLANK_STRING, BLANK_STRING);
                }
            }
        }
    }


    private void writeDataOnSheet(Workbook workbook, Sheet sheet, AssessmentModule module, AssessmentTopic
            topic, Integer topicRating, TopicLevelRecommendation topicRecommendation, int topicRecommendationCount, AssessmentParameter parameter, Integer paramRating, ParameterLevelRecommendation
                                          paramRecommendation, int paramRecommendationCount, String questionText, String answer) {
        String topicImpact, paramImpact, paramEffort;
        String topicEffort;
        topicImpact = getTopicRecommendationImpact(topicRecommendation);
        topicEffort = getTopicRecommendationEffort(topicRecommendation);
        paramImpact = getParameterRecommendationImpact(paramRecommendation);
        paramEffort = getParameterRecommendationEffort(paramRecommendation);
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);

        createStyledStringCell(row, 0, module.getModuleName(), style);
        createStyledStringCell(row, 1, topic.getTopicName(), style);

        checkToMergeRatingColumn(sheet, topicRating, topicRecommendationCount, 2);

        createStyledNumberCell(row, 2, topicRating);
        createStyledStringCell(row, 3, topicRecommendation.getRecommendation(), style);
        createStyledStringCell(row, 4, topicImpact, style);
        createStyledStringCell(row, 5, topicEffort, style);
        createStyledStringCell(row, 6, topicRecommendation.getDeliveryHorizon(), style);

        createStyledStringCell(row, 7, parameter.getParameterName(), style);

        checkToMergeRatingColumn(sheet, paramRating, paramRecommendationCount, 8);

        createStyledNumberCell(row, 8, paramRating);
        createStyledStringCell(row, 9, paramRecommendation.getRecommendation(), style);
        createStyledStringCell(row, 10, paramImpact, style);
        createStyledStringCell(row, 11, paramEffort, style);
        createStyledStringCell(row, 12, paramRecommendation.getDeliveryHorizon(), style);
        createStyledStringCell(row, 13, questionText, style);
        createStyledStringCell(row, 14, answer, style);
    }

    private String getParameterRecommendationImpact(ParameterLevelRecommendation paramRecommendation) {
        String impact;
        if (paramRecommendation.getRecommendationImpact() != null) {
            impact = paramRecommendation.getRecommendationImpact().toString();
        } else {
            impact = BLANK_STRING;
        }
        return impact;
    }

    private String getParameterRecommendationEffort(ParameterLevelRecommendation parameterRecommendation) {
        String effort;
        if (parameterRecommendation.getRecommendationEffort() != null) {
            effort = parameterRecommendation.getRecommendationEffort().toString();
        } else {
            effort = BLANK_STRING;
        }
        return effort;
    }

    private String getTopicRecommendationEffort(TopicLevelRecommendation topicRecommendation) {
        String effort;
        if (topicRecommendation.getRecommendationEffort() != null) {
            effort = topicRecommendation.getRecommendationEffort().toString();
        } else {
            effort = BLANK_STRING;
        }
        return effort;
    }

    private String getTopicRecommendationImpact(TopicLevelRecommendation topicRecommendation) {
        String impact;
        if (topicRecommendation.getRecommendationImpact() != null) {
            impact = topicRecommendation.getRecommendationImpact().toString();
        } else {
            impact = BLANK_STRING;
        }
        return impact;
    }

    private void checkToMergeRatingColumn(Sheet sheet, Integer rating, int totalRecommendationCount, int columnNo) {
        if (totalRecommendationCount > 1) {
            recommendationCount += 1;
        }
        mergeCells(sheet, totalRecommendationCount, recommendationCount, columnNo);
    }

    private void mergeCells(Sheet sheet, int totalRecommendationCount, int count, int columnNo) {
        if (count == totalRecommendationCount && count > 0) {
            recommendationCount = 0;
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum() - (totalRecommendationCount - 1), sheet.getLastRowNum(), columnNo, columnNo));
        }
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

/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package com.xact.assessment.services;

import com.xact.assessment.dtos.Recommendation;
import com.xact.assessment.dtos.*;
import com.xact.assessment.mappers.ReportDataMapper;
import com.xact.assessment.models.*;
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

    private final ChartService chartService;

    private final AssessmentMasterDataService assessmentMasterDataService;

    private List<AssessmentCategory> assessmentCategoryList;
    private Set<Integer> selectedModulesSet;
    private final UserQuestionService userQuestionService;

    private final ModuleService moduleService;
    private final ReportDataMapper mapper = new ReportDataMapper();


    public ReportService(TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService, ChartService chartService, AssessmentMasterDataService assessmentMasterDataService, UserQuestionService userQuestionService, ModuleService moduleService) {

        this.topicAndParameterLevelAssessmentService = topicAndParameterLevelAssessmentService;
        this.chartService = chartService;
        this.assessmentMasterDataService = assessmentMasterDataService;
        this.userQuestionService = userQuestionService;
        this.moduleService = moduleService;
    }

    public Workbook generateReport(Integer assessmentId) {
        List<Answer> answers = topicAndParameterLevelAssessmentService.getAnswers(assessmentId);
        List<UserQuestion> userQuestions = userQuestionService.findByAssessmentAndAnswer(assessmentId);
        assessmentCategoryList = assessmentMasterDataService.getUserAssessmentCategories(assessmentId);
        selectedModulesSet = mapSelectedModulesInSet(assessmentCategoryList);
        List<ParameterLevelRating> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessmentId);
        List<TopicLevelRating> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessmentId);
        List<TopicLevelRecommendation> topicLevelRecommendationData = topicAndParameterLevelAssessmentService.getTopicLevelRecommendations(assessmentId);
        List<ParameterLevelRecommendation> parameterLevelRecommendationData = topicAndParameterLevelAssessmentService.getParameterLevelRecommendations(assessmentId);
        Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = getTopicWiseRecommendations(topicLevelRecommendationData);
        Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = getParameterWiseRecommendations(parameterLevelRecommendationData);
        Map<Integer, List<ReportAnswerResponse>> answerParameterListMap = getParameterWiseAnswer(answers, userQuestions);
        return createReport(answerParameterListMap, parameterAssessmentData, topicAssessmentData, topicLevelRecommendationMap, parameterLevelRecommendationMap, assessmentId);
    }

    private Map<Integer, List<ReportAnswerResponse>> getParameterWiseAnswer(List<Answer> answers, List<UserQuestion> userQuestions) {
        Map<Integer, List<ReportAnswerResponse>> parameterListMap = new TreeMap<>();
        for (Answer answer : answers) {
            ReportAnswerResponse reportAnswerResponse = new ReportAnswerResponse(answer.getAnswerId().getQuestion().getParameter(), answer.getAnswerId().getQuestion().getQuestionText(), answer.getAnswerNote());
            mapAnswerBasedOnParameter(parameterListMap, reportAnswerResponse);
        }
        for (UserQuestion userQuestion : userQuestions) {
            ReportAnswerResponse reportAnswerResponse = new ReportAnswerResponse(userQuestion.getParameter(), userQuestion.getQuestion(), userQuestion.getAnswer());
            mapAnswerBasedOnParameter(parameterListMap, reportAnswerResponse);
        }
        return parameterListMap;
    }

    private void mapAnswerBasedOnParameter(Map<Integer, List<ReportAnswerResponse>> parameterListMap, ReportAnswerResponse reportAnswerResponse) {
        if (parameterListMap.containsKey(reportAnswerResponse.getParameter().getParameterId())) {
            parameterListMap.get(reportAnswerResponse.getParameter().getParameterId()).add(reportAnswerResponse);
        } else {
            List<ReportAnswerResponse> answerResponses = new ArrayList<>();
            answerResponses.add(reportAnswerResponse);
            parameterListMap.put(reportAnswerResponse.getParameter().getParameterId(), answerResponses);
        }
    }

    private Set<Integer> mapSelectedModulesInSet(List<AssessmentCategory> assessmentCategoryList) {
        Set<Integer> selectedModules = new HashSet<>();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            Set<Integer> moduleIds = assessmentCategory.getModules().stream()
                    .map(AssessmentModule::getModuleId)
                    .collect(toSet());
            selectedModules.addAll(moduleIds);
        }
        return selectedModules;
    }

    public List<AssessmentCategory> generateSunburstData(Assessment assessment) {
        List<ParameterLevelRating> parameterAssessmentData = topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessment.getAssessmentId());
        List<TopicLevelRating> topicAssessmentData = topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessment.getAssessmentId());
        List<AssessmentCategory> assessmentCategories = assessmentMasterDataService.getAllCategories();
        for (AssessmentCategory assessmentCategory : assessmentCategories) {
            fillInMaturityScore(assessmentCategory, topicAssessmentData, parameterAssessmentData, assessment);
        }
        return assessmentCategories;


    }


    public Map<Integer, List<ParameterLevelRecommendation>> getParameterWiseRecommendations(List<ParameterLevelRecommendation> parameterLevelRecommendations) {
        Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = new HashMap<>();

        Set<Integer> parameterIds = parameterLevelRecommendations.stream()
                .map(parameterLevelRecommendation -> parameterLevelRecommendation.getParameter().getParameterId())
                .collect(toSet());
        for (Integer parameterId : parameterIds) {
            List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterLevelRecommendations.stream().filter(parameterLevelRecommendation -> parameterId.equals(parameterLevelRecommendation.getParameter().getParameterId())).toList();
            parameterLevelRecommendationMap.put(parameterId, parameterLevelRecommendationList);

        }
        return parameterLevelRecommendationMap;
    }

    private Map<Integer, List<TopicLevelRecommendation>> getTopicWiseRecommendations(List<TopicLevelRecommendation> topicLevelRecommendations) {
        Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = new HashMap<>();
        Set<Integer> topicIds = topicLevelRecommendations.stream()
                .map(topicLevelRecommendation -> topicLevelRecommendation.getTopic().getTopicId())
                .collect(toSet());

        for (Integer topicId : topicIds) {
            List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.stream().filter(topicLevelRecommendation -> topicId.equals(topicLevelRecommendation.getTopic().getTopicId())).toList();
            topicLevelRecommendationMap.put(topicId, topicLevelRecommendationList);
        }
        return topicLevelRecommendationMap;
    }

    private Workbook createReport(Map<Integer, List<ReportAnswerResponse>> parameterAnswerListMap, List<ParameterLevelRating> parameterLevelRatings, List<TopicLevelRating> topicLevelRatings, Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Integer assessmentId) {
        Workbook workbook = new XSSFWorkbook();

        writeReport(parameterAnswerListMap, parameterLevelRatings, topicLevelRatings, topicLevelRecommendations, parameterLevelRecommendations, workbook);

        createDataAndGenerateChart(workbook, assessmentId, parameterLevelRatings, topicLevelRatings);

        return workbook;
    }


    private void writeReport(Map<Integer, List<ReportAnswerResponse>> parameterAnswerListMap, List<ParameterLevelRating> parameterLevelRatings, List<TopicLevelRating> topicLevelRatings, Map<Integer, List<TopicLevelRecommendation>> topicLevelRecommendations, Map<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendations, Workbook workbook) {
        for (Map.Entry<Integer, List<ReportAnswerResponse>> answer : parameterAnswerListMap.entrySet()) {
            writeAnswerRow(workbook, answer.getValue());
        }
        if (!parameterLevelRatings.isEmpty()) {
            for (ParameterLevelRating parameterLevelRating : parameterLevelRatings) {
                List<ParameterLevelRecommendation> parameterLevelRecommendationList = parameterLevelRecommendations.get(parameterLevelRating.getParameterLevelId().getParameter().getParameterId());
                if (parameterLevelRecommendationList != null) {
                    parameterLevelRecommendations.remove(parameterLevelRating.getParameterLevelId().getParameter().getParameterId());
                    writeParameterRow(workbook, parameterLevelRating, parameterLevelRecommendationList);
                } else {
                    writeParameterRow(workbook, parameterLevelRating, new ArrayList<>());
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
        if (!topicLevelRatings.isEmpty()) {
            for (TopicLevelRating topicLevelRating : topicLevelRatings) {
                List<TopicLevelRecommendation> topicLevelRecommendationList = topicLevelRecommendations.get(topicLevelRating.getTopicLevelId().getTopic().getTopicId());
                if (topicLevelRecommendationList != null) {
                    topicLevelRecommendations.remove(topicLevelRating.getTopicLevelId().getTopic().getTopicId());
                    writeTopicRow(workbook, topicLevelRating, topicLevelRecommendationList);
                } else {
                    writeTopicRow(workbook, topicLevelRating, new ArrayList<>());
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
        return selectedModulesSet.contains(moduleId);
    }

    private void createDataAndGenerateChart(Workbook workbook, Integer assessmentId, List<ParameterLevelRating> parameterLevelRatings, List<TopicLevelRating> topicLevelRatings) {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        Map<String, List<CategoryMaturity>> dataSet = new HashMap<>();
        List<CategoryMaturity> listOfCurrentScores = new ArrayList<>();
        for (AssessmentCategory assessmentCategory : assessmentCategoryList) {
            fillInMaturityScore(assessmentCategory, topicLevelRatings, parameterLevelRatings, assessment);
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
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        //create an anchor with upper left cell _and_ bottom right cell
        anchor.setCol1(1); //Column B
        anchor.setRow1(2); //Row 3
        anchor.setCol2(12); //Column 13th
        anchor.setRow2(24); //Row 25
        drawing.createPicture(anchor, pictureIdx);
    }

    private void writeTopicRow(Workbook workbook, TopicLevelRating topicLevelRating, List<TopicLevelRecommendation> topicLevelRecommendations) {

        Integer rating = topicLevelRating.getRating();
        AssessmentTopic topic = topicLevelRating.getTopicLevelId().getTopic();
        AssessmentModule module = topic.getModule();
        if (checkIfModuleSelected(module.getModuleId())) {
            AssessmentCategory category = module.getCategory();
            Sheet sheet = getMatchingSheet(workbook, category);

            generateHeaderIfNotExist(sheet, workbook);

            writeDataOnSheet(workbook, sheet, module, topic, rating, topicLevelRecommendations);
        }
    }

    private void writeParameterRow(Workbook workbook, ParameterLevelRating parameterLevelRating, List<ParameterLevelRecommendation> parameterLevelRecommendations) {
        Integer rating = parameterLevelRating.getRating();
        AssessmentParameter parameter = parameterLevelRating.getParameterLevelId().getParameter();
        AssessmentTopic topic = parameter.getTopic();
        AssessmentModule module = topic.getModule();
        if (checkIfModuleSelected(module.getModuleId())) {
            AssessmentCategory category = module.getCategory();

            Sheet sheet = getMatchingSheet(workbook, category);

            generateHeaderIfNotExist(sheet, workbook);
            writeDataOnSheet(workbook, sheet, module, topic, parameter, rating, parameterLevelRecommendations);
        }
    }

    private void writeAnswerRow(Workbook workbook, List<ReportAnswerResponse> answers) {
        for (ReportAnswerResponse answer : answers) {
            AssessmentParameter parameter = answer.getParameter();
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
                        answer.getQuestion(),
                        answer.getAnswer());
            }
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
        if (value == null || ZERO.equals(value)) {
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
        String topicImpact = getRecommendationImpact(topicRecommendation.getRecommendationImpact());
        String topicEffort = getRecommendationEffort(topicRecommendation.getRecommendationEffort());
        String topicDeliveryHorizon = getRecommendationDeliveryHorizon(topicRecommendation.getDeliveryHorizon());
        String paramImpact =getRecommendationImpact(paramRecommendation.getRecommendationImpact());
        String paramEffort = getRecommendationEffort(paramRecommendation.getRecommendationEffort());
        String paramDeliveryHorizon = getRecommendationDeliveryHorizon(paramRecommendation.getDeliveryHorizon());
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        CellStyle style = workbook.createCellStyle();
        style.setQuotePrefixed(true);

        createStyledStringCell(row, 0, module.getModuleName(), style);
        createStyledStringCell(row, 1, topic.getTopicName(), style);

        checkToMergeRatingColumn(sheet, topicRecommendationCount, 2);

        createStyledNumberCell(row, 2, topicRating);
        createStyledStringCell(row, 3, topicRecommendation.getRecommendationText(), style);
        createStyledStringCell(row, 4, topicImpact, style);
        createStyledStringCell(row, 5, topicEffort, style);
        createStyledStringCell(row, 6, topicDeliveryHorizon, style);

        createStyledStringCell(row, 7, parameter.getParameterName(), style);

        checkToMergeRatingColumn(sheet, paramRecommendationCount, 8);

        createStyledNumberCell(row, 8, paramRating);
        createStyledStringCell(row, 9, paramRecommendation.getRecommendationText(), style);
        createStyledStringCell(row, 10, paramImpact, style);
        createStyledStringCell(row, 11, paramEffort, style);
        createStyledStringCell(row, 12, paramDeliveryHorizon, style);
        createStyledStringCell(row, 13, questionText, style);
        createStyledStringCell(row, 14, answer, style);
    }

    private String getRecommendationImpact(RecommendationImpact recommendationImpact) {
        String impact;
        if (recommendationImpact!= null) {
            impact = recommendationImpact.toString();
        } else {
            impact = BLANK_STRING;
        }
        return impact;
    }
    private String getRecommendationEffort(RecommendationEffort recommendationEffort) {
        String effort;
        if (recommendationEffort != null) {
            effort = recommendationEffort.toString();
        } else {
            effort = BLANK_STRING;
        }
        return effort;
    }
    private String getRecommendationDeliveryHorizon(RecommendationDeliveryHorizon recommendationDeliveryHorizon) {
        String deliveryHorizon;
        if (recommendationDeliveryHorizon!= null) {
            deliveryHorizon = recommendationDeliveryHorizon.toString();
        } else {
            deliveryHorizon = BLANK_STRING;
        }
        return deliveryHorizon;
    }

    private void checkToMergeRatingColumn(Sheet sheet, int totalRecommendationCount, int columnNo) {
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


    private void fillInMaturityScore(AssessmentCategory assessmentCategory, List<TopicLevelRating> topicLevelRatings, List<ParameterLevelRating> parameterLevelRatings, Assessment assessment) {
        for (AssessmentModule assessmentModule : assessmentCategory.getModules()) {
            if (assessmentModule.getIsActive() && assessmentMasterDataService.isModuleSelectedByUser(assessment, assessmentModule)) {
                for (AssessmentTopic assessmentTopic : assessmentModule.getActiveTopics()) {
                    if (assessmentTopic.hasReferences()) {
                        setTopicRating(topicLevelRatings, assessmentTopic);
                    } else {
                        for (AssessmentParameter assessmentParameter : assessmentTopic.getActiveParameters()) {
                            setParameterRating(parameterLevelRatings, assessmentParameter);
                        }
                    }
                }
            }
        }
    }

    private void setParameterRating(List<ParameterLevelRating> parameterLevelRatings, AssessmentParameter assessmentParameter) {
        for (ParameterLevelRating parameterLevelRating : parameterLevelRatings) {
            if (parameterLevelRating.getParameterLevelId().getParameter().getParameterId().equals(assessmentParameter.getParameterId())) {
                assessmentParameter.setRating(parameterLevelRating.getRating());
            }
        }
    }

    private void setTopicRating(List<TopicLevelRating> topicLevelRatings, AssessmentTopic assessmentTopic) {
        for (TopicLevelRating topicLevelRating : topicLevelRatings) {
            if (topicLevelRating.getTopicLevelId().getTopic().getTopicId().equals(assessmentTopic.getTopicId())) {
                assessmentTopic.setRating(topicLevelRating.getRating());
            }
        }
    }


    public SummaryResponse getSummary(Integer assessmentId) {
        Integer totalNoOfQuestions = topicAndParameterLevelAssessmentService.getAnswers(assessmentId).size() + userQuestionService.findByAssessmentAndAnswer(assessmentId).size();
        List<ParameterLevelRating> parameterLevelRatingList = topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessmentId);
        List<TopicLevelRating> topicLevelRatingList = topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessmentId);


        Integer totalModule = moduleService.getAssessedModules(topicLevelRatingList, parameterLevelRatingList);
        Integer totalCategory = assessmentMasterDataService.getAssessedCategory(topicLevelRatingList, parameterLevelRatingList);
        int topicAssessed = getTotalAssessedTopicsCount(topicLevelRatingList, parameterLevelRatingList);
        int parameterAssessed = getTotalAssessedParamsCount(topicLevelRatingList, parameterLevelRatingList);

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setCategoryAssessed(totalCategory);
        summaryResponse.setModuleAssessed(totalModule);
        summaryResponse.setTopicAssessed(topicAssessed);
        summaryResponse.setParameterAssessed(parameterAssessed);
        summaryResponse.setQuestionAssessed(totalNoOfQuestions);
        return summaryResponse;

    }

    private int getTotalAssessedParamsCount(List<TopicLevelRating> topicLevelRatings, List<ParameterLevelRating> parameterLevelRatings) {
        int uniqueParamsAssessed = topicLevelRatings.stream().mapToInt(topicLevelAssessment -> topicLevelAssessment.getTopicLevelId().getTopic().getActiveParameters().size()).sum();
        return uniqueParamsAssessed + parameterLevelRatings.size();
    }

    private int getTotalAssessedTopicsCount(List<TopicLevelRating> topicLevelRatings, List<ParameterLevelRating> parameterLevelRatings) {
        Set<AssessmentTopic> uniqueTopics = new HashSet<>();
        topicLevelRatings.forEach(topicLevelAssessment -> uniqueTopics.add(topicLevelAssessment.getTopicLevelId().getTopic()));
        parameterLevelRatings.forEach(parameterLevelAssessment -> uniqueTopics.add(parameterLevelAssessment.getParameterLevelId().getParameter().getTopic()));
        return uniqueTopics.size();

    }


    public List<Recommendation> getRecommendations(Integer assessmentId) {
        List<TopicLevelRecommendation> topicLevelRecommendations = topicAndParameterLevelAssessmentService.getTopicRecommendations(assessmentId);
        List<ParameterLevelRecommendation> parameterLevelRecommendations = topicAndParameterLevelAssessmentService.getParameterRecommendations(assessmentId);
        List<Recommendation> recommendations = new ArrayList<>();

        recommendations.addAll(getTopicRecommendations(topicLevelRecommendations));

        recommendations.addAll(getParameterRecommendations(parameterLevelRecommendations));

        return groupRecommendationsByDeliveryHorizon(recommendations);
    }

    private List<Recommendation> getParameterRecommendations(List<ParameterLevelRecommendation> parameterLevelRecommendations) {
        List<Recommendation> recommendationList = new ArrayList<>();
        for (ParameterLevelRecommendation parameterLevelRecommendation : parameterLevelRecommendations) {
            if (parameterLevelRecommendation.getDeliveryHorizon() != null && parameterLevelRecommendation.getRecommendationEffort() != null && parameterLevelRecommendation.getRecommendationImpact() != null) {
                Recommendation recommendation = mapper.mapReportRecommendationResponse(parameterLevelRecommendation);
                recommendationList.add(recommendation);
            }
        }
        return recommendationList;
    }

    private List<Recommendation> getTopicRecommendations(List<TopicLevelRecommendation> topicLevelRecommendations) {
        List<Recommendation> recommendationList = new ArrayList<>();
        for (TopicLevelRecommendation topicLevelRecommendation : topicLevelRecommendations) {
            if (topicLevelRecommendation.getDeliveryHorizon() != null && topicLevelRecommendation.getRecommendationEffort() != null && topicLevelRecommendation.getRecommendationImpact() != null) {
                Recommendation recommendation = mapper.mapReportRecommendationResponse(topicLevelRecommendation);
                recommendationList.add(recommendation);
            }
        }
        return recommendationList;
    }

    private List<Recommendation> groupRecommendationsByDeliveryHorizon(List<Recommendation> recommendations) {
        Map<RecommendationDeliveryHorizon, Map<RecommendationImpact, List<Recommendation>>> sortedMap = recommendations.stream()
                .sorted(Recommendation::compareByDeliveryHorizon)
                .collect(Collectors.groupingBy(Recommendation::getDeliveryHorizon, LinkedHashMap::new,
                        Collectors.groupingBy(Recommendation::getImpact, LinkedHashMap::new, Collectors.toList())));
        return sortRecommendationsByImpact(sortedMap);
    }

    private List<Recommendation> sortRecommendationsByImpact(Map<RecommendationDeliveryHorizon, Map<RecommendationImpact, List<Recommendation>>> sortedMap) {
        List<Recommendation> recommendations = new ArrayList<>();

        for (Map.Entry<RecommendationDeliveryHorizon, Map<RecommendationImpact, List<Recommendation>>> deliveryHorizonMap : sortedMap.entrySet()) {
            sortRecommendations(recommendations, deliveryHorizonMap.getValue(), RecommendationImpact.HIGH);
            sortRecommendations(recommendations, deliveryHorizonMap.getValue(), RecommendationImpact.MEDIUM);
            sortRecommendations(recommendations, deliveryHorizonMap.getValue(), RecommendationImpact.LOW);
        }

        return recommendations;
    }

    private void sortRecommendations(List<Recommendation> sortedRecommendations, Map<RecommendationImpact, List<Recommendation>> deliveryHorizonMap, RecommendationImpact recommendationImpact) {
        if (deliveryHorizonMap.containsKey(recommendationImpact))
            sortedRecommendations.addAll(deliveryHorizonMap.get(recommendationImpact).stream().sorted(Recommendation::sortRecommendationByUpdatedTime).toList());
    }
}

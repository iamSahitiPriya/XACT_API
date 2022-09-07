/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;


import com.xact.assessment.models.*;
import com.xact.assessment.repositories.CategoryRepository;
import com.xact.assessment.services.*;
import com.xact.assessment.utils.ChartsUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static com.xact.assessment.models.RecommendationEffort.HIGH;
import static com.xact.assessment.models.RecommendationImpact.LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
    AnswerService answerService = mock(AnswerService.class);
    SpiderChartService chartService = mock(SpiderChartService.class);
    CategoryRepository categoryRepository = mock(CategoryRepository.class);

    TopicService topicService = mock(TopicService.class);
    ParameterService parameterService = mock(ParameterService.class);

    private final ReportService reportService = new ReportService(topicAndParameterLevelAssessmentService, answerService, chartService, categoryRepository, topicService, parameterService);

    @Test
    void getWorkbookAssessmentDataSheetWithRating() {
        Integer assessmentId = 123;
        List<Answer> answers = new ArrayList<>();

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Question question = new Question();
        question.setQuestionText("Question");
        question.setQuestionId(1);
        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterName("my param");
        parameter.setParameterId(1);
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        topic.setTopicName("my topic");
        AssessmentModule module = new AssessmentModule();
        module.setModuleName("my module");
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);

        topic.setModule(module);
        parameter.setTopic(topic);
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        List<ParameterLevelAssessment> parameterAssessments = new ArrayList<>();
        List<TopicLevelAssessment> topicAssessments = new ArrayList<>();
        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment();
        parameterAssessment.setRating(3);
        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);
        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendation("some text");
        topicLevelRecommendation1.setTopic(topic);
        topicLevelRecommendation1.setAssessment(assessment);
        topicLevelRecommendation1.setRecommendationImpact(LOW);
        topicLevelRecommendation1.setRecommendationEffort(HIGH);

        topicLevelRecommendationList.add(topicLevelRecommendation);
        topicLevelRecommendationList.add(topicLevelRecommendation1);

        topicRecommendationMap.put(topic.getTopicId(), topicLevelRecommendationList);

        HashMap<Integer, List<ParameterLevelRecommendation>> parameterRecommendationMap = new HashMap<>();
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendation("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);

        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId)).thenReturn(parameterLevelRecommendationList);
        when(parameterService.getParameter(parameter.getParameterId())).thenReturn(Optional.of(parameter));
        when(topicService.getTopic(topic.getTopicId())).thenReturn(Optional.of(topic));

        Workbook report = new XSSFWorkbook();
        reportService.writeReport(answers, parameterAssessments, topicAssessments, topicRecommendationMap, parameterRecommendationMap, report);

        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

    }

    @Test
    void getWorkbookAssessmentDataSheet() {
        Integer assessmentId = 123;
        List<Answer> answers = new ArrayList<>();

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Question question = new Question();
        question.setQuestionText("Question");
        question.setQuestionId(1);
        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterName("my param");
        parameter.setParameterId(1);
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        topic.setTopicName("my topic");
        AssessmentModule module = new AssessmentModule();
        module.setModuleName("my module");
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);

        topic.setModule(module);
        parameter.setTopic(topic);
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        List<ParameterLevelAssessment> parameterAssessments = new ArrayList<>();
        List<TopicLevelAssessment> topicAssessments = new ArrayList<>();
        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment();
        parameterAssessment.setRating(3);

        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);

        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendation("some text");
        topicLevelRecommendation1.setTopic(topic);
        topicLevelRecommendation1.setAssessment(assessment);
        topicLevelRecommendation1.setRecommendationImpact(LOW);
        topicLevelRecommendation1.setRecommendationEffort(HIGH);

        topicLevelRecommendationList.add(topicLevelRecommendation);
        topicLevelRecommendationList.add(topicLevelRecommendation1);

        topicRecommendationMap.put(topic.getTopicId(), topicLevelRecommendationList);

        HashMap<Integer, List<ParameterLevelRecommendation>> parameterRecommendationMap = new HashMap<>();
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendation("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);

        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId)).thenReturn(parameterLevelRecommendationList);
        when(parameterService.getParameter(parameter.getParameterId())).thenReturn(Optional.of(parameter));
        when(topicService.getTopic(topic.getTopicId())).thenReturn(Optional.of(topic));

        Workbook report = new XSSFWorkbook();
        reportService.writeReport(answers, parameterAssessments, topicAssessments, topicRecommendationMap, parameterRecommendationMap, report);

        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

    }

    @Test
    void getWorkbookAssessmentDataSheetWithoutRating() {
        Integer assessmentId = 123;
        List<Answer> answers = new ArrayList<>();

        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);
        Question question = new Question();
        question.setQuestionText("Question");
        question.setQuestionId(1);
        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterName("my param");
        parameter.setParameterId(1);
        AssessmentParameter parameter1 = new AssessmentParameter();
        parameter1.setParameterName("my param1");
        parameter1.setParameterId(2);
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        topic.setTopicName("my topic");
        AssessmentTopic topic1 = new AssessmentTopic();
        topic1.setTopicId(2);
        topic1.setTopicName("my topic");
        AssessmentModule module = new AssessmentModule();
        module.setModuleName("my module");
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);

        topic.setModule(module);
        topic1.setModule(module);
        parameter.setTopic(topic);
        parameter1.setTopic(topic1);
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        List<ParameterLevelAssessment> parameterAssessments = new ArrayList<>();
        List<TopicLevelAssessment> topicAssessments = new ArrayList<>();
        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment();
        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter1));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic1);
        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);

        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendation("some text");
        topicLevelRecommendation1.setTopic(topic);
        topicLevelRecommendation1.setAssessment(assessment);
        topicLevelRecommendation1.setRecommendationImpact(LOW);
        topicLevelRecommendation1.setRecommendationEffort(HIGH);

        topicLevelRecommendationList.add(topicLevelRecommendation);
        topicLevelRecommendationList.add(topicLevelRecommendation1);

        topicRecommendationMap.put(topic.getTopicId(), topicLevelRecommendationList);

        HashMap<Integer, List<ParameterLevelRecommendation>> parameterRecommendationMap = new HashMap<>();
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendation("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);


        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getAssessmentTopicRecommendationData(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getAssessmentParameterRecommendationData(assessmentId)).thenReturn(parameterLevelRecommendationList);
        when(parameterService.getParameter(parameter.getParameterId())).thenReturn(Optional.of(parameter));
        when(topicService.getTopic(topic.getTopicId())).thenReturn(Optional.of(topic));

        Workbook report = new XSSFWorkbook();
        reportService.writeReport(answers, parameterAssessments, topicAssessments, topicRecommendationMap, parameterRecommendationMap, report);

        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

    }

    @Test
    void shouldCreateHashMapForParameterLevelRecommendation() {
        AssessmentParameter parameter = new AssessmentParameter();
        parameter.setParameterName("my param");
        parameter.setParameterId(1);

        Integer assessmentId = 123;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendation("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);

        when(topicAndParameterLevelAssessmentService.getParameterAssessmentRecommendationData(assessmentId, parameterLevelRecommendation.getParameter().getParameterId())).thenReturn(parameterLevelRecommendationList);
        HashMap<Integer, List<ParameterLevelRecommendation>> parameterLevelRecommendationMap = reportService.getParameterWiseRecommendations(parameterLevelRecommendationList, assessmentId);

        assertEquals(1, parameterLevelRecommendationMap.size());
    }

    @Test
    void shouldCreateHashMapForTopicLevelRecommendation() {
        AssessmentTopic topic = new AssessmentTopic();
        topic.setTopicId(1);
        topic.setTopicName("my topic");

        Integer assessmentId = 123;
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(assessmentId);

        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendation("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        topicLevelRecommendationList.add(topicLevelRecommendation);

        when(topicAndParameterLevelAssessmentService.getTopicAssessmentRecommendationData(assessmentId, topicLevelRecommendation.getTopic().getTopicId())).thenReturn(topicLevelRecommendationList);
        HashMap<Integer, List<TopicLevelRecommendation>> topicLevelRecommendationMap = reportService.getTopicWiseRecommendations(topicLevelRecommendationList, assessmentId);

        assertEquals(1, topicLevelRecommendationMap.size());
    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("my category");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }


    private Workbook getMockWorkbookForChart() {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("Charts");
        return workbook;
    }

    @Test
    void shouldCreateDataAndGenerateChart() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);

        List<AssessmentCategory> assessmentCategories = new ArrayList<>();
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategories.add(assessmentCategory1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("First Module");
        assessmentModule.setCategory(assessmentCategory1);
        assessmentModule.setActive(true);

        assessmentCategory1.setModules(Collections.singleton(assessmentModule));

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(assessmentModule);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        assessmentTopicReference.setReferenceId(1);
        assessmentTopicReference.setReference("First Reference");
        assessmentTopicReference.setRating(Rating.ONE);
        assessmentTopicReference.setTopic(assessmentTopic);

        assessmentTopic.setReferences(Collections.singleton(assessmentTopicReference));

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment);
        topicLevelId.setTopic(assessmentTopic);

        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(1);

        when(categoryRepository.findAll()).thenReturn(assessmentCategories);

        String series1 = "Current Maturity";
        String series2 = "Desired Maturity";

        String category1 = "First Category";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(1.0, series2, category1);

        try {
            when(chartService.generateChart(any())).thenReturn(ChartsUtil.getSpiderChart(680, 480, dataset));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Workbook workbook = new XSSFWorkbook();
        reportService.createDataAndGenerateChart(workbook, assessment.getAssessmentId(), Collections.singletonList(parameterLevelAssessment), Collections.singletonList(topicLevelAssessment));
        verify(chartService).generateChart(any());
        assertEquals(workbook.getSheetAt(0).getSheetName(), getMockWorkbookForChart().getSheetAt(0).getSheetName());
    }

    @Test
    void shouldCreateSunBurstChartData(){
        Assessment assessment = new Assessment();
        assessment.setAssessmentId(1);

        List<AssessmentCategory> assessmentCategories = new ArrayList<>();
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategory1.setActive(true);
        assessmentCategories.add(assessmentCategory1);


        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("First Module");
        assessmentModule.setCategory(assessmentCategory1);
        assessmentModule.setActive(true);

        assessmentCategory1.setModules(Collections.singleton(assessmentModule));

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        assessmentModule.setTopics(Collections.singleton(assessmentTopic));

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        assessmentTopicReference.setReferenceId(1);
        assessmentTopicReference.setReference("First Reference");
        assessmentTopicReference.setRating(Rating.FIVE);
        assessmentTopicReference.setTopic(assessmentTopic);

        assessmentTopic.setReferences(Collections.singleton(assessmentTopicReference));

        ParameterLevelAssessment parameterLevelAssessment = new ParameterLevelAssessment();
        TopicLevelAssessment topicLevelAssessment = new TopicLevelAssessment();
        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment);
        topicLevelId.setTopic(assessmentTopic);

        topicLevelAssessment.setTopicLevelId(topicLevelId);
        topicLevelAssessment.setRating(5);

        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessment.getAssessmentId())).thenReturn(Collections.singletonList(parameterLevelAssessment));
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessment.getAssessmentId())).thenReturn(Collections.singletonList(topicLevelAssessment));
        when(categoryRepository.findAll()).thenReturn(assessmentCategories);

        List<AssessmentCategory> actualAssessmentCategoryList = reportService.generateSunburstData(assessment.getAssessmentId());
        double expectedDataAverageRating = 5;
        double actualDataAverageRating = actualAssessmentCategoryList.get(0).getCategoryAverage();

        assertEquals(expectedDataAverageRating, actualDataAverageRating);

    }
}

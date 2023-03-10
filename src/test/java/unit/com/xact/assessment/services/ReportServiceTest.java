/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.services;


import com.xact.assessment.dtos.Recommendation;
import com.xact.assessment.dtos.RecommendationEffort;
import com.xact.assessment.dtos.SummaryResponse;
import com.xact.assessment.models.*;
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

import static com.xact.assessment.dtos.RecommendationDeliveryHorizon.LATER;
import static com.xact.assessment.dtos.RecommendationEffort.HIGH;
import static com.xact.assessment.dtos.RecommendationImpact.LOW;
import static com.xact.assessment.models.AssessmentStatus.Active;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
    AnswerService answerService = mock(AnswerService.class);
    SpiderChartService chartService = mock(SpiderChartService.class);
    UserQuestionService userQuestionService = mock(UserQuestionService.class);
    ModuleService moduleService = mock(ModuleService.class);
    AssessmentMasterDataService assessmentMasterDataService = mock(AssessmentMasterDataService.class);


    private final ReportService reportService = new ReportService(topicAndParameterLevelAssessmentService, chartService,  assessmentMasterDataService,  userQuestionService, moduleService);

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
        module.setActive(true);
        AssessmentTopic topic1 = new AssessmentTopic("abc", module, true, "");
        AssessmentParameter parameter1 = AssessmentParameter.builder().parameterName("def").topic(topic1).isActive(true).comments("").build();
        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference(parameter1, Rating.FOUR, "");
        parameter1.setReferences(Collections.singleton(assessmentParameterReference));
        Set<AssessmentTopic> assessmentTopics = new HashSet<>();
        assessmentTopics.add(topic);
        assessmentTopics.add(topic1);
        Set<AssessmentParameter> assessmentParameters = new HashSet<>();
        assessmentParameters.add(parameter);
        assessmentParameters.add(parameter1);
        Set<AssessmentModule> assessmentModules = new HashSet<>();
        assessmentModules.add(module);
        category.setModules(assessmentModules);
        module.setTopics(assessmentTopics);
        topic.setParameters(Collections.singleton(parameter));
        topic1.setParameters(assessmentParameters);

        topic.setModule(module);
        parameter.setTopic(topic);
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);

        UserQuestion userQuestion = new UserQuestion();
        userQuestion.setAssessment(assessment);
        userQuestion.setParameter(parameter);
        userQuestion.setQuestionId(1);
        userQuestion.setQuestion("question?");
        userQuestion.setAnswer("answer");
        when(userQuestionService.findByAssessmentAndAnswer(assessmentId)).thenReturn(Collections.singletonList(userQuestion));

        List<ParameterLevelRating> parameterAssessments = new ArrayList<>();
        List<TopicLevelRating> topicAssessments = new ArrayList<>();
        ParameterLevelRating parameterAssessment = new ParameterLevelRating();
        parameterAssessment.setRating(3);
        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelRating topicAssessment = new TopicLevelRating(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);
        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendationText("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendationText("some text");
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
        parameterLevelRecommendation.setRecommendationText("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendationText("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);

        when(topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessmentId)).thenReturn(parameterAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicLevelRecommendations(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getParameterLevelRecommendations(assessmentId)).thenReturn(parameterLevelRecommendationList);
        when(assessmentMasterDataService.getUserAssessmentCategories(assessmentId)).thenReturn(Collections.singletonList(category));

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

        Workbook report = reportService.generateReport(assessmentId);

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
        module.setModuleId(1);
        module.setActive(true);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);
        category.setModules(Collections.singleton(module));

        topic.setModule(module);
        module.setTopics(Collections.singleton(topic));
        parameter.setTopic(topic);
        topic.setParameters(Collections.singleton(parameter));
        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        List<ParameterLevelRating> parameterAssessments = new ArrayList<>();
        List<TopicLevelRating> topicAssessments = new ArrayList<>();
        ParameterLevelRating parameterAssessment = new ParameterLevelRating();
        parameterAssessment.setRating(3);


        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment, parameter));
        parameterAssessments.add(parameterAssessment);

        TopicLevelId topicLevelId = new TopicLevelId(assessment, topic);
        TopicLevelRating topicAssessment = new TopicLevelRating(topicLevelId, 4, new Date(), new Date());
        topicAssessments.add(topicAssessment);

        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendationText("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendationText("some text");
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
        parameterLevelRecommendation.setRecommendationText("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendationText("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);

        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        when(assessmentMasterDataService.getUserAssessmentCategories(assessmentId)).thenReturn(Collections.singletonList(category));
        when(topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessmentId)).thenReturn(parameterAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessmentId)).thenReturn(topicAssessments);
        when(topicAndParameterLevelAssessmentService.getTopicLevelRecommendations(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getParameterLevelRecommendations(assessmentId)).thenReturn(parameterLevelRecommendationList);

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

        Workbook report = reportService.generateReport(assessment.getAssessmentId());

        assertEquals(getMockWorkbook().getSheetAt(0).getSheetName(), report.getSheetAt(0).getSheetName());
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
        module.setModuleId(1);
        module.setModuleName("my module");
        module.setActive(true);
        AssessmentCategory category = new AssessmentCategory();
        category.setCategoryName("my category");
        module.setCategory(category);
        category.setModules(Collections.singleton(module));

        topic.setModule(module);
        topic1.setModule(module);
        module.setTopics(Collections.singleton(topic));
        parameter.setTopic(topic);
        parameter1.setTopic(topic1);
        topic.setParameters(Collections.singleton(parameter));

        question.setParameter(parameter);
        AnswerId answerId = new AnswerId(assessment, question);
        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
        answers.add(answer);
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);

        HashMap<Integer, List<TopicLevelRecommendation>> topicRecommendationMap = new HashMap<>();
        List<TopicLevelRecommendation> topicLevelRecommendationList = new ArrayList<>();
        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation();
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setRecommendationText("some text");
        topicLevelRecommendation.setTopic(topic);
        topicLevelRecommendation.setAssessment(assessment);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setRecommendationEffort(HIGH);
        topicLevelRecommendation.setDeliveryHorizon(LATER);

        TopicLevelRecommendation topicLevelRecommendation1 = new TopicLevelRecommendation();
        topicLevelRecommendation1.setRecommendationId(2);
        topicLevelRecommendation1.setRecommendationText("some text");
        topicLevelRecommendation1.setTopic(topic);
        topicLevelRecommendation1.setAssessment(assessment);
        topicLevelRecommendation1.setRecommendationImpact(LOW);
        topicLevelRecommendation1.setRecommendationEffort(HIGH);
        topicLevelRecommendation1.setDeliveryHorizon(LATER);

        topicLevelRecommendationList.add(topicLevelRecommendation);
        topicLevelRecommendationList.add(topicLevelRecommendation1);
        topicRecommendationMap.put(topic.getTopicId(), topicLevelRecommendationList);

        HashMap<Integer, List<ParameterLevelRecommendation>> parameterRecommendationMap = new HashMap<>();
        List<ParameterLevelRecommendation> parameterLevelRecommendationList = new ArrayList<>();
        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation();
        parameterLevelRecommendation.setRecommendationId(1);
        parameterLevelRecommendation.setRecommendationText("some text");
        parameterLevelRecommendation.setParameter(parameter);
        parameterLevelRecommendation.setAssessment(assessment);
        parameterLevelRecommendation.setRecommendationImpact(LOW);
        parameterLevelRecommendation.setRecommendationEffort(HIGH);
        parameterLevelRecommendation.setDeliveryHorizon(LATER);

        ParameterLevelRecommendation parameterLevelRecommendation1 = new ParameterLevelRecommendation();
        parameterLevelRecommendation1.setRecommendationId(2);
        parameterLevelRecommendation1.setRecommendationText("some text");
        parameterLevelRecommendation1.setParameter(parameter);
        parameterLevelRecommendation1.setAssessment(assessment);
        parameterLevelRecommendation1.setRecommendationImpact(LOW);
        parameterLevelRecommendation1.setRecommendationEffort(HIGH);
        parameterLevelRecommendation1.setDeliveryHorizon(LATER);

        parameterLevelRecommendationList.add(parameterLevelRecommendation);
        parameterLevelRecommendationList.add(parameterLevelRecommendation1);

        parameterRecommendationMap.put(parameter.getParameterId(), parameterLevelRecommendationList);

        when(assessmentMasterDataService.getUserAssessmentCategories(assessmentId)).thenReturn(Collections.singletonList(category));
        when(topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessmentId)).thenReturn(new ArrayList<>());
        when(topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessmentId)).thenReturn(new ArrayList<>());
        when(topicAndParameterLevelAssessmentService.getTopicLevelRecommendations(assessmentId)).thenReturn(topicLevelRecommendationList);
        when(topicAndParameterLevelAssessmentService.getParameterLevelRecommendations(assessmentId)).thenReturn(parameterLevelRecommendationList);

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

        Workbook report = reportService.generateReport(assessment.getAssessmentId());

        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());

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
        workbook.createSheet("First Category");
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

        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment);
        topicLevelId.setTopic(assessmentTopic);

        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(1);


        List<TopicLevelRating> topicLevelRatings = new ArrayList<>();
        topicLevelRatings.add(topicLevelRating);

        when(assessmentMasterDataService.getUserAssessmentCategories(assessment.getAssessmentId())).thenReturn(Collections.singletonList(assessmentCategory1));
        when(topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessment.getAssessmentId())).thenReturn(topicLevelRatings);
        when(assessmentMasterDataService.getAllCategories()).thenReturn(assessmentCategories);

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

        Workbook workbook = reportService.generateReport(assessment.getAssessmentId());

        verify(chartService).generateChart(any());
        assertEquals(workbook.getSheetAt(0).getSheetName(), getMockWorkbookForChart().getSheetAt(0).getSheetName());
    }

    @Test
    void shouldCreateSunBurstChartData() {
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

        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment);
        topicLevelId.setTopic(assessmentTopic);

        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(5);

        when(topicAndParameterLevelAssessmentService.getParameterLevelRatings(assessment.getAssessmentId())).thenReturn(Collections.singletonList(parameterLevelRating));
        when(topicAndParameterLevelAssessmentService.getTopicLevelRatings(assessment.getAssessmentId())).thenReturn(Collections.singletonList(topicLevelRating));
        when(assessmentMasterDataService.getAllCategories()).thenReturn(assessmentCategories);
        when(assessmentMasterDataService.isModuleSelectedByUser(assessment,assessmentModule)).thenReturn(true);

        List<AssessmentCategory> actualAssessmentCategoryList = reportService.generateSunburstData(assessment);
        double expectedDataAverageRating = 5;
        double actualDataAverageRating = actualAssessmentCategoryList.get(0).getCategoryAverage();

        assertEquals(expectedDataAverageRating, actualDataAverageRating);

    }

    @Test
    void shouldGetAssessmentSummary() {
        Integer assessmentId = 1;
        Answer answer = new Answer();
        List<Answer> answers = new ArrayList<>();
        answers.add(answer);
        UserQuestion userQuestion = new UserQuestion();
        List<UserQuestion> questions = new ArrayList<>();
        questions.add(userQuestion);

        Date created1 = new Date(2022 - 7 - 13);
        Date updated1 = new Date(2022 - 9 - 24);

        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", organisation, Active, created1, updated1);

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic1);

        ParameterLevelId parameterLevelId = new ParameterLevelId();
        parameterLevelId.setAssessment(assessment1);
        parameterLevelId.setParameter(assessmentParameter);
        ParameterLevelRating parameterLevelRating = new ParameterLevelRating();
        parameterLevelRating.setParameterLevelId(parameterLevelId);
        parameterLevelRating.setRating(1);

        TopicLevelId topicLevelId = new TopicLevelId();
        topicLevelId.setAssessment(assessment1);
        topicLevelId.setTopic(assessmentTopic);
        TopicLevelRating topicLevelRating = new TopicLevelRating();
        topicLevelRating.setTopicLevelId(topicLevelId);
        topicLevelRating.setRating(2);

        when(answerService.getAnswers(1)).thenReturn(answers);
        when(userQuestionService.getUserQuestions(1)).thenReturn(questions);
        when(moduleService.getAssessedModules(Collections.singletonList(topicLevelRating),Collections.singletonList(parameterLevelRating))).thenReturn(1);
        when(assessmentMasterDataService.getAssessedCategory(Collections.singletonList(topicLevelRating),Collections.singletonList(parameterLevelRating))).thenReturn(1);

        SummaryResponse actualResponse = new SummaryResponse();
        actualResponse.setQuestionAssessed(2);
        actualResponse.setTopicAssessed(1);
        actualResponse.setParameterAssessed(1);
        actualResponse.setModuleAssessed(1);
        actualResponse.setCategoryAssessed(0);
        SummaryResponse expectedResponse = reportService.getSummary(1);

        assertEquals(expectedResponse.getCategoryAssessed(), actualResponse.getCategoryAssessed());
    }

    @Test
    void getRecommendations() {
        Assessment assessment1 = new Assessment(1, "Name", "Client Assessment", new Organisation(), Active, new Date(), new Date());

        AssessmentCategory assessmentCategory = new AssessmentCategory();
        assessmentCategory.setCategoryId(1);

        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setCategory(assessmentCategory);

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setModule(assessmentModule);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setModule(assessmentModule);

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setTopic(assessmentTopic1);

        TopicLevelRecommendation topicLevelRecommendation = new TopicLevelRecommendation(assessmentTopic);
        topicLevelRecommendation.setRecommendationText("recommendation");
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setAssessment(assessment1);
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setDeliveryHorizon(LATER);

        ParameterLevelRecommendation parameterLevelRecommendation = new ParameterLevelRecommendation(assessmentParameter);
        parameterLevelRecommendation.setRecommendationText("recommendation");
        topicLevelRecommendation.setRecommendationId(1);
        topicLevelRecommendation.setAssessment(new Assessment());
        topicLevelRecommendation.setRecommendationEffort(RecommendationEffort.LOW);
        topicLevelRecommendation.setRecommendationImpact(LOW);
        topicLevelRecommendation.setDeliveryHorizon(LATER);

        when(topicAndParameterLevelAssessmentService.getTopicRecommendations(1)).thenReturn(Collections.singletonList(topicLevelRecommendation));
        when(topicAndParameterLevelAssessmentService.getParameterRecommendations(1)).thenReturn(Collections.singletonList(parameterLevelRecommendation));

        List<Recommendation> recommendations = reportService.getRecommendations(1);

        assertEquals("recommendation",recommendations.get(0).getRecommendation());
    }
}

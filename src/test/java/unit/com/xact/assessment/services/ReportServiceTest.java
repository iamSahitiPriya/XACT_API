///*
// *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
// */
//
//package unit.com.xact.assessment.services;
//
//
//import com.xact.assessment.models.*;
//import com.xact.assessment.repositories.CategoryRepository;
//import com.xact.assessment.services.*;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class ReportServiceTest {
//
//    TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService = mock(TopicAndParameterLevelAssessmentService.class);
//    AnswerService answerService = mock(AnswerService.class);
//    SpiderChartService chartService = mock(SpiderChartService.class);
//    AverageCategoryService averageCategoryService = mock(AverageCategoryService.class);
//    CategoryRepository categoryRepository = mock(CategoryRepository.class);
//
//
//    private final ReportService reportService = new ReportService(topicAndParameterLevelAssessmentService, answerService,chartService,averageCategoryService,categoryRepository);
//
//    @Test
//    void getWorkbook() {
//        Integer assessmentId = 123;
//        List<Answer> answers = new ArrayList<>();
//
//        Assessment assessment = new Assessment();
//        assessment.setAssessmentId(assessmentId);
//        Question question = new Question();
//        question.setQuestionText("Question");
//        question.setQuestionId(1);
//        AssessmentParameter parameter = new AssessmentParameter();
//        parameter.setParameterName("my param");
//        AssessmentTopic topic = new AssessmentTopic();
//        topic.setTopicName("my topic");
//        AssessmentModule module = new AssessmentModule();
//        module.setModuleName("my module");
//        AssessmentCategory category = new AssessmentCategory();
//        category.setCategoryName("my category");
//        module.setCategory(category);
//
//        topic.setModule(module);
//        parameter.setTopic(topic);
//        question.setParameter(parameter);
//        AnswerId answerId = new AnswerId(assessment, question);
//        Answer answer = new Answer(answerId, "my answer", new Date(), new Date());
//        answers.add(answer);
//        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
//        List<ParameterLevelAssessment> parameterAssessments = new ArrayList<>();
//        List<TopicLevelAssessment> topicAssessments = new ArrayList<>();
//        ParameterLevelAssessment parameterAssessment = new ParameterLevelAssessment();
//        parameterAssessment.setRating(3);
//        parameterAssessment.setRecommendation("recommendation");
//        parameterAssessment.setParameterLevelId(new ParameterLevelId(assessment,parameter));
//        parameterAssessments.add(parameterAssessment);
//
//        TopicLevelId topicLevelId = new TopicLevelId(assessment,topic);
//        TopicLevelAssessment topicAssessment = new TopicLevelAssessment(topicLevelId,4,"recom",new Date(),new Date());
//        topicAssessments.add(topicAssessment);
//        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterAssessments);
//        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicAssessments);
//
//
//        Workbook report = reportService.generateReport(assessmentId);
//
//        assertEquals(report.getSheetAt(0).getSheetName(), getMockWorkbook().getSheetAt(0).getSheetName());
//
//    }
//
//    private Workbook getMockWorkbook() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet categorySheet = workbook.createSheet("my category");
//        Row row = categorySheet.createRow(1);
//        row.createCell(1).setCellValue("Demo");
//        return workbook;
//    }
//
//
//}

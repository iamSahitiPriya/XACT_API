/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ReportController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ReportControllerTest {

    private final Authentication authentication = Mockito.mock(Authentication.class);
    private final ReportService reportService = Mockito.mock(ReportService.class);
    private final AssessmentService assessmentService = Mockito.mock(AssessmentService.class);
    private final UserAuthService userAuthService = Mockito.mock(UserAuthService.class);

    private  final AnswerService answerService=Mockito.mock(AnswerService.class);
    private final TopicAndParameterLevelAssessmentService topicAndParameterLevelAssessmentService=Mockito.mock(TopicAndParameterLevelAssessmentService.class);
    private ReportController reportController = new ReportController(reportService, assessmentService, userAuthService);

    @Test
    void shouldGetReport() {
        Integer assessmentId = 123;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(reportService.generateReport(assessmentId)).thenReturn(getMockWorkbook());
        List<ParameterLevelRating> parameterLevelRatings =new ArrayList<>();
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterLevelRatings);
        List<Answer> answers=new ArrayList<>();
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        List<TopicLevelRating> topicLevelRatings =new ArrayList<>();
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicLevelRatings);

        MutableHttpResponse<byte[]> xlsDataResponse = reportController.getReport(assessmentId, authentication);


        assertEquals(HttpStatus.OK, xlsDataResponse.status());
        assertNotNull(xlsDataResponse.body());
    }

    private Workbook getMockWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        Sheet categorySheet = workbook.createSheet("Category");
        Row row = categorySheet.createRow(1);
        row.createCell(1).setCellValue("Demo");
        return workbook;
    }

    @Test
    void shouldGetAssessmentReportData(){
        Integer assessmentId = 123;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);


        List<AssessmentCategory> assessmentCategories = new ArrayList<>();
        AssessmentCategory assessmentCategory1 = new AssessmentCategory();
        assessmentCategory1.setCategoryId(1);
        assessmentCategory1.setCategoryName("First Category");
        assessmentCategory1.setActive(true);
        assessmentCategories.add(assessmentCategory1);
        AssessmentCategory assessmentCategory2 = new AssessmentCategory("Second Category",true,"2");
        assessmentCategory2.setCategoryId(2);
        assessmentCategories.add(assessmentCategory2);


        AssessmentModule assessmentModule = new AssessmentModule();
        assessmentModule.setModuleId(1);
        assessmentModule.setModuleName("First Module");
        assessmentModule.setCategory(assessmentCategory1);
        assessmentModule.setActive(true);

        AssessmentModule assessmentModule1 = new AssessmentModule("Second Module",assessmentCategory2,true,"");

        assessmentCategory1.setModules(Set.of(assessmentModule));
        assessmentCategory2.setModules(Set.of(assessmentModule1));

        AssessmentTopic assessmentTopic = new AssessmentTopic();
        assessmentTopic.setTopicId(1);
        assessmentTopic.setTopicName("First Topic");
        assessmentTopic.setModule(assessmentModule);
        assessmentTopic.setActive(true);

        assessmentModule.setTopics(Set.of(assessmentTopic));

        AssessmentTopicReference assessmentTopicReference = new AssessmentTopicReference();
        assessmentTopicReference.setReferenceId(1);
        assessmentTopicReference.setReference("First Reference");
        assessmentTopicReference.setRating(Rating.FIVE);
        assessmentTopicReference.setTopic(assessmentTopic);

        assessmentTopic.setReferences(Set.of(assessmentTopicReference));
        assessmentTopic.setRating(5);

        AssessmentTopic assessmentTopic1 = new AssessmentTopic();
        assessmentTopic1.setTopicId(2);
        assessmentTopic1.setTopicName("Second Topic");
        assessmentTopic1.setModule(assessmentModule1);
        assessmentTopic1.setActive(true);

        assessmentModule1.setTopics(Set.of(assessmentTopic1));

        AssessmentParameter assessmentParameter = new AssessmentParameter();
        assessmentParameter.setParameterId(1);
        assessmentParameter.setParameterName("First Parameter");
        assessmentParameter.setActive(true);
        assessmentParameter.setTopic(assessmentTopic1);
        assessmentTopic1.setParameters(Set.of(assessmentParameter));

        AssessmentParameterReference assessmentParameterReference = new AssessmentParameterReference();
        assessmentParameterReference.setReference("Second Reference");
        assessmentParameterReference.setRating(Rating.FIVE);
        assessmentParameterReference.setParameter(assessmentParameter);

        assessmentParameter.setReferences(Set.of(assessmentParameterReference));
        assessmentParameter.setRating(5);

        when(reportService.generateSunburstData(assessment)).thenReturn(assessmentCategories);

        MutableHttpResponse<ReportDataResponse>  actualResponse = reportController.getAssessmentReportData(assessmentId,authentication);
        MutableHttpResponse<ReportDataResponse> expectedResponse = HttpResponse.ok();
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldGetTemplateReport(){
        MutableHttpResponse<byte[]> actualResponse = reportController.getReportTemplate();
        MutableHttpResponse<ReportDataResponse> expectedResponse = HttpResponse.ok();
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldGetRecommendations() {
        Integer assessmentId = 123;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name","Client Assessment", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUser assessmentUser = new AssessmentUser();
        assessmentUser.setUserId(userId);

        when(assessmentService.getAssessment(assessment.getAssessmentId(),user)).thenReturn(assessment);
        Recommendation recommendation = new Recommendation("recommendation", RecommendationDeliveryHorizon.NOW, RecommendationImpact.LOW, RecommendationEffort.LOW,"category",new Date());
        Recommendation recommendation1 = new Recommendation("recommendation", RecommendationDeliveryHorizon.NEXT,RecommendationImpact.LOW,RecommendationEffort.LOW,"category",new Date());
        List<Recommendation> recommendations = new ArrayList<>();
        recommendations.add(recommendation);
        recommendations.add(recommendation1);

        when(reportService.getRecommendations(assessmentId)).thenReturn(recommendations);

        HttpResponse<List<Recommendation>> actualResponse = reportController.getRecommendations(1,authentication);

        assertEquals(HttpStatus.OK, actualResponse.status());
    }

}


/*
 *  Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ReportController;
import com.xact.assessment.models.*;
import com.xact.assessment.services.*;
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
    void getReport() {
        Integer assessmentId = 123;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        Profile profile = new Profile();
        profile.setEmail(userEmail);
        user.setProfile(profile);

        when(userAuthService.getLoggedInUser(authentication)).thenReturn(user);

        UserId userId = new UserId();
        userId.setUserEmail("hello@thoughtworks.com");

        Date created = new Date(2022 - 4 - 13);
        Date updated = new Date(2022 - 4 - 13);
        Organisation organisation = new Organisation(2, "abc", "hello", "ABC", 4);

        Assessment assessment = new Assessment(1, "Name", organisation, AssessmentStatus.Active, created, updated);
        userId.setAssessment(assessment);

        AssessmentUsers assessmentUsers = new AssessmentUsers();
        assessmentUsers.setUserId(userId);

        when(assessmentService.getAssessment(assessmentId, user)).thenReturn(assessment);
        when(reportService.generateReport(assessmentId)).thenReturn(getMockWorkbook());
        List<ParameterLevelAssessment> parameterLevelAssessments=new ArrayList<>();
        when(topicAndParameterLevelAssessmentService.getParameterAssessmentData(assessmentId)).thenReturn(parameterLevelAssessments);
        List<Answer> answers=new ArrayList<>();
        when(answerService.getAnswers(assessmentId)).thenReturn(answers);
        List<TopicLevelAssessment> topicLevelAssessments=new ArrayList<>();
        when(topicAndParameterLevelAssessmentService.getTopicAssessmentData(assessmentId)).thenReturn(topicLevelAssessments);

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
}


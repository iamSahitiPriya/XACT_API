/*
 * Copyright (c) 2022 - Thoughtworks Inc. All rights reserved.
 */

package unit.com.xact.assessment.controllers;

import com.xact.assessment.controllers.ContributorController;
import com.xact.assessment.dtos.*;
import com.xact.assessment.models.*;
import com.xact.assessment.services.AssessmentMasterDataService;
import com.xact.assessment.services.QuestionService;
import com.xact.assessment.services.UserAuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.security.authentication.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static com.xact.assessment.dtos.ContributorRole.AUTHOR;
import static org.mockito.Mockito.*;

class ContributorControllerTest {

    private final QuestionService questionService = mock(QuestionService.class);

    private final UserAuthService userAuthService=mock(UserAuthService.class);
    private final AssessmentMasterDataService assessmentMasterDataService = mock(AssessmentMasterDataService.class);
    private final ContributorController contributorController = new ContributorController(questionService, userAuthService, assessmentMasterDataService);

    private final Authentication authentication = Mockito.mock(Authentication.class);


    @Test
    void shouldGetContributorQuestions() {
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);
        ContributorResponse contributorResponse = new ContributorResponse();
        ContributorModuleData contributorModuleData = new ContributorModuleData();
        contributorModuleData.setModuleName("Module");
        ContributorTopicData contributorTopicData = new ContributorTopicData();
        contributorTopicData.setTopicName("Topic");
        ContributorParameterData contributorParameterData = new ContributorParameterData();
        contributorParameterData.setParameterName("Parameter");
        ContributorQuestionData contributorQuestionData = new ContributorQuestionData();
        contributorQuestionData.setQuestionId(1);
        contributorQuestionData.setQuestion("Question?");
        contributorQuestionData.setStatus(ContributorQuestionStatus.DRAFT);
        contributorParameterData.setQuestions(Collections.singletonList(contributorQuestionData));
        contributorTopicData.setParameters(Collections.singletonList(contributorParameterData));
        contributorModuleData.setTopics(Collections.singletonList(contributorTopicData));

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(questionService.getContributorResponse(AUTHOR, userEmail)).thenReturn(contributorResponse);

        HttpResponse<ContributorResponse> actualResponse = contributorController.getContributorQuestions(AUTHOR, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldUpdateContributorQuestionStatus() {
        Integer questionId = 1;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);
        QuestionStatusUpdateRequest questionStatusUpdateRequest = new QuestionStatusUpdateRequest();
        questionStatusUpdateRequest.setQuestionId(Collections.singletonList(questionId));
        questionStatusUpdateRequest.setComments("comments");
        QuestionStatusUpdateResponse questionStatusUpdateResponse = new QuestionStatusUpdateResponse();
        questionStatusUpdateResponse.setQuestionId(questionStatusUpdateRequest.getQuestionId());
        questionStatusUpdateResponse.setStatus(ContributorQuestionStatus.SENT_FOR_REVIEW);
        questionStatusUpdateResponse.setComments("comments");

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        when(questionService.updateContributorQuestionsStatus(1, ContributorQuestionStatus.SENT_FOR_REVIEW, questionStatusUpdateRequest, userEmail)).thenReturn(questionStatusUpdateResponse);

        HttpResponse<QuestionStatusUpdateResponse> actualResponse = contributorController.updateContributorQuestionsStatus(1, ContributorQuestionStatus.SENT_FOR_REVIEW, questionStatusUpdateRequest, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());
    }

    @Test
    void shouldDeleteQuestion() {
        Integer questionId = 1;
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);
        doNothing().when(questionService).deleteQuestion(questionId, userEmail);

        HttpResponse<Question> actualResponse = contributorController.deleteQuestion(questionId, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }

    @Test
    void shouldUpdateQuestionText() {
        Integer questionId = 1;
        String questionText = "new question";
        User user = new User();
        String userEmail = "hello@thoughtworks.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userEmail);
        user.setUserInfo(userInfo);

        AssessmentCategory assessmentCategory = new AssessmentCategory(1, "category", true, "");
        AssessmentModule assessmentModule = new AssessmentModule(1, "moduleName", assessmentCategory, true, "");
        AssessmentTopic topic = new AssessmentTopic(1, "topicName", assessmentModule, true, "");
        AssessmentParameter parameter = AssessmentParameter.builder().parameterId(1).parameterName("parameterName").topic(topic).isActive(true).comments("").build();
        Question question = new Question();
        question.setQuestionId(questionId);
        question.setQuestionText(questionText);
        question.setParameter(parameter);


        when(questionService.updateContributorQuestion(questionId, questionText,userEmail)).thenReturn(question);
        when(userAuthService.getCurrentUser(authentication)).thenReturn(user);

        HttpResponse<QuestionDto> actualResponse = contributorController.updateQuestion(questionId, questionText, authentication);

        Assertions.assertEquals(HttpResponse.ok().getStatus(), actualResponse.getStatus());

    }
}
